package com.websarva.wings.android.towerwalk.util;

public class TransitionUtils {

    // インターバル（ミリ秒）
    private static final long INTERVAL = 700;
    // 一番最後にクリックされた時間
    private static long sLastClickTime = 0;

    public static boolean isTransitionAvailable() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - sLastClickTime) >= INTERVAL) {
            sLastClickTime = currentTime;
            return true;
        }
        return false;
    }
}
