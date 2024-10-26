package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.subscriber.DomainEventEnvelope;

public interface EventStartingHandler<Data, EventClass extends DomainEvent> {

    void apply(Data data, DomainEventEnvelope<EventClass> event);
}
