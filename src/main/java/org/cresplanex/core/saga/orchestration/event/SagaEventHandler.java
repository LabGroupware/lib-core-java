package org.cresplanex.core.saga.orchestration.event;

import java.util.function.Function;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;
import org.cresplanex.core.saga.orchestration.SagaStateMachineAction;

/**
 * サガのイベントハンドラを表すクラス。
 *
 * @param <Data> サガデータの型
 */
public class SagaEventHandler<Data> {

    private final Class<DomainEvent> eventClass;
    private final Function<Data, Long> aggregateIdProvider;
    private final SagaStateMachineAction<Data, DomainEventEnvelope<DomainEvent>> action;

    /**
     * 指定されたイベントクラス、アグリゲートIDプロバイダー、およびアクションでインスタンスを作成します。
     *
     * @param eventClass イベントのクラス
     * @param aggregateIdProvider アグリゲートIDを取得するための関数
     * @param action イベントハンドラとしてのサガアクション
     */
    public SagaEventHandler(Class<DomainEvent> eventClass, Function<Data, Long> aggregateIdProvider, SagaStateMachineAction<Data, DomainEventEnvelope<DomainEvent>> action) {
        this.eventClass = eventClass;
        this.aggregateIdProvider = aggregateIdProvider;
        this.action = action;
    }

    /**
     * 新しいサガイベントハンドラを作成します。
     *
     * @param eventClass イベントのクラス
     * @param aggregateIdProvider アグリゲートIDを提供する関数
     * @param eventHandler イベントハンドラとしてのサガアクション
     * @param <Data> サガデータの型
     * @return 作成されたサガイベントハンドラ
     */
    public static <Data> SagaEventHandler<Data> make(Class<DomainEvent> eventClass, Function<Data, Long> aggregateIdProvider,
            SagaStateMachineAction<Data, DomainEventEnvelope<DomainEvent>> eventHandler) {
        return new SagaEventHandler<>(eventClass, aggregateIdProvider, eventHandler);
    }

    /**
     * サガデータに基づいてイベントクラスとアグリゲートIDを取得します。
     *
     * @param data サガデータ
     * @return イベントクラスとアグリゲートIDを持つオブジェクト
     */
    public EventClassAndAggregateId eventClassAndAggregateId(Data data) {
        return new EventClassAndAggregateId(eventClass, aggregateIdProvider.apply(data));
    }

    /**
     * イベントクラスを取得します。
     *
     * @return イベントクラス
     */
    public Class<DomainEvent> getEventClass() {
        return eventClass;
    }

    /**
     * サガのアクションを取得します。
     *
     * @return サガアクション
     */
    public SagaStateMachineAction<Data, DomainEventEnvelope<DomainEvent>> getAction() {
        return action;
    }
}
