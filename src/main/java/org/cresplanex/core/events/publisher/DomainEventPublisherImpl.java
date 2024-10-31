package org.cresplanex.core.events.publisher;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.events.common.EventUtil;
import org.cresplanex.core.messaging.producer.MessageProducer;

/**
 * ドメインイベントを公開する実装クラスです。
 */
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private final MessageProducer messageProducer;
    private final DomainEventNameMapping domainEventNameMapping;

    /**
     * {@code DomainEventPublisherImpl}のインスタンスを生成します。
     *
     * @param messageProducer メッセージプロデューサー
     * @param domainEventNameMapping イベント名マッピング
     */
    public DomainEventPublisherImpl(MessageProducer messageProducer, DomainEventNameMapping domainEventNameMapping) {
        this.messageProducer = messageProducer;
        this.domainEventNameMapping = domainEventNameMapping;
    }

    @Override
    public void publish(String aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {
        publish(aggregateType, aggregateId, Collections.emptyMap(), domainEvents);
    }

    @Override
    public void publish(String aggregateType,
            Object aggregateId,
            Map<String, String> headers,
            List<DomainEvent> domainEvents) {

        for (DomainEvent event : domainEvents) {
            messageProducer.send(aggregateType,
                    EventUtil.makeMessageForDomainEvent(aggregateType, aggregateId, headers, event,
                            domainEventNameMapping.eventToExternalEventType(aggregateType, event)));
        }
    }
}
