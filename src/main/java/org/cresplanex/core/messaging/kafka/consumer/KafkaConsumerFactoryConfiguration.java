package org.cresplanex.core.messaging.kafka.consumer;

import org.cresplanex.core.messaging.kafka.basic.consumer.DefaultKafkaConsumerFactory;
import org.cresplanex.core.messaging.kafka.basic.consumer.KafkaConsumerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConsumerFactoryConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public KafkaConsumerFactory kafkaConsumerFactory() {
        return new DefaultKafkaConsumerFactory();
    }
}
