package org.cresplanex.core.commands.producer;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.producer.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandProducerConfiguration {

    @Bean
    public CommandProducer commandProducer(MessageProducer messageProducer, ChannelMapping channelMapping, CommandNameMapping commandNameMapping) {
        return new CommandProducerImpl(messageProducer, commandNameMapping);
    }

}
