package com.websarva.wings.android.towerwalk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.websarva.wings.android.towerwalk.util.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Database {

    // タグ
    private static final String TAG = Database.class.getSimpleName();

    /**
     * 検索結果を格納するクラス
     */
    protected static class Result extends LinkedHashMap<String, String> {

        /**
         * 結果を文字列で取得する
         *
         * @param key カラム名
         * @return 値
         */
        public String getString(String key) {
            return get(key);
        }

        /**
         * 結果をintで取得する
         *
         * @param key カラム名
         * @return 値
         */
        public int getInt(String key) {
            try {
                return Integer.parseInt(get(key));
            } catch (ClassCastException e) {
                LogUtils.eTag(TAG, e.getMessage());
            }
            return 0;
        }

        /**
         * 結果をintで取得する
         *
         * @param key カラム名
         * @return 値
         */
        public long getLong(String key) {
            try {
                return Long.parseLong(get(key));
            } catch (ClassCastException e) {
                LogUtils.eTag(TAG, e.getMessage());
            }
            return 0;
        }
    }

    /**
     * 検索結果のリストを格納するクラス
     */
    protected static class ResultSet extends ArrayList<Result> {

    }

    /**
     * テーブル内の全てのデータを取得する
     *
     * @param tableName        テーブル名
     * @param sqLiteOpenHelper データベースヘルパーオブジェクト
     * @return テーブル内の全てのデータ
     */
    protected static ResultSet selectAll(@NonNull String tableName, SQLiteOpenHelper sqLiteOpenHelper) {
        ResultSet resultSet = new ResultSet();

        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        String sqlSelect = " SELECT * FROM " + tableName;

        try {
            Cursor cursor = database.rawQuery(sqlSelect, null);
            while (cursor.moveToNext()) {
                Result result = new Result();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    // 取得した結果を、カラム名と値をセットにして格納する
                    result.put(cursor.getColumnName(i), cursor.getString(i));
                }
                resultSet.add(result);
            }
        } catch (SQLException e) {
            LogUtils.eTag(TAG, e.getMessage());
        } finally {
            database.close();
        }
        return !resultSet.isEmpty() ? resultSet : null;
    }

    /**
     * 条件に該当するデータを取得する
     *
     * @param tableName        テーブル名
     * @param contentValues    カラム名と値のペア
     * @param sqLiteOpenHelper データベースヘルパーオブジェクト
     * @return 条件に該当するデータ
     */
    protected static ResultSet select(@NonNull String tableName, ContentValues contentValues, @NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        if ((contentValues == null) || (contentValues.size() == 0)) {
            return selectAll(tableName, sqLiteOpenHelper);
        }

        ResultSet resultSet = new ResultSet();

        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();

        StringBuilder stringBuilder = new StringBuilder(" SELECT * FROM " + tableName + " WHERE ");
        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            columns.add(entry.getKey());
            values.add(entry.getKey());
        }

        for (int i = 0; i < contentValues.size(); i++) {
            if (i == (contentValues.size() - 1)) {
                stringBuilder.append(columns.get(i) + " = " + values.get(i));
            } else {
                stringBuilder.append(columns.get(i) + " = " + values.get(i) + " AND ");
            }
        }

        String sqlSelect = stringBuilder.toString();

        try {
            Cursor cursor = database.rawQuery(sqlSelect, null);
            while (cursor.moveToNext()) {
                Result result = new Result();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    // 取得した結果を、カラム名と値をセットにして格納する
                    result.put(cursor.getColumnName(i), cursor.getString(i));
                }
                resultSet.add(result);
            }
        } catch (SQLException e) {
            LogUtils.eTag(TAG, e.getMessage());
        } finally {
            database.close();
        }
        return !resultSet.isEmpty() ? resultSet : null;
    }

    /**
     * データを挿入する
     *
     * @param tableName        テーブル名
     * @param contentValues    カラム名と値のペア
     * @param sqLiteOpenHelper データベースヘルパーオブジェクト
     * @return 挿入件数
     */
    protected static long insert(@NonNull String tableName, ContentValues contentValues, @NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        long result = 0;
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        try {
            result = database.insert(tableName, null, contentValues);
        } catch (SQLiteException e) {
            LogUtils.eTag(TAG, e.getMessage());
            result = -1;
        } finally {
            database.close();
        }
        return result;
    }

    /**
     * テーブル内の全てのデータを削除する
     *
     * @param tableName        テーブル名
     * @param sqLiteOpenHelper データベースヘルパーオブジェクト
     * @return 削除した件数
     */
    protected static int deleteAll(@NonNull String tableName, @NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        int result = 0;
        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();
        try {
            result = database.delete(tableName, null, null);
        } catch (SQLiteException e) {
            LogUtils.eTag(TAG, e.getMessage());
            result = -1;
        } finally {
            database.close();
        }
        return result;
    }

    /**
     * 条件に該当するデータを削除する
     *
     * @param tableName        テーブル名
     * @param contentValues    カラム名と値のペア
     * @param sqLiteOpenHelper データベースヘルパーオブジェクト
     * @return 削除件数
     */
    protected static int delete(@NonNull String tableName, ContentValues contentValues, @NonNull SQLiteOpenHelper sqLiteOpenHelper) {
        if ((contentValues == null) || (contentValues.size() == 0)) {
            return deleteAll(tableName, sqLiteOpenHelper);
        }

        SQLiteDatabase database = sqLiteOpenHelper.getWritableDatabase();

        List<String> columns = new ArrayList<>();
        List<String> values = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
            columns.add(entry.getKey());
            values.add(entry.getKey());
        }
        for (int i = 0; i < columns.size(); i++) {
            if (i == (contentValues.size() - 1)) {
                stringBuilder.append(columns.get(i) + " = ?");
            } else {
                stringBuilder.append(columns.get(i) + " = ? AND ");
            }
        }

        String sqlDelete = stringBuilder.toString();

        int result = 0;
        try {
            result = database.delete(tableName, sqlDelete, values.toArray(new String[values.size()]));
        } catch (SQLiteException e) {
            LogUtils.eTag(TAG, e.getMessage());
            result = -1;
        } finally {
            database.close();
        }

        return result;
    }
}
