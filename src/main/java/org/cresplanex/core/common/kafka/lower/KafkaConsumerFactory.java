package org.cresplanex.core.common.kafka.lower;

import java.util.Properties;

/**
 * Kafkaコンシューマのファクトリーインターフェース。
 * <p>
 * このインターフェースは、Kafkaメッセージコンシューマを生成するためのメソッドを提供します。
 * </p>
 */
public interface KafkaConsumerFactory {

    /**
     * 指定されたプロパティを使用してKafkaメッセージコンシューマを生成します。
     *
     * @param subscriptionId サブスクリプションID
     * @param consumerProperties コンシューマのプロパティ
     * @return Kafkaメッセージコンシューマ
     */
    KafkaMessageConsumer makeConsumer(String subscriptionId, Properties consumerProperties);
}
