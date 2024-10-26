package org.cresplanex.core.messaging.kafka.basic.consumer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(CoreKafkaConsumerSpringConfigurationProperties.class)
public class CoreKafkaConsumerSpringConfigurationPropertiesConfiguration {

    @Bean
    public CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties(CoreKafkaConsumerSpringConfigurationProperties coreKafkaConsumerSpringConfigurationProperties) {
        CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties = new CoreKafkaConsumerConfigurationProperties(coreKafkaConsumerSpringConfigurationProperties.getProperties());
        coreKafkaConsumerConfigurationProperties.setBackPressure(coreKafkaConsumerSpringConfigurationProperties.getBackPressure());
        coreKafkaConsumerConfigurationProperties.setPollTimeout(coreKafkaConsumerSpringConfigurationProperties.getPollTimeout());
        return coreKafkaConsumerConfigurationProperties;

    }
}
