package org.cresplanex.core.saga.participant;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.common.SagaLockManager;

public class SagaCommandDispatcherFactory {

    private final MessageConsumer messageConsumer;
    private final SagaLockManager sagaLockManager;
    private final CommandNameMapping commandNameMapping;
    private final CommandReplyProducer commandReplyProducer;

    public SagaCommandDispatcherFactory(MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            CommandNameMapping commandNameMapping,
            CommandReplyProducer commandReplyProducer) {
        this.messageConsumer = messageConsumer;
        this.sagaLockManager = sagaLockManager;
        this.commandNameMapping = commandNameMapping;
        this.commandReplyProducer = commandReplyProducer;
    }

    public SagaCommandDispatcher make(String commandDispatcherId, CommandHandlers target) {
        SagaCommandDispatcher sagaCommandDispatcher = new SagaCommandDispatcher(commandDispatcherId, target, messageConsumer, sagaLockManager, commandNameMapping, commandReplyProducer);
        sagaCommandDispatcher.initialize();
        return sagaCommandDispatcher;
    }
}
