package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaActions;

import java.util.function.Supplier;

/**
 * サガのアクションを提供するプロバイダークラスです。
 *
 * @param <Data> サガデータのタイプ
 */
public class SagaActionsProvider<Data> extends AbstractSagaActionsProvider<Data, SagaActions<Data>> {

    /**
     * コンストラクタ。
     *
     * @param sagaActions サガアクション
     */
    public SagaActionsProvider(SagaActions<Data> sagaActions) {
        super(sagaActions);
    }

    /**
     * コンストラクタ。
     *
     * @param sagaActionsSupport サガアクションを提供するサプライヤー
     */
    public SagaActionsProvider(Supplier<SagaActions<Data>> sagaActionsSupport) {
        super(sagaActionsSupport);
    }
}
