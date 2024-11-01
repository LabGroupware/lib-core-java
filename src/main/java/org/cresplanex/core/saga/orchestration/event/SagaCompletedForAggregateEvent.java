package org.cresplanex.core.saga.orchestration.event;

import org.cresplanex.core.events.common.DomainEvent;

/**
 * 特定のアグリゲートに関連するサガが完了したことを示すイベント。
 */
public class SagaCompletedForAggregateEvent implements DomainEvent {

    private final String sagaId;

    /**
     * 指定されたサガIDでインスタンスを作成します。
     *
     * @param sagaId 完了したサガのID
     */
    public SagaCompletedForAggregateEvent(String sagaId) {
        this.sagaId = sagaId;
    }

    /**
     * 完了したサガのIDを取得します。
     *
     * @return サガのID
     */
    public String getSagaId() {
        return sagaId;
    }
}
