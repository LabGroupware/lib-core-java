package org.cresplanex.core.events.subscriber;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.messaging.common.Message;

public class DomainEventEnvelopeImpl<T extends DomainEvent> implements DomainEventEnvelope<T> {

  private final Message message;
  private final String aggregateType;
  private final String aggregateId;
  private final String eventId;
  private final T event;

  public DomainEventEnvelopeImpl(Message message, String aggregateType, String aggregateId, String eventId, T event) {
    this.message = message;
    this.aggregateType = aggregateType;
    this.aggregateId = aggregateId;
    this.eventId = eventId;
    this.event = event;
  }

  @Override
  public String getAggregateId() {
    return aggregateId;
  }

  @Override
  public Message getMessage() {
    return message;
  }

  @Override
  public T getEvent() {
    return event;
  }


  @Override
  public String getAggregateType() {
    return aggregateType;
  }

  @Override
  public String getEventId() {
    return eventId;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
