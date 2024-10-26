package org.cresplanex.core.messaging.kafka.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreKafkaPropertiesConfiguration {

    @Bean
    public CoreKafkaConfigurationProperties coreKafkaConfigurationProperties(@Value("${corelocal.kafka.bootstrap.servers}") String bootstrapServers,
            @Value("${corelocal.kafka.connection.validation.timeout:#{1000}}") long connectionValidationTimeout) {
        return new CoreKafkaConfigurationProperties(bootstrapServers, connectionValidationTimeout);
    }
}
