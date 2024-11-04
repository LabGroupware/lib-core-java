package org.cresplanex.core.events.subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;

public class DomainEventHandlersBuilder {

    private String aggregateType;
    private final List<DomainEventHandler> handlers = new ArrayList<>();

    public DomainEventHandlersBuilder(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public static DomainEventHandlersBuilder forAggregateType(String aggregateType) {
        return new DomainEventHandlersBuilder(aggregateType);
    }

    @SuppressWarnings("unchecked")
    public <E extends DomainEvent> DomainEventHandlersBuilder onEvent(Class<E> eventClass, Consumer<DomainEventEnvelope<E>> handler) {
        handlers.add(new DomainEventHandler(
                aggregateType,
                (Class<DomainEvent>) eventClass,
                (e) -> handler.accept((DomainEventEnvelope<E>) e)
        ));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <E extends DomainEvent> DomainEventHandlersBuilder onEvent(Class<E> eventClass, Consumer<DomainEventEnvelope<E>> handler, String eventTypeName) {
        handlers.add(new DomainEventHandler(
                aggregateType,
                (Class<DomainEvent>) eventClass,
                (e) -> handler.accept((DomainEventEnvelope<E>) e),
                eventTypeName
        ));
        return this;
    }

    public DomainEventHandlersBuilder andForAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
        return this;
    }

    public DomainEventHandlers build() {
        return new DomainEventHandlers(handlers);
    }
}
