package org.cresplanex.core.messaging.common;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder for creating messages.
 *
 * メッセージを作成するためのビルダー. これを使ってメッセージを作成することで、メッセージの作成を簡単に行うことができます.
 */
public class MessageBuilder {

    protected String body; // payload
    protected Map<String, String> headers = new HashMap<>();

    protected MessageBuilder() {
    }

    public MessageBuilder(String body) {
        this.body = body;
    }

    public MessageBuilder(Message message) {
        this(message.getPayload());
        this.headers = message.getHeaders();
    }

    public static MessageBuilder withPayload(String payload) {
        return new MessageBuilder(payload);
    }

    public MessageBuilder withHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Adds the headers to the headers.
     *
     * 指定されたプレフィックスをヘッダーのキーに追加したものを, ヘッダーに追加します.
     *
     * @param headers the headers to add
     * @return this builder
     */
    public MessageBuilder withExtraHeaders(String prefix, Map<String, String> headers) {

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            this.headers.put(prefix + entry.getKey(), entry.getValue());
        }

        return this;
    }

    public Message build() {
        return new MessageImpl(body, headers);
    }

    public static MessageBuilder withMessage(Message message) {
        return new MessageBuilder(message);
    }
}
