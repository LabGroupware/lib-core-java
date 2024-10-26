package org.cresplanex.core.messaging.kafka.common;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

// import org.cresplanex.core.messaging.kafka.common.sbe.MultiMessageDecoder;
import org.cresplanex.core.messaging.kafka.common.sbe.MultiMessageEncoder;
// import org.cresplanex.core.messaging.kafka.common.sbe.MessageHeaderDecoder;
// import org.cresplanex.core.messaging.kafka.common.sbe.MessageHeaderEncoder;
// import java.util.Objects;

public class CoreKafkaMultiMessage extends KeyValue {

    private List<CoreKafkaMultiMessageHeader> headers;

    public CoreKafkaMultiMessage(String key, String value) {
        this(key, value, Collections.emptyList());
    }

    public CoreKafkaMultiMessage(String key, String value, List<CoreKafkaMultiMessageHeader> headers) {
        super(key, value);
        this.headers = headers;
    }

    public List<CoreKafkaMultiMessageHeader> getHeaders() {
        return headers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, o);
    }
    // @Override
    // public boolean equals(Object o) {
    //     return EqualsBuilder.reflectionEquals(this, o);
    // }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public int estimateSize() {
        int headerSize = MultiMessageEncoder.MessagesEncoder.HeadersEncoder.HEADER_SIZE;
        int messagesSize = KeyValue.estimateSize(headers);
        return super.estimateSize() + headerSize + messagesSize;
    }

}
