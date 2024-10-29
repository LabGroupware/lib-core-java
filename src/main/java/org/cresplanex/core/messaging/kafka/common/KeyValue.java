package org.cresplanex.core.messaging.kafka.common;

import java.util.Collection;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * キーと値のペアを表すクラス。
 * <p>
 * キーと値のサイズを見積もり、キーと値のコレクションのサイズを推定します。
 * </p>
 */
public class KeyValue {

    /** 文字ごとの推定バイト数 */
    public static final int ESTIMATED_BYTES_PER_CHAR = 3;

    /** キーヘッダーのサイズ（バイト） */
    public static final int KEY_HEADER_SIZE = 4;

    /** 値ヘッダーのサイズ（バイト） */
    public static final int VALUE_HEADER_SIZE = 4;

    /** キー */
    private final String key;

    /** 値 */
    private final String value;

    /**
     * 指定されたキーと値でインスタンスを初期化します。
     *
     * @param key キー
     * @param value 値
     */
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * キーを取得します。
     *
     * @return キー
     */
    public String getKey() {
        return key;
    }

    /**
     * 値を取得します。
     *
     * @return 値
     */
    public String getValue() {
        return value;
    }

    /**
     * キーと値のサイズを見積もります。
     *
     * @return キーと値の推定サイズ（バイト単位）
     */
    public int estimateSize() {
        int keyLength = estimatedStringSizeInBytes(key);
        int valueLength = estimatedStringSizeInBytes(value);
        return KEY_HEADER_SIZE + keyLength + VALUE_HEADER_SIZE + valueLength;
    }

    /**
     * キーと値のコレクションの合計サイズを見積もります。
     *
     * @param kvs キーと値のコレクション
     * @return コレクション内のキーと値の合計サイズ
     */
    public static int estimateSize(Collection<? extends KeyValue> kvs) {
        return kvs.stream().mapToInt(KeyValue::estimateSize).sum();
    }

    /**
     * 指定された文字列の推定バイトサイズを計算します。
     *
     * @param s 推定サイズを計算する文字列
     * @return 文字列の推定サイズ（バイト単位）
     */
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

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
