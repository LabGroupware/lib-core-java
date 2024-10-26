package org.cresplanex.core.consumer.common;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.messaging.consumer.SubscriberMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ConsumerBaseCommonConfiguration.class)
public class ConsumerCommonConfiguration {

    @Bean
    public MessageConsumer messageConsumer(MessageConsumerImplementation messageConsumerImplementation,
            ChannelMapping channelMapping,
            DecoratedMessageHandlerFactory decoratedMessageHandlerFactory, SubscriberMapping subscriberMapping) {
        return new MessageConsumerImpl(channelMapping, messageConsumerImplementation, decoratedMessageHandlerFactory, subscriberMapping);
    }
}
