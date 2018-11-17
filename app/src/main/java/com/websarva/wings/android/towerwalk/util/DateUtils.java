package com.websarva.wings.android.towerwalk.util;

import java.text.SimpleDateFormat;

public class DateUtils {

    // フォーマット形式
    private static final String FORMAT_PATTERN = "yyyy/MM/dd HH:mm";

    /**
     * UNIX時間を日付に変更する
     *
     * @param unixTime UNIX時間
     * @return 日付
     */
    public static String convertUnixTimeToDate(long unixTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_PATTERN);
        return simpleDateFormat.format(unixTime);
    }
}
