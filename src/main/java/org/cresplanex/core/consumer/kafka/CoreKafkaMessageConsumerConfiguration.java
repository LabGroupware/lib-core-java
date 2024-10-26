package org.cresplanex.core.consumer.kafka;

import org.cresplanex.core.consumer.common.ConsumerCommonConfiguration;
import org.cresplanex.core.consumer.common.MessageConsumerImplementation;
import org.cresplanex.core.messaging.kafka.consumer.KafkaConsumerFactoryConfiguration;
import org.cresplanex.core.messaging.kafka.consumer.MessageConsumerKafkaConfiguration;
import org.cresplanex.core.messaging.kafka.consumer.MessageConsumerKafkaImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ConsumerCommonConfiguration.class, MessageConsumerKafkaConfiguration.class, KafkaConsumerFactoryConfiguration.class})
public class CoreKafkaMessageConsumerConfiguration {

    @Bean
    public MessageConsumerImplementation messageConsumerImplementation(MessageConsumerKafkaImpl messageConsumerKafka) {
        return new CoreKafkaMessageConsumer(messageConsumerKafka);
    }
}
