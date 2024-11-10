package org.cresplanex.core.saga.participant;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandHandlers;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.lock.LockTarget;

/**
 * SagaCommandHandlerBuilderは、サガ内で使用されるコマンドハンドラを構築するためのビルダークラスです。
 * ハンドラーの事前ロックおよび事後ロック設定をサポートします。
 *
 * @param <C> コマンドの型
 */
public class SagaCommandHandlerBuilder<C extends Command> implements AbstractSagaCommandHandlersBuilder {

    private final SagaCommandHandlersBuilder parent;
    private final SagaCommandHandler h;

    /**
     * SagaCommandHandlerBuilderのコンストラクタ。
     *
     * @param parent 親ビルダー
     * @param h 構築するサガコマンドハンドラ
     */
    public SagaCommandHandlerBuilder(SagaCommandHandlersBuilder parent, SagaCommandHandler h) {
        super();
        this.parent = parent;
        this.h = h;
    }

    @Override
    public <T extends Command> SagaCommandHandlerBuilder<T> onMessageReturningMessages(Class<T> commandClass, String commandType, Function<CommandMessage<T>, List<Message>> handler) {
        return parent.onMessageReturningMessages(commandClass, commandType, handler);
    }

    @Override
    public <T extends Command> SagaCommandHandlerBuilder<T> onMessageReturningOptionalMessage(Class<T> commandClass, String commandType, Function<CommandMessage<T>, Optional<Message>> handler) {
        return parent.onMessageReturningOptionalMessage(commandClass, commandType, handler);
    }

    @Override
    public <T extends Command> SagaCommandHandlerBuilder<T> onMessage(Class<T> commandClass, String commandType, Function<CommandMessage<T>, Message> handler) {
        return parent.onMessage(commandClass, commandType, handler);
    }

    @Override
    public <T extends Command> SagaCommandHandlerBuilder<T> onMessage(Class<T> commandClass, String commandType, Consumer<CommandMessage<T>> handler) {
        return parent.onMessage(commandClass, commandType, handler);
    }

    /**
     * コマンドハンドラーに事前ロック処理を設定します。
     *
     * @param preLock コマンドメッセージとパス変数を基にロック対象を決定する関数
     * @return このビルダーインスタンス
     */
    @SuppressWarnings("unchecked")
    public SagaCommandHandlerBuilder<C> withPreLock(BiFunction<CommandMessage<C>, PathVariables, LockTarget> preLock) {
        h.setPreLock((raw, pvs) -> preLock.apply((CommandMessage<C>) raw, pvs));
        return this;
    }

    public SagaCommandHandlerBuilder<C> withPreLock(Class<?> entityClass, String entityId) {
        h.setPreLock((raw, pvs) -> new LockTarget(entityClass, entityId));
        return this;
    }

    /**
     * コマンドハンドラーに事後ロック処理を設定します。
     *
     * @param postLock 事後ロック処理を実行する関数
     * @return このビルダーインスタンス
     */
    public SagaCommandHandlerBuilder<C> withPostLock(PostLockFunction<C> postLock) {
        h.setPostLock(postLock);
        return this;
    }

    /**
     * 構築したコマンドハンドラー群を返します。
     *
     * @return CommandHandlersインスタンス
     */
    public CommandHandlers build() {
        return parent.build();
    }
}
