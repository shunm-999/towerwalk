package com.websarva.wings.android.towerwalk.consts;

import com.websarva.wings.android.towerwalk.R;

public class GameConst {

    // 行数・列数
    public static final int SQUARE_NUMBER = 5;
    // タワーの最大の高さ
    public static final int MAX_TOWER_HIGH = 5;
    // 領域外のタワーの値
    public static final int OUTSIDE_TOWER_HIGH = -1;
    // 移動可能なタワーの高低差
    public static final int DIFFERENCE_IN_HEIGHT = 2;

    // ゲームの勝敗
    public enum GameStatus {
        PLAYING(-1),
        WIN(R.string.tower_walk_board_view_game_result_win),
        DRAW(R.string.tower_walk_board_view_game_result_draw),
        LOSE(R.string.tower_walk_board_view_game_result_lose),
        ERROR(R.string.tower_walk_board_view_game_result_error);

        GameStatus(int resultText) {
            mResultTextCode = resultText;
        }

        // ゲーム結果の文字列
        private int mResultTextCode;

        /**
         * ゲーム結果の文字列を取得する
         *
         * @return ゲーム結果の文字列
         */
        public int getResultTextCode() {
            return mResultTextCode;
        }
    }
}
