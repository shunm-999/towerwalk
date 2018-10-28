package com.websarva.wings.android.towerwalk.Const;

public class KeyMapConst {

    // ボタンのタグ（左）
    public static final String BUTTON_LEFT = "buttonLeft";
    // ボタンのタグ（上）
    public static final String BUTTON_TOP = "buttonTop";
    // ボタンのタグ（右）
    public static final String BUTTON_RIGHT = "buttonRight";
    // ボタンのタグ（下）
    public static final String BUTTON_BOTTOM = "buttonBottom";

    // ボタン押下時の移動距離
    public enum KeyMap {
        LEFT(new int[]{0, -1}, BUTTON_LEFT),
        TOP(new int[]{-1, 0}, BUTTON_TOP),
        RIGHT(new int[]{0, 1}, BUTTON_RIGHT),
        BOTTOM(new int[]{1, 0}, BUTTON_BOTTOM);

        // 座標
        private int[] mDistance;
        // タグ
        private String mStatus;

        /**
         * KeyMapを構築する
         */
        KeyMap(int[] distance, String status) {
            this.mDistance = distance;
            this.mStatus = status;
        }

        /**
         * 座標を取得する
         *
         * @return 座標
         */
        public int[] getDistance() {
            return mDistance;
        }

        /**
         * ステータスを取得する
         *
         * @return ステータス
         */
        public String getStatus() {
            return mStatus;
        }
    }
}
