package org.cresplanex.core.events.publisher;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.messaging.producer.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventsPublisherConfiguration {

    @Bean
    public DomainEventPublisher domainEventPublisher(MessageProducer messageProducer, DomainEventNameMapping domainEventNameMapping) {
        return new DomainEventPublisherImpl(messageProducer, domainEventNameMapping);
    }
}
