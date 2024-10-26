package org.cresplanex.core.messaging.consumer;

/**
 * Interface for mapping subscribers.
 * 
 * サブスクライバをマッピングするためのインターフェース.
 */
public interface SubscriberMapping {

    /**
     * Maps the subscriber ID to an external representation.
     * 
     * サブスクライバIDを外部表現にマッピングします.
     * 
     * @param subscriberId the subscriber ID
     * @return the external representation
     */
    String toExternal(String subscriberId);
}
