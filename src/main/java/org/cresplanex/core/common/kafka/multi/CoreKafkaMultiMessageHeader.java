package org.cresplanex.core.common.kafka.multi;

/**
 * Kafkaのマルチメッセージに使用されるヘッダーを表すクラス。
 * <p>
 * キーと値のペアとして、メッセージの付加情報を保持します。
 * </p>
 */
public class CoreKafkaMultiMessageHeader extends KeyValue {

    /**
     * 指定されたキーと値でヘッダーを初期化します。
     *
     * @param key ヘッダーのキー
     * @param value ヘッダーの値
     */
    public CoreKafkaMultiMessageHeader(String key, String value) {
        super(key, value);
    }
}
