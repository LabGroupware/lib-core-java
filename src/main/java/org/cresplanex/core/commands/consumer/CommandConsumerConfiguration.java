package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CommandReplyProducerConfiguration.class)
public class CommandConsumerConfiguration {

    @Bean
    public CommandDispatcherFactory commandDispatcherFactory(MessageConsumer messageConsumer, CommandNameMapping commandNameMapping, CommandReplyProducer commandReplyProducer) {
        return new CommandDispatcherFactory(messageConsumer, commandNameMapping, commandReplyProducer);
    }

}
