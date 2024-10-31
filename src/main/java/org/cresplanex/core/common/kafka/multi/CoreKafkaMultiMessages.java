package org.cresplanex.core.common.kafka.multi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Kafkaの複数メッセージの集合を表すクラス。
 * <p>
 * メッセージとそれに関連するヘッダーを管理します。
 * </p>
 */
public class CoreKafkaMultiMessages {

    /** メッセージに関連するヘッダーのリスト */
    private List<CoreKafkaMultiMessagesHeader> headers;

    /** メッセージのリスト */
    private List<CoreKafkaMultiMessage> messages;

    /**
     * 指定されたメッセージリストでインスタンスを初期化し、空のヘッダーリストを設定します。
     *
     * @param messages メッセージのリスト
     */
    public CoreKafkaMultiMessages(List<CoreKafkaMultiMessage> messages) {
        this(Collections.emptyList(), messages);
    }

    /**
     * 指定されたヘッダーリストとメッセージリストでインスタンスを初期化します。
     *
     * @param headers ヘッダーのリスト
     * @param messages メッセージのリスト
     */
    public CoreKafkaMultiMessages(List<CoreKafkaMultiMessagesHeader> headers, List<CoreKafkaMultiMessage> messages) {
        this.headers = headers;
        this.messages = messages;
    }

    /**
     * ヘッダーのリストを取得します。
     *
     * @return ヘッダーのリスト
     */
    public List<CoreKafkaMultiMessagesHeader> getHeaders() {
        return headers;
    }

    /**
     * メッセージのリストを取得します。
     *
     * @return メッセージのリスト
     */
    public List<CoreKafkaMultiMessage> getMessages() {
        return messages;
    }

    /**
     * ヘッダーとメッセージを含むデータの推定サイズを取得します。
     *
     * @return 推定サイズ（バイト単位）
     */
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

    @Override
    public int hashCode() {
        return Objects.hash(headers, messages);
    }
}
