package org.cresplanex.core.events.common;

/**
 * イベントメッセージのヘッダーに使用する定数を定義するクラスです。
 */
public class EventMessageHeaders {

    /**
     * イベントのタイプを表すヘッダー
     */
    public static final String EVENT_TYPE = "event-type";

    /**
     * 集約のタイプを表すヘッダー
     */
    public static final String AGGREGATE_TYPE = "event-aggregate-type";

    /**
     * 集約のIDを表すヘッダー
     */
    public static final String AGGREGATE_ID = "event-aggregate-id";
}
