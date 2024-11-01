package org.cresplanex.core.saga.orchestration.event;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;

/**
 * サガの状態遷移の開始時にイベントを処理するハンドラ。
 *
 * @param <Data> サガデータのタイプ
 * @param <EventClass> イベントのクラス
 */
public interface EventStartingHandler<Data, EventClass extends DomainEvent> {

    /**
     * イベントを適用し、サガデータに反映します。
     *
     * @param data サガデータ
     * @param event 適用するイベント
     */
    void apply(Data data, DomainEventEnvelope<EventClass> event);
}
