package org.cresplanex.core.events.subscriber;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.messaging.common.Message;

public interface DomainEventEnvelope<T extends DomainEvent> {
  String getAggregateId();
  Message getMessage();
  String getAggregateType();
  String getEventId();

  T getEvent();
}
