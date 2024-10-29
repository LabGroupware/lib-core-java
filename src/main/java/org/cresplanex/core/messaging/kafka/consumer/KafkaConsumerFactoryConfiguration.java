package org.cresplanex.core.messaging.kafka.consumer;

import org.cresplanex.core.messaging.kafka.basic.consumer.DefaultKafkaConsumerFactory;
import org.cresplanex.core.messaging.kafka.basic.consumer.KafkaConsumerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafkaコンシューマーファクトリーの設定クラス。
 * <p>
 * KafkaConsumerFactoryのBeanが定義されていない場合に、DefaultKafkaConsumerFactoryを使用するBeanを提供します。</p>
 */
@Configuration
public class KafkaConsumerFactoryConfiguration {

    /**
     * Kafkaコンシューマーファクトリーを提供するBean。
     * <p>
     * KafkaConsumerFactoryのBeanがすでに定義されていない場合に限り、DefaultKafkaConsumerFactoryを返します。</p>
     *
     * @return Kafkaコンシューマーファクトリーのインスタンス
     */
    @Bean
    @ConditionalOnMissingBean
    public KafkaConsumerFactory kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory();
    }
}
