package org.cresplanex.core.events.autoconfigure;

import org.cresplanex.core.events.publisher.EventsPublisherConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(EventsPublisherConfiguration.class)
@Import(EventsPublisherConfiguration.class)
public class EventsPublisherAutoConfiguration {
}
