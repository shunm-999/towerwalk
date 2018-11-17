package com.websarva.wings.android.towerwalk.db;

public class CompetitionHistoryDetailTable extends Table {

    // タグ
    private static final String TAG = CompetitionHistoryTable.class.getSimpleName();
    // テーブル名
    static final String TABLE_NAME = "CompetitionHistoryDetailTable";

    // 対戦記録ID
    static final String COLUMN_HISTORY_ID = "history_id";
    // 何手目か
    static final String COLUMN_COUNT = "count";
    // 手番
    static final String COLUMN_TURN = "turn";
    // 行
    static final String COLUMN_LINE = "line";
    // 列
    static final String COLUMN_ROW = "row";

    // 手番のリスト
    enum GameTurn {
        PLAYER(0),
        OPPONENT(1);

        private int mTurn;

        /**
         * GameResultを構築する
         */
        GameTurn(int turn) {
            mTurn = turn;
        }

        /**
         * 手番を取得する
         *
         * @return 手番
         */
        public int getTurn() {
            return mTurn;
        }
    }
}
