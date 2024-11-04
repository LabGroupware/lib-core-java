package org.cresplanex.core.events.subscriber;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.events.common.EventNameMappingDefaultConfiguration;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EventNameMappingDefaultConfiguration.class})
public class CoreEventSubscriberConfiguration {

    @Bean("org.cresplanex.core.events.subscriber.DomainEventDispatcherFactory")
    public DomainEventDispatcherFactory domainEventDispatcherFactory(MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
        return new DomainEventDispatcherFactory(messageConsumer, domainEventNameMapping);
    }
}
