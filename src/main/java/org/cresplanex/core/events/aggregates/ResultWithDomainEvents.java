package org.cresplanex.core.events.aggregates;

import java.util.Arrays;
import java.util.List;

import org.cresplanex.core.events.common.DomainEvent;

public class ResultWithDomainEvents<A, E extends DomainEvent> {

    public final A result;
    public final List<E> events;

    public ResultWithDomainEvents(A result, List<E> events) {
        this.result = result;
        this.events = events;
    }

    @SafeVarargs
    public ResultWithDomainEvents(A result, E... events) {
        this.result = result;
        this.events = Arrays.asList(events);
    }
}
