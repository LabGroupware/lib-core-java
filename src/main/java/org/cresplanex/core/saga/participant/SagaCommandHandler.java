package org.cresplanex.core.saga.participant;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandHandler;
import org.cresplanex.core.commands.consumer.CommandHandlerArgs;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.lock.LockTarget;

/**
 * SagaCommandHandlerは、サガ内で使用されるコマンドハンドラです。
 * 事前および事後のロック処理を含め、特定のコマンドを処理するための機能を提供します。
 */
public class SagaCommandHandler extends CommandHandler {

    // 事前ロック処理: コマンドメッセージとパス変数を受け取り、ロック対象を返す関数
    private Optional<BiFunction<CommandMessage<?>, PathVariables, LockTarget>> preLock = Optional.empty();
    // 事後ロック処理: ロック対象に対して実行される関数
    private Optional<PostLockFunction<?>> postLock = Optional.empty();

    /**
     * SagaCommandHandlerのコンストラクタ。
     *
     * @param <C> コマンドの型
     * @param channel コマンドが送信されるチャンネル
     * @param commandClass コマンドのクラス型
     * @param handler コマンドの処理を行う関数
     */
    public <C extends Command> SagaCommandHandler(String channel, Class<C> commandClass, Function<CommandHandlerArgs<C>, List<Message>> handler) {
        super(channel, Optional.empty(), commandClass, handler);
    }

    /**
     * SagaCommandHandlerのコンストラクタ。
     *
     * @param <C> コマンドの型
     * @param channel コマンドが送信されるチャンネル
     * @param commandClass コマンドのクラス型
     * @param handler コマンドの処理を行う関数
     */
    public <C extends Command> SagaCommandHandler(String channel, Class<C> commandClass, String commandType, Function<CommandHandlerArgs<C>, List<Message>> handler) {
        super(channel, Optional.empty(), commandClass, handler, commandType);
    }

    /**
     * 事前ロック処理を設定します。コマンドメッセージとパス変数を基にロック対象を決定します。
     *
     * @param preLock コマンドメッセージとパス変数を受け取り、ロック対象を返す関数
     */
    public void setPreLock(BiFunction<CommandMessage<?>, PathVariables, LockTarget> preLock) {
        this.preLock = Optional.of(preLock);
    }

    /**
     * 事後ロック処理を設定します。コマンドの処理後に実行されるロック処理です。
     *
     * @param postLock ロック対象に対して実行される関数
     */
    public void setPostLock(PostLockFunction<?> postLock) {
        this.postLock = Optional.of(postLock);
    }

    /**
     * 事前ロック処理を取得します。
     *
     * @return 事前ロック処理のOptionalオブジェクト
     */
    public Optional<BiFunction<CommandMessage<?>, PathVariables, LockTarget>> getPreLock() {
        return preLock;
    }

    /**
     * 事後ロック処理を取得します。
     *
     * @return 事後ロック処理のOptionalオブジェクト
     */
    public Optional<PostLockFunction<?>> getPostLock() {
        return postLock;
    }
}
