package org.cresplanex.core.saga.orchestration;

/**
 * Sagaの状態遷移アクションを定義するインターフェース。 Sagaデータとリプライを受け取り、次のアクションを生成します。
 *
 * @param <Data> Sagaデータの型
 * @param <Reply> リプライの型
 */
public interface SagaStateMachineAction<Data, Reply> {

    /**
     * Sagaデータとリプライを適用して次のSagaアクションを生成します。
     *
     * @param data Sagaデータ
     * @param reply リプライ
     * @return 次のSagaアクション
     */
    SagaActions<Data> apply(Data data, Reply reply);

}
