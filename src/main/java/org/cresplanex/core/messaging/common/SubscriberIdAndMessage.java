package org.cresplanex.core.messaging.common;

/**
 * Represents a subscriber ID and a message.
 *
 * 購読者IDとメッセージを表す.
 */
public class SubscriberIdAndMessage {

    private final String subscriberId;
    private final Message message;

    public SubscriberIdAndMessage(String subscriberId, Message message) {
        this.subscriberId = subscriberId;
        this.message = message;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public Message getMessage() {
        return message;
    }
}
