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

/**
 * SagaCommandHandlersBuilderは、サガ内で使用されるコマンドハンドラのリストを構築するためのビルダークラスです。
 * 各メソッドを使用して、特定のコマンドクラスに対するハンドラを追加できます。
 */
public class SagaCommandHandlersBuilder implements AbstractSagaCommandHandlersBuilder {

    private String channel;
    private final List<CommandHandler> handlers = new ArrayList<>();

    /**
     * 指定されたチャンネルから新しいSagaCommandHandlersBuilderを開始します。
     *
     * @param channel コマンドが送信されるチャンネル
     * @return SagaCommandHandlersBuilderのインスタンス
     */
    public static SagaCommandHandlersBuilder fromChannel(String channel) {
        return new SagaCommandHandlersBuilder().andFromChannel(channel);
    }

    /**
     * チャンネルを設定し、ビルダーのインスタンスを返します。
     *
     * @param channel コマンドが送信されるチャンネル
     * @return このビルダーインスタンス
     */
    private SagaCommandHandlersBuilder andFromChannel(String channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningMessages(Class<C> commandClass, String commandType, Function<CommandMessage<C>, List<Message>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, commandType, args -> handler.apply(args.getCommandMessage()));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningOptionalMessage(Class<C> commandClass, String commandType, Function<CommandMessage<C>, Optional<Message>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, commandType, args -> handler.apply(args.getCommandMessage()).map(Collections::singletonList).orElse(Collections.emptyList()));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, String commandType, Function<CommandMessage<C>, Message> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, commandType,
                args -> Collections.singletonList(handler.apply(args.getCommandMessage())));
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    @Override
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, String commandType, Consumer<CommandMessage<C>> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, commandType,
                args -> {
                    handler.accept(args.getCommandMessage());
                    return Collections.emptyList();
                });
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    /**
     * BiConsumer型のハンドラを使用してコマンドメッセージを処理するメソッドを追加します。
     *
     * @param <C> コマンドの型
     * @param commandClass コマンドクラスの型
     * @param handler コマンドメッセージとコマンド返信トークンを処理する関数
     * @return SagaCommandHandlerBuilderのインスタンス
     */
    public <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, String commandType, BiConsumer<CommandMessage<C>, CommandReplyToken> handler) {
        SagaCommandHandler h = new SagaCommandHandler(channel, commandClass, commandType,
                args -> {
                    handler.accept(args.getCommandMessage(), args.getCommandReplyToken());
                    return Collections.emptyList();
                });
        this.handlers.add(h);
        return new SagaCommandHandlerBuilder<>(this, h);
    }

    /**
     * 追加されたすべてのハンドラを含むCommandHandlersインスタンスを生成します。
     *
     * @return CommandHandlersインスタンス
     */
    public CommandHandlers build() {
        return new CommandHandlers(handlers);
    }

}
