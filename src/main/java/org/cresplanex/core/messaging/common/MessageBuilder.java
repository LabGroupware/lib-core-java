package org.cresplanex.core.messaging.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A builder for creating messages.
 *
 * メッセージを作成するためのビルダー. これを使ってメッセージを作成することで、メッセージの作成を簡単に行うことができます.
 */
public class MessageBuilder {

    private static final Logger log = LoggerFactory.getLogger(MessageBuilder.class);
    protected String body; // payload
    protected Map<String, String> headers = new HashMap<>();
    protected boolean throwException = false;

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

    public MessageBuilder withThrowException(boolean throwException) {
        this.throwException = throwException;
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
        return new MessageImpl(body, headers, throwException);
    }

    public static MessageBuilder withMessage(Message message) {
        return new MessageBuilder(message);
    }
}
