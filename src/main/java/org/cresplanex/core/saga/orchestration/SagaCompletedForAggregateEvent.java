package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.events.common.DomainEvent;

public class SagaCompletedForAggregateEvent implements DomainEvent {

    public SagaCompletedForAggregateEvent(String sagaId) {
    }
}
