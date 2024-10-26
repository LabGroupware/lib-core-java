package org.cresplanex.core.events.autoconfigure;

import org.cresplanex.core.events.subscriber.EventSubscriberConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnClass(EventSubscriberConfiguration.class)
@Import(EventSubscriberConfiguration.class)
public class EventsSubscriberAutoConfiguration {
}
