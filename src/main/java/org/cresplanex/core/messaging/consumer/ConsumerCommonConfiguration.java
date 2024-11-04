package org.cresplanex.core.messaging.consumer;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.ChannelMappingDefaultConfiguration;
import org.cresplanex.core.messaging.consumer.decorator.BuiltInMessageHandlerDecoratorConfiguration;
import org.cresplanex.core.messaging.consumer.decorator.DecoratedMessageHandlerFactory;
import org.cresplanex.core.messaging.consumer.subscribermap.SubscriberMapping;
import org.cresplanex.core.messaging.consumer.subscribermap.SubscriberMappingDefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({BuiltInMessageHandlerDecoratorConfiguration.class, ChannelMappingDefaultConfiguration.class, SubscriberMappingDefaultConfiguration.class})
public class ConsumerCommonConfiguration {

    @Bean("org.cresplanex.core.messaging.consumer.MessageConsumer")
    public MessageConsumer messageConsumer(MessageConsumerImplementation messageConsumerImplementation,
            ChannelMapping channelMapping,
            DecoratedMessageHandlerFactory decoratedMessageHandlerFactory, SubscriberMapping subscriberMapping) {
        return new MessageConsumerImpl(channelMapping, messageConsumerImplementation, decoratedMessageHandlerFactory, subscriberMapping);
    }
}
