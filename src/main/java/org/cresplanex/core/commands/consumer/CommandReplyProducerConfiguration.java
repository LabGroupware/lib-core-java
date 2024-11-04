package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.messaging.producer.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandReplyProducerConfiguration {
    @Bean("org.cresplanex.core.commands.consumer.CommandReplyProducer")
    public CommandReplyProducer commandReplyProducer(MessageProducer messageProducer) {
        return new CommandReplyProducer(messageProducer);
    }
}
