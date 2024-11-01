package org.cresplanex.core.saga.orchestration.event;

import org.cresplanex.core.events.common.DomainEvent;

/**
 * 特定のイベントクラスとアグリゲートIDを保持するクラス。
 */
public class EventClassAndAggregateId {

    private final Class<DomainEvent> eventClass;
    private final Long aggregateId;

    /**
     * 指定されたイベントクラスとアグリゲートIDでインスタンスを作成します。
     *
     * @param eventClass イベントクラス
     * @param aggregateId アグリゲートID
     */
    public EventClassAndAggregateId(Class<DomainEvent> eventClass, Long aggregateId) {
        this.eventClass = eventClass;
        this.aggregateId = aggregateId;
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
     * アグリゲートIDを取得します。
     *
     * @return アグリゲートID
     */
    public Long getAggregateId() {
        return aggregateId;
    }

    /**
     * 指定されたアグリゲートタイプ、アグリゲートID、およびイベントタイプがこのインスタンスのものと一致するかを確認します。
     *
     * @param aggregateType アグリゲートタイプ
     * @param aggregateId アグリゲートID
     * @param eventType イベントタイプ
     * @return 一致する場合はtrue、そうでない場合はfalse
     */
    public boolean isFor(String aggregateType, long aggregateId, String eventType) {
        return eventClass.getName().equals(eventType) && this.aggregateId.equals(aggregateId);
    }
}
