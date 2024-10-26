package org.cresplanex.core.saga.orchestration;

import java.util.List;

public interface SagaCommandProducer {

    String sendCommands(String sagaType, String sagaId, List<CommandWithDestinationAndType> commands, String sagaReplyChannel);
}
