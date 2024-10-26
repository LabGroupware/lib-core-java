package org.cresplanex.core.saga.simpledsl;

import io.eventuate.tram.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.CommandWithDestinationAndType;


public interface ParticipantInvocation<Data> {

  boolean isSuccessfulReply(Message message);

  boolean isInvocable(Data data);

  CommandWithDestinationAndType makeCommandToSend(Data data);
}
