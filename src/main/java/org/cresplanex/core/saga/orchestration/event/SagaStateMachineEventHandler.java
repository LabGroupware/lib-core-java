package org.cresplanex.core.saga.orchestration.event;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;
import org.cresplanex.core.saga.orchestration.SagaActions;

/**
 * Sagaの状態遷移イベントハンドラ。 ドメインイベントを受け取り、Sagaデータに基づいて適切なアクションを生成します。
 *
 * @param <Data> Sagaデータの型
 * @param <EventClass> 処理するドメインイベントの型
 */
public interface SagaStateMachineEventHandler<Data, EventClass extends DomainEvent> {

    /**
     * ドメインイベントをSagaデータに適用し、Sagaアクションを生成します。
     *
     * @param data Sagaデータ
     * @param event ドメインイベントのエンベロープ
     * @return 生成されたSagaアクション
     */
    SagaActions<Data> apply(Data data, DomainEventEnvelope<EventClass> event);

}
