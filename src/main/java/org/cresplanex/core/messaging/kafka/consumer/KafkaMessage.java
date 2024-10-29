package org.cresplanex.core.messaging.kafka.consumer;

/**
 * Kafkaメッセージを表すクラス。
 * <p>Kafkaメッセージのペイロード（内容）を保持します。</p>
 */
public class KafkaMessage {

    /**
     * メッセージの内容を表す文字列。
     */
    private final String payload;

    /**
     * KafkaMessageのコンストラクタ。
     * 
     * @param payload メッセージの内容
     */
    public KafkaMessage(String payload) {
        this.payload = payload;
    }

    /**
     * メッセージの内容を取得します。
     * 
     * @return メッセージの内容
     */
    public String getPayload() {
        return payload;
    }
}