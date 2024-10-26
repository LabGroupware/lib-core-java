package org.cresplanex.core;

import org.cresplanex.core.consumer.kafka.CoreKafkaMessageConsumerConfiguration;
import org.cresplanex.core.messaging.producer.jdbc.MessageProducerJdbcConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({MessageProducerJdbcConfiguration.class,
    CoreKafkaMessageConsumerConfiguration.class})
public class JdbcKafkaConfiguration {
}
