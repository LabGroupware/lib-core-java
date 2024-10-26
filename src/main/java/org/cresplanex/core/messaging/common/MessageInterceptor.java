package org.cresplanex.core.messaging.common;

/**
 * Interface for message interceptors.
 *
 * メッセージインターセプタのためのインターフェース.
 */
public interface MessageInterceptor {

    /**
     * Called before sending a message.
     *
     * メッセージを送信する前に呼び出されます.
     *
     * @param message the message to send
     */
    default void preSend(Message message) {
    }

    /**
     * Called after sending a message.
     *
     * メッセージを送信した後に呼び出されます.
     *
     * @param message the message to send
     * @param e the exception that occurred during sending
     */
    default void postSend(Message message, Exception e) {
    }

    /**
     * Called before receiving a message.
     *
     * メッセージを受信する前に呼び出されます.
     *
     * @param message the message to receive
     */
    default void preReceive(Message message) {
    }

    /**
     * Called before handling a message.
     *
     * メッセージを処理する前に呼び出されます.
     *
     * @param subscriberId the ID of the subscriber
     * @param message the message to receive
     */
    default void preHandle(String subscriberId, Message message) {
    }

    /**
     * Called after handling a message.
     *
     * メッセージを処理した後に呼び出されます.
     *
     * @param subscriberId the ID of the subscriber
     * @param message the message to receive
     * @param throwable the exception that occurred during handling
     */
    default void postHandle(String subscriberId, Message message, Throwable throwable) {
    }

    /**
     * Called after receiving a message.
     *
     * メッセージを受信した後に呼び出されます.
     *
     * @param message the message to receive
     */
    default void postReceive(Message message) {
    }

}
