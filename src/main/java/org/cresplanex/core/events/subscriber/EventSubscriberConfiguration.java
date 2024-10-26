package org.cresplanex.core.events.subscriber;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSubscriberConfiguration {

    @Bean
    public DomainEventDispatcherFactory domainEventDispatcherFactory(MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
        return new DomainEventDispatcherFactory(messageConsumer, domainEventNameMapping);
    }
}
