package org.cresplanex.core.events.common;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingBean(DomainEventNameMapping.class)
public class EventsCommonAutoConfiguration {

    @Bean
    public DomainEventNameMapping domainEventNameMapping() {
        return new DefaultDomainEventNameMapping();
    }

}
