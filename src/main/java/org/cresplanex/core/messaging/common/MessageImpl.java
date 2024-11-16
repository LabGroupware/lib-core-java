package org.cresplanex.core.messaging.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A message that can be sent.
 *
 * 送信可能なメッセージ.
 */
public class MessageImpl implements Message {

    private static final Logger log = LoggerFactory.getLogger(MessageImpl.class);
    private String payload;
    private Map<String, String> headers;
    private boolean throwException;

    public MessageImpl() {
    }

    public MessageImpl(String payload, Map<String, String> headers, boolean throwException) {
        this.payload = payload;
        this.headers = headers;
        this.throwException = throwException;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public Optional<String> getHeader(String name) {
        return Optional.ofNullable(headers.get(name));
    }

    @Override
    public String getRequiredHeader(String name) {
        String s = headers.get(name);
        if (s == null) {
            throw new RuntimeException("No such header: " + name + " in this message " + this); 
        }else {
            return s;
        }
    }

    @Override
    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    @Override
    public String getId() {
        return getRequiredHeader(Message.ID);
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void setHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(name, value);
    }

    @Override
    public void removeHeader(String key) {
        headers.remove(key);
    }

    @Override
    public boolean isThrowException() {
        return throwException;
    }
}
