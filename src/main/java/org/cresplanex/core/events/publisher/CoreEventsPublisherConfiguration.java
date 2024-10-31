package org.cresplanex.core.events.publisher;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.events.common.EventNameMappingDefaultConfiguration;
import org.cresplanex.core.messaging.producer.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EventNameMappingDefaultConfiguration.class})
public class CoreEventsPublisherConfiguration {

    @Bean
    public DomainEventPublisher domainEventPublisher(MessageProducer messageProducer, DomainEventNameMapping domainEventNameMapping) {
        return new DomainEventPublisherImpl(messageProducer, domainEventNameMapping);
    }
}
