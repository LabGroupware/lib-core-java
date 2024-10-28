package org.cresplanex.core.saga.participant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandHandler;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.CommandReplyToken;
import org.cresplanex.core.messaging.common.Message;

public class SagaCommandHandlersBuilder implements AbstractSagaCommandHandlersBuilder {

    private String channel;

    private final List<CommandHandler> handlers = new ArrayList<>();

    public static SagaCommandHandlersBuilder fromChannel(String channel) {
        return new SagaCommandHandlersBuilder().andFromChannel(channel);
    }

    private SagaCommandHandlersBuilder andFromChannel(String channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningMessages(Class<C> commandClass,
            Function<CommandMessage<C>, List<Message>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, args -> handler.apply(args.getCommandMessage()));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningOptionalMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Optional<Message>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, args -> handler.apply(args.getCommandMessage()).map(Collections::singletonList).orElse(Collections.emptyList()));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Message> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass,
                args -> Collections.singletonList(handler.apply(args.getCommandMessage())));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, Consumer<CommandMessage<C>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass,
                args -> {
                    handler.accept(args.getCommandMessage());
                    return Collections.emptyList();
                });
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, BiConsumer<CommandMessage<C>, CommandReplyToken> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass,
                args -> {
                    handler.accept(args.getCommandMessage(), args.getCommandReplyToken());
                    return Collections.emptyList();
                });
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    public CommandHandlers build() {
        return new CommandHandlers(handlers);
    }

}
