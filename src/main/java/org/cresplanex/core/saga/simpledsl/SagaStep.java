package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.cresplanex.core.messaging.common.Message;

public interface SagaStep<Data> extends ISagaStep<Data> {

  Optional<BiConsumer<Data, Object>> getReplyHandler(Message message, boolean compensating);

  StepOutcome makeStepOutcome(Data data, boolean compensating);

}
