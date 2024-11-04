package org.cresplanex.core.events.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventNameMappingDefaultConfiguration {

    @Bean("org.cresplanex.core.events.common.DomainEventNameMapping")
    public DomainEventNameMapping domainEventNameMapping() {
        return new DefaultDomainEventNameMapping();
    }
}
