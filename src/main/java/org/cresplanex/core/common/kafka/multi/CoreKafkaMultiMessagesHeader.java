package org.cresplanex.core.common.kafka.multi;

/**
 * Kafkaマルチメッセージのヘッダーを表すクラス。
 * <p>
 * キーと値のペアとして、マルチメッセージに付加情報を提供します。
 * </p>
 */
public class CoreKafkaMultiMessagesHeader extends KeyValue {

    /**
     * 指定されたキーと値でヘッダーを初期化します。
     *
     * @param key ヘッダーのキー
     * @param value ヘッダーの値
     */
    public CoreKafkaMultiMessagesHeader(String key, String value) {
        super(key, value);
    }
}
