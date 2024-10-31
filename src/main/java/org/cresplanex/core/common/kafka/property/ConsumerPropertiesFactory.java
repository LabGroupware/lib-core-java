package org.cresplanex.core.common.kafka.property;

import java.util.Properties;

/**
 * Kafkaコンシューマのプロパティを生成するファクトリークラス。
 * <p>
 * Kafkaの接続に必要な基本設定を提供します。
 * </p>
 */
public class ConsumerPropertiesFactory {

    /**
     * デフォルトのKafkaコンシューマ設定を生成します。
     *
     * @param bootstrapServers Kafkaのブートストラップサーバーのアドレス
     * @param subscriberId     サブスクライバーID
     * @return 生成されたプロパティオブジェクト
     */
    public static Properties makeDefaultConsumerProperties(String bootstrapServers, String subscriberId) {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", bootstrapServers);
        consumerProperties.put("group.id", subscriberId);
        consumerProperties.put("enable.auto.commit", "false");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        consumerProperties.put("auto.offset.reset", "earliest");
        return consumerProperties;
    }
}
