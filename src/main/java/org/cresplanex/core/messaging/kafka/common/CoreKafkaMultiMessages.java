package org.cresplanex.core.messaging.kafka.common;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CoreKafkaMultiMessages {

    private List<CoreKafkaMultiMessagesHeader> headers;
    private List<CoreKafkaMultiMessage> messages;

    public CoreKafkaMultiMessages(List<CoreKafkaMultiMessage> messages) {
        this(Collections.emptyList(), messages);
    }

    public CoreKafkaMultiMessages(List<CoreKafkaMultiMessagesHeader> headers, List<CoreKafkaMultiMessage> messages) {
        this.headers = headers;
        this.messages = messages;
    }

    public List<CoreKafkaMultiMessagesHeader> getHeaders() {
        return headers;
    }

    public List<CoreKafkaMultiMessage> getMessages() {
        return messages;
    }

    public int estimateSize() {
        return KeyValue.estimateSize(headers) + KeyValue.estimateSize(messages);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CoreKafkaMultiMessages that = (CoreKafkaMultiMessages) o;
        return EqualsBuilder.reflectionEquals(this, that);
    }
    // @Override
    // public boolean equals(Object o) {
    //     return EqualsBuilder.reflectionEquals(this, o);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(headers, messages);
    }
}
