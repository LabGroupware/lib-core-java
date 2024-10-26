package org.cresplanex.core.saga.simpledsl;

import io.eventuate.tram.messaging.common.Message;

public interface ISagaStep<Data> {
    boolean isSuccessfulReply(boolean compensating, Message message);

    boolean hasAction(Data data);

    boolean hasCompensation(Data data);
}
