package org.cresplanex.core.events.subscriber;

import java.util.function.Consumer;

import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelope;
import org.cresplanex.core.events.common.EventMessageHeaders;
import org.cresplanex.core.messaging.common.Message;

/**
 * ドメインイベントの処理を担当するハンドラクラスです。
 */
public class DomainEventHandler {

    private final String aggregateType;
    private final Class<DomainEvent> eventClass;
    private final Consumer<DomainEventEnvelope<DomainEvent>> handler;

    /**
     * 指定された集約タイプ、イベントクラス、およびハンドラで{@code DomainEventHandler}のインスタンスを生成します。
     *
     * @param aggregateType 処理対象の集約タイプ
     * @param eventClass 処理対象のイベントクラス
     * @param handler イベントを処理するためのハンドラ
     */
    public DomainEventHandler(String aggregateType, Class<DomainEvent> eventClass, Consumer<DomainEventEnvelope<DomainEvent>> handler) {
        this.aggregateType = aggregateType;
        this.eventClass = eventClass;
        this.handler = handler;
    }

    /**
     * 指定されたメッセージがこのハンドラで処理可能かどうかを判定します。
     *
     * @param message 処理対象のメッセージ
     * @return このハンドラで処理可能な場合は{@code true}、それ以外の場合は{@code false}
     */
    public boolean handles(Message message) {
        return aggregateType.equals(message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE))
                && eventClass.getName().equals(message.getRequiredHeader(EventMessageHeaders.EVENT_TYPE));
    }

    /**
     * 指定されたイベントエンベロープを使用してハンドラを呼び出します。
     *
     * @param dee ドメインイベントエンベロープ
     */
    public void invoke(DomainEventEnvelope<DomainEvent> dee) {
        handler.accept(dee);
    }

    /**
     * このハンドラが処理するイベントのクラスを取得します。
     *
     * @return イベントクラス
     */
    public Class<DomainEvent> getEventClass() {
        return eventClass;
    }

    /**
     * このハンドラが対象とする集約タイプを取得します。
     *
     * @return 集約タイプ
     */
    public String getAggregateType() {
        return aggregateType;
    }
}
