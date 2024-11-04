package org.cresplanex.core.events.common;

/**
 * イベントクラス名と外部イベントタイプのマッピングを行うインターフェースです。
 */
public interface DomainEventNameMapping {

    /**
     * ドメインイベントから外部イベントタイプへの変換を行います。
     *
     * @param aggregateType 集約タイプ
     * @param event ドメインイベント
     * @return 外部イベントタイプの文字列
     */
    String eventToExternalEventType(String aggregateType, DomainEvent event, String eventTypeName);

    String eventToExternalEventType(String aggregateType, DomainEvent event);

    /**
     * 外部イベントタイプからイベントクラス名への変換を行います。
     *
     * @param aggregateType 集約タイプ
     * @param eventTypeHeader 外部イベントタイプのヘッダー
     * @return イベントクラス名の文字列
     */
    String externalEventTypeToEventClassName(String aggregateType, String eventTypeHeader);
}
