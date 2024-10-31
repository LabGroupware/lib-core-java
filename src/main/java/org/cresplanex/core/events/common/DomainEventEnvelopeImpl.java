package org.cresplanex.core.events.common;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.messaging.common.Message;

/**
 * {@link DomainEventEnvelope}の具体的な実装クラスです。
 *
 * @param <T> ドメインイベントのタイプ
 */
public class DomainEventEnvelopeImpl<T extends DomainEvent> implements DomainEventEnvelope<T> {

    private final Message message;
    private final String aggregateType;
    private final String aggregateId;
    private final String eventId;
    private final T event;

    /**
     * {@code DomainEventEnvelopeImpl}のインスタンスを生成します。
     *
     * @param message メッセージ
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param eventId イベントID
     * @param event ドメインイベント
     */
    public DomainEventEnvelopeImpl(Message message, String aggregateType, String aggregateId, String eventId, T event) {
        this.message = message;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventId = eventId;
        this.event = event;
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public Message getMessage() {
        return message;
    }

    @Override
    public T getEvent() {
        return event;
    }

    @Override
    public String getAggregateType() {
        return aggregateType;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
