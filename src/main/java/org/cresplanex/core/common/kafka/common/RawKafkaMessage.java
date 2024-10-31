package org.cresplanex.core.common.kafka.common;

/**
 * Kafkaメッセージのキーとペイロードを保持するためのクラスです。
 */
public class RawKafkaMessage {

    private final String messageKey;
    private final byte[] payload;

    /**
     * メッセージキーとペイロードでメッセージを初期化します。
     *
     * @param messageKey メッセージキー
     * @param payload メッセージのペイロード
     */
    public RawKafkaMessage(String messageKey, byte[] payload) {
        this.messageKey = messageKey;
        this.payload = payload;
    }

    /**
     * メッセージキーを取得します。
     *
     * @return メッセージキー
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * メッセージのペイロードを取得します。
     *
     * @return メッセージのペイロード
     */
    public byte[] getPayload() {
        return payload;
    }
}
