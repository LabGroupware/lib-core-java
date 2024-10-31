package org.cresplanex.core.messaging.producer;

import org.cresplanex.core.messaging.common.Message;

/**
 * メッセージプロデューサの基本インターフェース。 メッセージの送信や、必要に応じたメッセージIDの設定、およびコンテキスト内での処理をサポートします。
 */
public interface MessageProducerImplementation {

    /**
     * 指定されたメッセージを送信します。
     *
     * @param message 送信するメッセージオブジェクト
     */
    void send(Message message);

    /**
     * 必要に応じてメッセージにメッセージIDを設定します。 デフォルトでは何も行いません。
     *
     * @param message メッセージIDを設定するメッセージオブジェクト
     */
    default void setMessageIdIfNecessary(Message message) {
        // デフォルトでは何も行わない
    }

    /**
     * 指定された処理をコンテキスト内で実行します。 デフォルトでは直接処理を実行します。
     *
     * @param runnable 実行する処理
     */
    default void withContext(Runnable runnable) {
        runnable.run();
    }
}
