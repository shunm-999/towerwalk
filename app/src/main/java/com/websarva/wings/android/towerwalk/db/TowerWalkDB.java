package com.websarva.wings.android.towerwalk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;
import android.util.Log;

import com.websarva.wings.android.towerwalk.consts.GameConst.GameStatus;
import com.websarva.wings.android.towerwalk.TowerWalkApplication;
import com.websarva.wings.android.towerwalk.util.DateUtils;
import com.websarva.wings.android.towerwalk.util.LogUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TowerWalkDB extends Database {

    // タグ
    private static final String TAG = TowerWalkDB.class.getSimpleName();

    // データベース名
    static final String DATABASE_NAME = "towerwalk.db";
    // データベースのバージョン
    static final int DATABASE_VERSION = 1;

    // データベースヘルパー
    private static final SQLiteOpenHelper sDatabaseHelper = new CompetitionHistoryDatabaseHelper(TowerWalkApplication.getInstance().getApplicationContext());

    /**
     * TowerWalkDBを構築する
     */
    private TowerWalkDB() {
    }

    /**
     * データベースヘルパークラス
     */
    private static class CompetitionHistoryDatabaseHelper extends SQLiteOpenHelper {

        /**
         * CompetitionHistoryDatabaseHelperを構築する
         *
         * @param context コンテキスト
         */
        public CompetitionHistoryDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql;

            // 対戦記録テーブルを作成する
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" CREATE TABLE " + CompetitionHistoryTable.TABLE_NAME);
            stringBuilder.append(" ( ");
            stringBuilder.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , ");
            stringBuilder.append(CompetitionHistoryTable.COLUMN_GAME_RESULT + " INTEGER , ");
            stringBuilder.append(CompetitionHistoryTable.COLUMN_NAME + " TEXT , ");
            stringBuilder.append(CompetitionHistoryTable.COLUMN_CREATE_DATE + " TEXT  ");
            stringBuilder.append(" ); ");

            sql = stringBuilder.toString();

            sqLiteDatabase.execSQL(sql);
            LogUtils.iTag("sql ->", sql);

            stringBuilder = new StringBuilder();

            // 対戦記録詳細テーブルを作成する
            stringBuilder.append(" CREATE TABLE " + CompetitionHistoryDetailTable.TABLE_NAME);
            stringBuilder.append(" ( ");
            stringBuilder.append(BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_HISTORY_ID + " INTEGER , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_COUNT + " INTEGER , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_TURN + " INTEGER , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_LINE + " INTEGER , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_ROW + " INTEGER ,  ");
            stringBuilder.append(" FOREIGN KEY ( " + CompetitionHistoryDetailTable.COLUMN_HISTORY_ID + " )  REFERENCES " + CompetitionHistoryTable.TABLE_NAME + " ( " + BaseColumns._ID + " ) ");
            stringBuilder.append(" ); ");

            sql = stringBuilder.toString();

            sqLiteDatabase.execSQL(sql);
            LogUtils.iTag("sql ->", sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        }
    }

    /**
     * 対戦結果を記録する
     *
     * @param gameStatus 勝敗
     * @param resultList 対戦記録
     * @return 挿入件数
     */
    public static long recordResult(GameStatus gameStatus, List<Map<Integer, Integer>> resultList) {
        SQLiteDatabase database = sDatabaseHelper.getWritableDatabase();
        int insertNumber = 0;

        try {
            // トランザクション開始
            database.beginTransaction();

            ContentValues contentValues = new ContentValues();
            long currentTime = System.currentTimeMillis();
            switch (gameStatus) {
                case WIN:
                    contentValues.put(CompetitionHistoryTable.COLUMN_GAME_RESULT, CompetitionHistoryTable.GameResult.WIN.getResult());
                    break;
                case DRAW:
                    contentValues.put(CompetitionHistoryTable.COLUMN_GAME_RESULT, CompetitionHistoryTable.GameResult.DRAW.getResult());
                    break;
                case LOSE:
                    contentValues.put(CompetitionHistoryTable.COLUMN_GAME_RESULT, CompetitionHistoryTable.GameResult.LOSE.getResult());
                    break;
            }

            contentValues.put(CompetitionHistoryTable.COLUMN_NAME, DateUtils.convertUnixTimeToDate(currentTime));
            contentValues.put(CompetitionHistoryTable.COLUMN_CREATE_DATE, String.valueOf(currentTime));
            database.insert(CompetitionHistoryTable.TABLE_NAME, null, contentValues);

            int historyId;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" SELECT " + BaseColumns._ID + " FROM " + CompetitionHistoryTable.TABLE_NAME);
            stringBuilder.append(" WHERE  " + BaseColumns._ID + " NOT IN ( ");
            stringBuilder.append(" SELECT " + CompetitionHistoryDetailTable.COLUMN_HISTORY_ID + " FROM " + CompetitionHistoryDetailTable.TABLE_NAME);
            stringBuilder.append(" ) ");
            String sqlSelect = stringBuilder.toString();

            Cursor cursor = database.rawQuery(sqlSelect, null);
            if (cursor.moveToNext()) {
                historyId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            } else {
                return -1;
            }

            stringBuilder = new StringBuilder();
            stringBuilder.append(" INSERT INTO " + CompetitionHistoryDetailTable.TABLE_NAME + " ( ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_HISTORY_ID + " , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_COUNT + " , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_TURN + " , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_LINE + " , ");
            stringBuilder.append(CompetitionHistoryDetailTable.COLUMN_ROW);
            stringBuilder.append(" ) ");
            stringBuilder.append(" VALUES (?,?,?,?,?) ");
            String sqlInsert = stringBuilder.toString();

            SQLiteStatement sqLiteStatement = database.compileStatement(sqlInsert);
            sqLiteStatement.bindLong(1, historyId);

            try {
                for (int i = 0; i < resultList.size(); i++) {
                    Map<Integer, Integer> position = resultList.get(i);
                    sqLiteStatement.bindLong(2, i);
                    sqLiteStatement.bindLong(3, i % 2);
                    for (Map.Entry<Integer, Integer> entry : position.entrySet()) {
                        sqLiteStatement.bindLong(4, entry.getKey());
                        sqLiteStatement.bindLong(5, entry.getValue());
                    }
                    insertNumber += sqLiteStatement.executeInsert();
                }
            } catch (SQLiteException e) {
                LogUtils.eTag("sql ->", e.getMessage());
            } finally {
                sqLiteStatement.close();
                // トランザクション成功
                database.setTransactionSuccessful();
            }

        } catch (SQLiteException e) {
            LogUtils.eTag("sql ->", e.getMessage());
            insertNumber = -1;
        } finally {
            // トランザクション終了
            database.endTransaction();
            database.close();
        }

        return insertNumber;
    }

    /**
     * 対戦記録画面用のデータ一覧を取得する
     *
     * @return 対戦記録のリスト
     */
    public static ResultSet getGameResultData() {
        ResultSet resultSet = selectAll(CompetitionHistoryTable.TABLE_NAME, sDatabaseHelper);
        if (resultSet == null) {
            return new ResultSet();
        }
        Collections.sort(resultSet, new Comparator<Result>() {
            @Override
            public int compare(Result result1, Result result2) {
                return result2.getInt(BaseColumns._ID) - result1.getInt(BaseColumns._ID);
            }
        });
        return resultSet;
    }

    /**
     * シーケンスIDと一致する、対戦記録画面用のデータを取得する
     *
     * @param sequenceId シーケンスID
     * @return 対戦記録のリスト
     */
    public static ResultSet getGameResultData(int sequenceId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BaseColumns._ID, sequenceId);
        return selectAll(CompetitionHistoryTable.TABLE_NAME, sDatabaseHelper);
    }

    /**
     * 対戦記録詳細画面用のデータを取得する
     */
    public static ResultSet getGameResultDetailData(int historyId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CompetitionHistoryDetailTable.COLUMN_HISTORY_ID, historyId);
        return select(CompetitionHistoryDetailTable.TABLE_NAME, contentValues, sDatabaseHelper);
    }
}
