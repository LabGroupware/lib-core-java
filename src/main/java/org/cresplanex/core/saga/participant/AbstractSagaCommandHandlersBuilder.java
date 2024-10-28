package org.cresplanex.core.saga.participant;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.messaging.common.Message;

public interface AbstractSagaCommandHandlersBuilder {

    <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningMessages(Class<C> commandClass,
            Function<CommandMessage<C>, List<Message>> handler);

    <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningOptionalMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Optional<Message>> handler);

    <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Message> handler);

    <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, Consumer<CommandMessage<C>> handler);
}

