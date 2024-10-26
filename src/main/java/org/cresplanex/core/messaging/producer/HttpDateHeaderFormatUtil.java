package org.cresplanex.core.messaging.producer;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting HTTP date headers.
 *
 * HTTPの日付ヘッダーをフォーマットするためのユーティリティクラス.
 */
public class HttpDateHeaderFormatUtil {

    /**
     * Returns the current time as an HTTP date string.
     *
     * 現在時刻をHTTP日付文字列として返します.
     *
     * @return the current time as an HTTP date string
     */
    public static String nowAsHttpDateString() {
        return timeAsHttpDateString(ZonedDateTime.now(ZoneId.of("GMT")));
    }

    /**
     * Returns the given time as an HTTP date string.
     *
     * 指定された時刻をHTTP日付文字列として返します.
     *
     * @param gmtTime the time to format
     * @return the given time as an HTTP date string
     */
    public static String timeAsHttpDateString(ZonedDateTime gmtTime) {
        return gmtTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
