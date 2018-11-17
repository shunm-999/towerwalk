package com.websarva.wings.android.towerwalk.db;

public class CompetitionHistoryTable {

    // タグ
    private static final String TAG = CompetitionHistoryTable.class.getSimpleName();
    // テーブル名
    static final String TABLE_NAME = "CompetitionHistoryTable";
    // 勝敗
    static final String COLUMN_GAME_RESULT = "game_result";
    // 名前
    static final String COLUMN_NAME = "name";
    // 対戦日
    static final String COLUMN_CREATE_DATE = "create_date";

    // 勝敗のリスト
    enum GameResult {
        WIN(0),
        DRAW(1),
        LOSE(2);

        private int mResult;

        /**
         * GameResultを構築する
         */
        GameResult(int result) {
            mResult = result;
        }

        /**
         * 勝敗を取得する
         *
         * @return 勝敗
         */
        public int getResult() {
            return mResult;
        }
    }
}
