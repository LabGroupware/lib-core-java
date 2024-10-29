package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.Properties;

/**
 * Kafkaメッセージコンシューマのデフォルトファクトリークラス。
 * <p>
 * このクラスは、指定されたプロパティを使用してデフォルトのKafkaメッセージコンシューマを生成します。
 * </p>
 */
public class DefaultKafkaConsumerFactory implements KafkaConsumerFactory {

    /**
     * 指定されたプロパティを使用してKafkaメッセージコンシューマを生成します。
     *
     * @param subscriptionId サブスクリプションID
     * @param consumerProperties コンシューマのプロパティ
     * @return Kafkaメッセージコンシューマ
     */
    @Override
    public KafkaMessageConsumer makeConsumer(String subscriptionId, Properties consumerProperties) {
        return DefaultKafkaMessageConsumer.create(consumerProperties);
    }
}
