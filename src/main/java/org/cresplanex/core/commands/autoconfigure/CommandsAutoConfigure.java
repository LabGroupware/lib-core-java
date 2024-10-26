package org.cresplanex.core.commands.autoconfigure;

import org.cresplanex.core.commands.consumer.CommandConsumerConfiguration;
import org.cresplanex.core.commands.producer.CommandProducerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(CommandConsumerConfiguration.class)
@Import({CommandConsumerConfiguration.class, CommandProducerConfiguration.class})
public class CommandsAutoConfigure {
}
