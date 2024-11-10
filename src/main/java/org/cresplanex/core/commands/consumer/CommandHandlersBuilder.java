package org.cresplanex.core.commands.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.messaging.common.Message;

/**
 * コマンドハンドラーを構築するためのビルダークラス。 チャネルやリソースの指定に応じてさまざまなタイプのコマンドハンドラーを設定できます。
 */
public class CommandHandlersBuilder {

    private String channel;
    private Optional<String> resource = Optional.empty();
    private final List<CommandHandler> handlers = new ArrayList<>();

    /**
     * 指定されたチャネルでビルダーの初期化を開始します。
     *
     * @param channel コマンドのチャネル名
     * @return 初期化された CommandHandlersBuilder インスタンス
     */
    public static CommandHandlersBuilder fromChannel(String channel) {
        return new CommandHandlersBuilder().andFromChannel(channel);
    }

    /**
     * チャネルを設定します。
     *
     * @param channel コマンドのチャネル名
     * @return このビルダーのインスタンス
     */
    private CommandHandlersBuilder andFromChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * リソースを設定します。
     *
     * @param resource リソースの名前
     * @return このビルダーのインスタンス
     */
    public CommandHandlersBuilder resource(String resource) {
        this.resource = Optional.of(resource);
        return this;
    }

    /**
     * 指定されたクラスのコマンドを受信し、メッセージのリストを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージとパス変数を受け取ってメッセージのリストを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessageReturningMessages(Class<C> commandClass,
            BiFunction<CommandMessage<C>, PathVariables, List<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, CommandHandlerArgs.makeFn(handler)));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessageReturningMessages(Class<C> commandClass,
            String commandType, BiFunction<CommandMessage<C>, PathVariables, List<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, CommandHandlerArgs.makeFn(handler), commandType));
        return this;
    }

    /**
     * 指定されたクラスのコマンドを受信し、オプションで単一のメッセージを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージとパス変数を受け取ってオプションでメッセージを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessageReturningOptionalMessage(Class<C> commandClass,
            BiFunction<CommandMessage<C>, PathVariables, Optional<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass,
                args -> handler.apply(args.getCommandMessage(), args.getPathVars()).map(Collections::singletonList).orElse(Collections.emptyList())));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessageReturningOptionalMessage(Class<C> commandClass,
            String commandType, BiFunction<CommandMessage<C>, PathVariables, Optional<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass,
                args -> handler.apply(args.getCommandMessage(), args.getPathVars()).map(Collections::singletonList).orElse(Collections.emptyList()), commandType));
        return this;
    }

    /**
     * 指定されたクラスのコマンドを受信し、単一のメッセージを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージとパス変数を受け取ってメッセージを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            BiFunction<CommandMessage<C>, PathVariables, Message> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> Collections.singletonList(handler.apply(args.getCommandMessage(), args.getPathVars()))));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            String commandType, BiFunction<CommandMessage<C>, PathVariables, Message> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> Collections.singletonList(handler.apply(args.getCommandMessage(), args.getPathVars())), commandType));
        return this;
    }

    /**
     * コマンドメッセージを受け取って複数のメッセージを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージを受け取りメッセージのリストを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessageReturningMessages(Class<C> commandClass,
            Function<CommandMessage<C>, List<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> handler.apply(args.getCommandMessage())));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessageReturningMessages(Class<C> commandClass,
            String commandType, Function<CommandMessage<C>, List<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> handler.apply(args.getCommandMessage()), commandType));
        return this;
    }

    /**
     * コマンドメッセージを受け取ってオプションでメッセージを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージを受け取りオプションでメッセージを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessageReturningOptionalMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Optional<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass,
                args -> handler.apply(args.getCommandMessage()).map(Collections::singletonList).orElse(Collections.emptyList())));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessageReturningOptionalMessage(Class<C> commandClass,
            String commandType, Function<CommandMessage<C>, Optional<Message>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass,
                args -> handler.apply(args.getCommandMessage()).map(Collections::singletonList).orElse(Collections.emptyList()), commandType));
        return this;
    }

    /**
     * コマンドメッセージを受け取って単一のメッセージを返すハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージを受け取りメッセージを返すハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            Function<CommandMessage<C>, Message> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> Collections.singletonList(handler.apply(args.getCommandMessage()))));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            String commandType, Function<CommandMessage<C>, Message> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> Collections.singletonList(handler.apply(args.getCommandMessage())), commandType));
        return this;
    }

    /**
     * コマンドメッセージを受け取り、コマンド返信トークンを利用して複雑な処理を行うハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージとコマンド返信トークンを使用して処理を行うハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onComplexMessage(Class<C> commandClass,
            BiConsumer<CommandMessage<C>, CommandReplyToken> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, makeFn(handler)));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onComplexMessage(Class<C> commandClass,
            String commandType, BiConsumer<CommandMessage<C>, CommandReplyToken> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, makeFn(handler), commandType));
        return this;
    }

    /**
     * コマンドメッセージを受け取って処理を行うハンドラを設定します。
     * 
     * コマンドメッセージを受け取って, 何も返さない処理を行うハンドラを設定します。
     *
     * @param <C> コマンドのクラス
     * @param commandClass コマンドクラスのタイプ
     * @param handler コマンドメッセージを受け取り処理を行うハンドラ
     * @return このビルダーのインスタンス
     */
    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            Consumer<CommandMessage<C>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> {
            handler.accept(args.getCommandMessage());
            return Collections.emptyList();
        }));
        return this;
    }

    public <C extends Command> CommandHandlersBuilder onMessage(Class<C> commandClass,
            String commandType, Consumer<CommandMessage<C>> handler) {
        this.handlers.add(new CommandHandler(channel, resource, commandClass, args -> {
            handler.accept(args.getCommandMessage());
            return Collections.emptyList();
        }, commandType));
        return this;
    }

    /**
     * コマンドメッセージとコマンド返信トークンを受け取り、メッセージリストを返す関数を作成します。
     *
     * @param <C> コマンドのクラス
     * @param handler コマンドメッセージと返信トークンを受け取るハンドラ
     * @return メッセージリストを返す関数
     */
    private <C extends Command> Function<CommandHandlerArgs<C>, List<Message>> makeFn(BiConsumer<CommandMessage<C>, CommandReplyToken> handler) {
        return args -> {
            handler.accept(args.getCommandMessage(), args.getCommandReplyToken());
            return Collections.emptyList();
        };
    }

    /**
     * 設定されたすべてのコマンドハンドラを組み立てて、CommandHandlersインスタンスを作成します。
     *
     * @return 組み立てられたCommandHandlersインスタンス
     */
    public CommandHandlers build() {
        return new CommandHandlers(handlers);
    }
}
