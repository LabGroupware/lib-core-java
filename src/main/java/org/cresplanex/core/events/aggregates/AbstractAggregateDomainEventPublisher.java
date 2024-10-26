package org.cresplanex.core.events.aggregates;

import java.util.List;
import java.util.function.Function;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.publisher.DomainEventPublisher;

public abstract class AbstractAggregateDomainEventPublisher<A, E extends DomainEvent> {

    private final Function<A, Object> idSupplier;
    private final DomainEventPublisher eventPublisher;
    private final Class<A> aggregateType;

    protected AbstractAggregateDomainEventPublisher(DomainEventPublisher eventPublisher,
            Class<A> aggregateType,
            Function<A, Object> idSupplier) {
        this.eventPublisher = eventPublisher;
        this.aggregateType = aggregateType;
        this.idSupplier = idSupplier;
    }

    public Class<A> getAggregateType() {
        return aggregateType;
    }

    @SuppressWarnings("unchecked")
    public void publish(A aggregate, List<E> events) {
        eventPublisher.publish(aggregateType, idSupplier.apply(aggregate), (List<DomainEvent>) events);
    }

}
