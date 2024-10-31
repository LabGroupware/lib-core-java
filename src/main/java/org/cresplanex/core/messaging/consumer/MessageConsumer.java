package org.cresplanex.core.messaging.consumer;

import java.util.Set;

/**
 * Interface for message consumers.
 *
 * メッセージコンシューマのためのインターフェース. コンシューマはこれを実装して、メッセージを受信するための処理を行います.
 */
public interface MessageConsumer {

    /**
     * Subscribes to the specified channels.
     *
     * 指定されたチャネルにサブスクライブします.
     *
     * @param subscriberId the subscriber ID
     * @param channels the channels
     * @param handler the message handler
     * @return the message subscription
     */
    MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);

    /**
     * Gets the ID of the message consumer.
     *
     * メッセージコンシューマのIDを取得します.
     *
     * @return the ID
     */
    String getId();

    /**
     * Closes the message consumer.
     *
     * メッセージコンシューマをクローズします.
     */
    void close();
}
