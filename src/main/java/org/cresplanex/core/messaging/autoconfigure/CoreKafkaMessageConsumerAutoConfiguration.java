package org.cresplanex.core.messaging.autoconfigure;

import org.cresplanex.core.consumer.common.MessageConsumerImplementation;
import org.cresplanex.core.consumer.kafka.CoreKafkaMessageConsumerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(CoreKafkaMessageConsumerConfiguration.class)
@ConditionalOnMissingBean(MessageConsumerImplementation.class)
@Import(CoreKafkaMessageConsumerConfiguration.class)
public class CoreKafkaMessageConsumerAutoConfiguration {
}
