package org.cresplanex.core.saga.participant;

import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.lock.LockTarget;

/**
 * PostLockFunctionインターフェースは、特定の条件に基づいてロック対象を決定するための関数を表します。
 *
 * @param <C> 実装時に使用される特定のコマンドの型
 */
public interface PostLockFunction<C> {

    /**
     * コマンドメッセージ、パス変数、返信メッセージを基にロック対象を決定します。
     *
     * @param raw コマンドメッセージオブジェクト。処理の基本的なデータを含みます。
     * @param pvs パス変数オブジェクト。メッセージのパスに含まれる動的な変数を表します。
     * @param reply 返信メッセージオブジェクト。応答データやステータス情報を含みます。
     * @return LockTarget ロックの対象とするオブジェクト
     */
    public LockTarget apply(CommandMessage<?> raw, PathVariables pvs, Message reply);
}
