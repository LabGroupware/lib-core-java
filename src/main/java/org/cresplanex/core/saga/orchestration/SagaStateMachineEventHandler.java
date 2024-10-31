package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;

public interface SagaStateMachineEventHandler<Data, EventClass extends DomainEvent> {

    SagaActions<Data> apply(Data data, DomainEventEnvelope<EventClass> event);

}
