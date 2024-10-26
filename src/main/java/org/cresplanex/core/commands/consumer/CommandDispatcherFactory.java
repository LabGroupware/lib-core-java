package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

public class CommandDispatcherFactory {

    private final MessageConsumer messageConsumer;
    private final CommandNameMapping commandNameMapping;
    private final CommandReplyProducer commandReplyProducer;

    public CommandDispatcherFactory(MessageConsumer messageConsumer,
            CommandNameMapping commandNameMapping, CommandReplyProducer commandReplyProducer) {
        this.messageConsumer = messageConsumer;
        this.commandNameMapping = commandNameMapping;
        this.commandReplyProducer = commandReplyProducer;
    }

    public CommandDispatcher make(String commandDispatcherId,
            CommandHandlers commandHandlers) {
        CommandDispatcher commandDispatcher = new CommandDispatcher(commandDispatcherId, commandHandlers, messageConsumer, commandNameMapping, commandReplyProducer);
        commandDispatcher.initialize();
        return commandDispatcher;
    }
}
