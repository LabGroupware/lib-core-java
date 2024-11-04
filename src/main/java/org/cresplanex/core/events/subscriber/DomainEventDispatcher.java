package org.cresplanex.core.events.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.events.common.DomainEvent;
import org.cresplanex.core.events.common.DomainEventEnvelopeImpl;
import org.cresplanex.core.events.common.DomainEventNameMapping;
import org.cresplanex.core.events.common.EventMessageHeaders;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.consumer.MessageConsumer;

/**
 * ドメインイベントを処理するためのディスパッチャクラスです。
 * 指定されたメッセージコンシューマを通じてイベントを受信し、対応するイベントハンドラを用いて処理します。
 */
public class DomainEventDispatcher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String eventDispatcherId;
    private final DomainEventHandlers domainEventHandlers;
    private final MessageConsumer messageConsumer;

    private final DomainEventNameMapping domainEventNameMapping;

    /**
     * {@code DomainEventDispatcher}のインスタンスを作成します。
     *
     * @param eventDispatcherId ディスパッチャのID
     * @param domainEventHandlers 登録されたドメインイベントハンドラ
     * @param messageConsumer メッセージコンシューマ
     * @param domainEventNameMapping ドメインイベントの名前マッピング
     */
    public DomainEventDispatcher(String eventDispatcherId, DomainEventHandlers domainEventHandlers, MessageConsumer messageConsumer, DomainEventNameMapping domainEventNameMapping) {
        this.eventDispatcherId = eventDispatcherId;
        this.domainEventHandlers = domainEventHandlers;
        this.messageConsumer = messageConsumer;
        this.domainEventNameMapping = domainEventNameMapping;
    }

    /**
     * ディスパッチャを初期化し、イベントの受信を開始します。
     */
    public void initialize() {
        logger.info("Initializing domain event dispatcher");
        messageConsumer.subscribe(eventDispatcherId, domainEventHandlers.getAggregateTypesAndEvents(), this::messageHandler);
        logger.info("Initialized domain event dispatcher");
    }

    /**
     * 受信したメッセージを処理し、適切なハンドラを呼び出します。
     *
     * @param message 受信したメッセージ
     */
    public void messageHandler(Message message) {
        String aggregateType = message.getRequiredHeader(EventMessageHeaders.AGGREGATE_TYPE);

        // イベントのタイプを取得し、ドメインイベントのクラス名に変換
        message.setHeader(EventMessageHeaders.EVENT_TYPE,
                domainEventNameMapping.externalEventTypeToEventClassName(aggregateType, message.getRequiredHeader(EventMessageHeaders.EVENT_TYPE)));

        // メッセージに対応するハンドラを取得
        Optional<DomainEventHandler> handler = domainEventHandlers.findTargetMethod(message);

        if (handler.isEmpty()) {
            return; // 対応するハンドラがない場合は何もせずに終了
        }

        // メッセージをドメインイベントとしてパース
        DomainEvent param = JSonMapper.fromJson(message.getPayload(), handler.get().getEventClass());

        // ドメインイベントをラップしたDomainEventEnvelopeを生成し、ハンドラを呼び出し
        handler.get().invoke(new DomainEventEnvelopeImpl<>(message,
                aggregateType,
                message.getRequiredHeader(EventMessageHeaders.AGGREGATE_ID),
                message.getRequiredHeader(Message.ID),
                param));
    }
}
