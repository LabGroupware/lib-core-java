package org.cresplanex.core.messaging.consumer;

/**
 * Interface for message subscriptions.
 *
 * メッセージのサブスクリプションのためのインターフェース.
 */
public interface MessageSubscription {

    /**
     * Unsubscribes from the message source.
     *
     * メッセージソースから購読解除します.
     */
    void unsubscribe();
}
