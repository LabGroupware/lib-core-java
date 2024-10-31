package org.cresplanex.core.messaging.consumer.decorator;

/**
 * 組み込みのメッセージハンドラデコレータの順序を定義するクラスです。
 * <p>
 * このクラスは、デコレータの適用順序を管理するために使用します。</p>
 */
public class BuiltInMessageHandlerDecoratorOrder {

    /**
     * メッセージの受信前後に実行されるデコレータの順序
     */
    public static int PRE_POST_RECEIVE_MESSAGE_HANDLER_DECORATOR = 100;
    /**
     * 重複検出を行うメッセージハンドラデコレータの順序
     */
    public static int DUPLICATE_DETECTING_MESSAGE_HANDLER_DECORATOR = 200;
    /**
     * メッセージの処理前後に実行されるデコレータの順序
     */
    public static int PRE_POST_HANDLER_MESSAGE_HANDLER_DECORATOR = 300;
}
