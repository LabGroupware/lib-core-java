package org.cresplanex.core.common.kafka.multi;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import org.cresplanex.core.messaging.kafka.common.sbe.MultiMessageEncoder;

/**
 * Kafkaのマルチメッセージを表すクラス。
 * <p>
 * メッセージ本体に加え、複数のヘッダーを保持します。
 * </p>
 */
public class CoreKafkaMultiMessage extends KeyValue {

    /** メッセージに関連するヘッダーのリスト */
    private List<CoreKafkaMultiMessageHeader> headers;

    /**
     * 指定されたキーと値でインスタンスを初期化し、空のヘッダーリストを設定します。
     *
     * @param key メッセージのキー
     * @param value メッセージの値
     */
    public CoreKafkaMultiMessage(String key, String value) {
        this(key, value, Collections.emptyList());
    }

    /**
     * 指定されたキー、値、およびヘッダーリストでインスタンスを初期化します。
     *
     * @param key メッセージのキー
     * @param value メッセージの値
     * @param headers メッセージのヘッダーリスト
     */
    public CoreKafkaMultiMessage(String key, String value, List<CoreKafkaMultiMessageHeader> headers) {
        super(key, value);
        this.headers = headers;
    }

    /**
     * メッセージに関連するヘッダーのリストを取得します。
     *
     * @return ヘッダーのリスト
     */
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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * メッセージのサイズを推定します。
     * <p>
     * 基本メッセージのサイズに、ヘッダーのサイズを加算して計算します。
     * </p>
     *
     * @return メッセージの推定サイズ（バイト単位）
     */
    @Override
    public int estimateSize() {
        int headerSize = MultiMessageEncoder.MessagesEncoder.HeadersEncoder.HEADER_SIZE;
        int messagesSize = KeyValue.estimateSize(headers);
        return super.estimateSize() + headerSize + messagesSize;
    }
}
