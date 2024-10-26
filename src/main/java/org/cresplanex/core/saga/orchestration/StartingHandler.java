package org.cresplanex.core.saga.orchestration;

import java.util.function.Function;

@SuppressWarnings("rawtypes")
public interface StartingHandler<Data> extends Function<Data, SagaActions> {
}

// public interface StartingHandler<Data> extends Function<Data, SagaActions<Data>> {
// }
