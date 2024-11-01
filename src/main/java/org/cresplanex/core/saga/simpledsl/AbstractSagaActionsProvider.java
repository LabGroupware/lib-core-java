package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaActions;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * サガアクションを提供する抽象クラスです。サガアクションの提供方法として 既存のサガアクション、もしくはサプライヤーを通じた動的なサガアクションの
 * 提供をサポートします。
 *
 * @param <Data> サガデータのタイプ
 * @param <SuppliedValue> 提供されるサガアクションの型
 */
public abstract class AbstractSagaActionsProvider<Data, SuppliedValue> {

    private final SagaActions<Data> sagaActions;
    private final Supplier<SuppliedValue> sagaActionsSupplier;

    /**
     * コンストラクタ。既存のサガアクションを設定します。
     *
     * @param sagaActions 設定するサガアクション
     */
    public AbstractSagaActionsProvider(SagaActions<Data> sagaActions) {
        this.sagaActions = sagaActions;
        this.sagaActionsSupplier = null;
    }

    /**
     * コンストラクタ。サガアクションのサプライヤーを設定します。
     *
     * @param sagaActionsSupplier サガアクションを提供するサプライヤー
     */
    public AbstractSagaActionsProvider(Supplier<SuppliedValue> sagaActionsSupplier) {
        this.sagaActions = null;
        this.sagaActionsSupplier = sagaActionsSupplier;
    }

    /**
     * サガアクションを変換し、適切な形式で提供します。
     *
     * @param f1 サガアクションが存在する場合の変換関数
     * @param f2 サガアクションがサプライヤーから提供される場合の変換関数
     * @return 変換後のサガアクション
     */
    public SuppliedValue toSagaActions(Function<SagaActions<Data>, SuppliedValue> f1, Function<SuppliedValue, SuppliedValue> f2) {
        return sagaActions != null ? f1.apply(sagaActions) : f2.apply(sagaActionsSupplier.get());
    }
}
