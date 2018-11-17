package com.websarva.wings.android.towerwalk.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ConvertUtils {
    /**
     * dpからpixelへの変換
     *
     * @param dp      dp
     * @param context コンテキスト
     * @return pixel
     */
    public static float convertDp2Px(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * metrics.density;
    }

    /**
     * pixelからdpへの変換
     *
     * @param px      px
     * @param context コンテキスト
     * @return dp
     */
    public static float convertPx2Dp(int px, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return px / metrics.density;
    }
}
