package org.cresplanex.core.messaging.producer;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingCommonProducerConfiguration {

    @Autowired(required = false)
    private final MessageInterceptor[] messageInterceptors = new MessageInterceptor[0];

    @Bean
    public MessageProducer messageProducer(ChannelMapping channelMapping, MessageProducerImplementation implementation) {
        return new MessageProducerImpl(messageInterceptors, channelMapping, implementation);
    }
}
