package org.cresplanex.core.events.common;

import java.util.Map;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;

/**
 * ドメインイベント用のユーティリティクラスです。
 */
public class EventUtil {

    /**
     * ドメインイベントのメッセージを作成します。
     *
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param headers 追加のヘッダー情報
     * @param event ドメインイベント
     * @param eventType イベントタイプ
     * @return 作成されたメッセージ
     */
    public static Message makeMessageForDomainEvent(String aggregateType,
            Object aggregateId,
            Map<String, String> headers,
            DomainEvent event,
            String eventType) {
        String aggregateIdAsString = aggregateId.toString();
        return MessageBuilder
                .withPayload(JSonMapper.toJson(event))
                .withExtraHeaders("", headers)
                .withHeader(Message.PARTITION_ID, aggregateIdAsString)
                .withHeader(EventMessageHeaders.AGGREGATE_ID, aggregateIdAsString)
                .withHeader(EventMessageHeaders.AGGREGATE_TYPE, aggregateType)
                .withHeader(EventMessageHeaders.EVENT_TYPE, eventType)
                .build();
    }
}
