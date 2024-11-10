package org.cresplanex.core.saga.participant;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.messaging.common.Message;

/**
 * AbstractSagaCommandHandlersBuilderインターフェースは、特定のコマンドメッセージを処理する
 * Sagaコマンドハンドラービルダーを作成するためのメソッドを提供します。
 */
public interface AbstractSagaCommandHandlersBuilder {

    /**
     * 複数のメッセージを返すコマンドメッセージハンドラーを登録します。
     *
     * @param <C> 処理対象のコマンドの型
     * @param commandClass コマンドのクラス型
     * @param commandType コマンドのタイプ
     * @param handler コマンドメッセージを受け取り、複数のメッセージを返すハンドラ関数
     * @return SagaCommandHandlerBuilder コマンドハンドラーを構築するビルダー
     */
    <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningMessages(Class<C> commandClass,
            String commandType, Function<CommandMessage<C>, List<Message>> handler);

    /**
     * Optional<Message>を返すコマンドメッセージハンドラーを登録します。
     *
     * @param <C> 処理対象のコマンドの型
     * @param commandClass コマンドのクラス型
     * @param commandType コマンドのタイプ
     * @param handler コマンドメッセージを受け取り、Optional<Message>を返すハンドラ関数
     * @return SagaCommandHandlerBuilder コマンドハンドラーを構築するビルダー
     */
    <C extends Command> SagaCommandHandlerBuilder<C> onMessageReturningOptionalMessage(Class<C> commandClass,
            String commandType, Function<CommandMessage<C>, Optional<Message>> handler);

    /**
     * 単一のメッセージを返すコマンドメッセージハンドラーを登録します。
     *
     * @param <C> 処理対象のコマンドの型
     * @param commandClass コマンドのクラス型
     * @param commandType コマンドのタイプ
     * @param handler コマンドメッセージを受け取り、単一のメッセージを返すハンドラ関数
     * @return SagaCommandHandlerBuilder コマンドハンドラーを構築するビルダー
     */
    <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, String commandType, Function<CommandMessage<C>, Message> handler);

    /**
     * 戻り値のないコマンドメッセージハンドラーを登録します。
     *
     * @param <C> 処理対象のコマンドの型
     * @param commandClass コマンドのクラス型
     * @param commandType コマンドのタイプ
     * @param handler コマンドメッセージを受け取り、処理を行うだけで戻り値を持たないハンドラ関数
     * @return SagaCommandHandlerBuilder コマンドハンドラーを構築するビルダー
     */
    <C extends Command> SagaCommandHandlerBuilder<C> onMessage(Class<C> commandClass, String commandType, Consumer<CommandMessage<C>> handler);
}
