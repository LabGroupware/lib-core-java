package org.cresplanex.core.messaging.autoconfigure;

import org.cresplanex.core.messaging.producer.jdbc.MessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(MessageProducerJdbcConfiguration.class)
@Import(MessageProducerJdbcConfiguration.class)
public class MessageProducerJdbcAutoConfiguration {
}
