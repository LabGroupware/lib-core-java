package org.cresplanex.core.saga.orchestration.event;

import java.util.List;

import org.cresplanex.core.events.common.DomainEvent;

/**
 * 公開するイベントの情報を保持するクラス。
 */
public class EventToPublish {

    private final Class<?> aggregateType;
    private final String aggregateId;
    private final List<DomainEvent> domainEvents;

    /**
     * 指定されたアグリゲートタイプ、アグリゲートID、およびドメインイベントリストでインスタンスを作成します。
     *
     * @param aggregateType アグリゲートのクラス
     * @param aggregateId アグリゲートのID
     * @param domainEvents 公開するドメインイベントのリスト
     */
    public EventToPublish(Class<?> aggregateType, String aggregateId, List<DomainEvent> domainEvents) {
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.domainEvents = domainEvents;
    }

    /**
     * アグリゲートのクラスを取得します。
     *
     * @return アグリゲートのクラス
     */
    public Class<?> getAggregateType() {
        return aggregateType;
    }

    /**
     * アグリゲートIDを取得します。
     *
     * @return アグリゲートID
     */
    public String getAggregateId() {
        return aggregateId;
    }

    /**
     * ドメインイベントのリストを取得します。
     *
     * @return ドメインイベントのリスト
     */
    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }
}
