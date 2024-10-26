package org.cresplanex.core.events.subscriber;

import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

public class DomainEventDispatcherFactory {

  protected MessageConsumer messageConsumer;
  protected DomainEventNameMapping domainEventNameMapping;

  public DomainEventDispatcherFactory(MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
    this.messageConsumer = messageConsumer;
    this.domainEventNameMapping = domainEventNameMapping;
  }

  public DomainEventDispatcher make(String eventDispatcherId, DomainEventHandlers domainEventHandlers) {
    DomainEventDispatcher domainEventDispatcher = new DomainEventDispatcher(eventDispatcherId, domainEventHandlers, messageConsumer, domainEventNameMapping);
    domainEventDispatcher.initialize();
    return domainEventDispatcher;
  }
}
