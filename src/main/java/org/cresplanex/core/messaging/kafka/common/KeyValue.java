package org.cresplanex.core.messaging.kafka.common;

import java.util.Collection;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class KeyValue {

    public static final int ESTIMATED_BYTES_PER_CHAR = 3;
    public static final int KEY_HEADER_SIZE = 4;
    public static final int VALUE_HEADER_SIZE = 4;

    private final String key;
    private final String value;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int estimateSize() {
        int keyLength = estimatedStringSizeInBytes(key);
        int valueLength = estimatedStringSizeInBytes(value);
        return KEY_HEADER_SIZE + keyLength + VALUE_HEADER_SIZE + valueLength;
    }

    public static int estimateSize(Collection<? extends KeyValue> kvs) {
        return kvs.stream().mapToInt(KeyValue::estimateSize).sum();
    }

    private int estimatedStringSizeInBytes(String s) {
        return s == null ? 0 : s.length() * ESTIMATED_BYTES_PER_CHAR;
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
        KeyValue keyValue = (KeyValue) o;
        return EqualsBuilder.reflectionEquals(this, keyValue);
    }
    // @Override
    // public boolean equals(Object o) {
    //     return EqualsBuilder.reflectionEquals(this, o);
    // }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
