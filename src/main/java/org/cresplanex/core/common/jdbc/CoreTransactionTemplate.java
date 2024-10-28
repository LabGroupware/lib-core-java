package org.cresplanex.core.common.jdbc;

import java.util.function.Supplier;

/**
 * トランザクション内での処理実行を定義するテンプレートインターフェース。
 * <p>
 * トランザクション管理の実装に依存せず、トランザクション内で処理を実行できるメソッドを提供します。
 * </p>
 */
public interface CoreTransactionTemplate {

    /**
     * トランザクション内で指定されたコールバック処理を実行します。
     *
     * @param <T> コールバック処理の戻り値の型
     * @param callback トランザクション内で実行するコールバック
     * @return コールバック処理の結果
     */
    <T> T executeInTransaction(Supplier<T> callback);
}
