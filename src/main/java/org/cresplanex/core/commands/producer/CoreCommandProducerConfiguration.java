package org.cresplanex.core.commands.producer;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.ChannelMappingDefaultConfiguration;
import org.cresplanex.core.messaging.producer.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ChannelMappingDefaultConfiguration.class})
public class CoreCommandProducerConfiguration {

    @Bean("org.cresplanex.core.commands.producer.CommandProducer")
    public CommandProducer commandProducer(MessageProducer messageProducer, ChannelMapping channelMapping, CommandNameMapping commandNameMapping) {
        return new CommandProducerImpl(messageProducer, commandNameMapping);
    }

}
