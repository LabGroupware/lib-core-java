package org.cresplanex.core.messaging.kafka.consumer;

import org.cresplanex.core.messaging.kafka.basic.consumer.CoreKafkaConsumerConfigurationProperties;
import org.cresplanex.core.messaging.kafka.basic.consumer.CoreKafkaConsumerSpringConfigurationPropertiesConfiguration;
import org.cresplanex.core.messaging.kafka.basic.consumer.KafkaConsumerFactory;
import org.cresplanex.core.messaging.kafka.common.CoreKafkaConfigurationProperties;
import org.cresplanex.core.messaging.kafka.common.CoreKafkaPropertiesConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreKafkaPropertiesConfiguration.class, CoreKafkaConsumerSpringConfigurationPropertiesConfiguration.class})
public class MessageConsumerKafkaConfiguration {

    @Autowired(required = false)
    private final TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping = new OriginalTopicPartitionToSwimlaneMapping();

    @Bean
    public MessageConsumerKafkaImpl messageConsumerKafka(CoreKafkaConfigurationProperties props,
            CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties,
            KafkaConsumerFactory kafkaConsumerFactory) {
        return new MessageConsumerKafkaImpl(props.getBootstrapServers(), coreKafkaConsumerConfigurationProperties, kafkaConsumerFactory, partitionToSwimLaneMapping);
    }
}
