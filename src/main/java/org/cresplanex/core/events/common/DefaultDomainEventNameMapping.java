package org.cresplanex.core.events.common;

/**
 * DomainEventNameMappingインターフェースのデフォルト実装です。
 */
public class DefaultDomainEventNameMapping implements DomainEventNameMapping {

    /**
     * ドメインイベントを外部イベントタイプに変換します。
     *
     * @param aggregateType 集約タイプ
     * @param event ドメインイベント
     * @return ドメインイベントのクラス名を表す外部イベントタイプ
     */
    @Override
    public String eventToExternalEventType(String aggregateType, DomainEvent event) {
        return event.getClass().getName();
    }

    /**
     * 外部イベントタイプをイベントクラス名に変換します。
     *
     * @param aggregateType 集約タイプ
     * @param eventTypeHeader 外部イベントタイプのヘッダー
     * @return 外部イベントタイプのヘッダーからそのまま得られるイベントクラス名
     */
    @Override
    public String externalEventTypeToEventClassName(String aggregateType, String eventTypeHeader) {
        return eventTypeHeader;
    }
}
