package org.cresplanex.core.common.jdbc;

import java.util.function.Supplier;

import org.springframework.transaction.support.TransactionTemplate;

/**
 * Springの {@link TransactionTemplate} を使用したトランザクションテンプレート実装。
 * <p>
 * このクラスは {@link CoreTransactionTemplate} を実装し、Springのトランザクション管理を利用して
 * トランザクション内での処理を実行します。
 * </p>
 */
public class CoreSpringTransactionTemplate implements CoreTransactionTemplate {

    /**
     * トランザクションの管理を提供する {@link TransactionTemplate} インスタンス
     */
    private final TransactionTemplate transactionTemplate;

    /**
     * {@link CoreSpringTransactionTemplate} のコンストラクタ。
     *
     * @param transactionTemplate Springのトランザクション管理テンプレート
     */
    public CoreSpringTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    /**
     * トランザクション内で指定されたコールバック処理を実行します。
     *
     * @param <T> コールバック処理の戻り値の型
     * @param callback トランザクション内で実行するコールバック
     * @return コールバック処理の結果
     */
    @Override
    public <T> T executeInTransaction(Supplier<T> callback) {
        return transactionTemplate.execute(status -> callback.get());
    }
}
