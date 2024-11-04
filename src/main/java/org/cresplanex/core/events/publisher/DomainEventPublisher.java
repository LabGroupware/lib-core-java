package org.cresplanex.core.events.publisher;

import java.util.List;
import java.util.Map;
import org.cresplanex.core.events.common.DomainEvent;

/**
 * ドメインイベントの公開を行うインターフェースです。
 */
public interface DomainEventPublisher {

    /**
     * ドメインイベントを公開します。
     *
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param domainEvents 公開するドメインイベントのリスト
     */
    void publish(String aggregateType, Object aggregateId, List<DomainEvent> domainEvents);

    /**
     * イベントタイプ名を指定してドメインイベントを公開します。
     *
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param domainEvents 公開するドメインイベントのリスト
     * @param eventTypeName イベントタイプ名
     */
    void publish(String aggregateType, Object aggregateId, List<DomainEvent> domainEvents, String eventTypeName);

    /**
     * ヘッダーを指定してドメインイベントを公開します。
     *
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param headers 追加のヘッダー情報
     * @param domainEvents 公開するドメインイベントのリスト
     */
    void publish(String aggregateType, Object aggregateId, Map<String, String> headers, List<DomainEvent> domainEvents);

    /**
     * イベントタイプ名とヘッダーを指定してドメインイベントを公開します。
     *
     * @param aggregateType 集約のタイプ
     * @param aggregateId 集約のID
     * @param headers 追加のヘッダー情報
     * @param domainEvents 公開するドメインイベントのリスト
     * @param eventTypeName イベントタイプ名
     */
    void publish(String aggregateType, Object aggregateId, Map<String, String> headers, List<DomainEvent> domainEvents, String eventTypeName);

    /**
     * ドメインイベントを公開します（集約のクラス名を指定）。
     *
     * @param aggregateType 集約のクラス
     * @param aggregateId 集約のID
     * @param domainEvents 公開するドメインイベントのリスト
     */
    default void publish(Class<?> aggregateType, Object aggregateId, List<DomainEvent> domainEvents) {
        publish(aggregateType.getName(), aggregateId, domainEvents);
    }
}
