package org.cresplanex.core.events.common;

import org.cresplanex.core.messaging.common.Message;

/**
 * ドメインイベントをラップするエンベロープインターフェースです。
 *
 * @param <T> ドメインイベントのタイプ
 */
public interface DomainEventEnvelope<T extends DomainEvent> {

    /**
     * 集約IDを取得します。
     *
     * @return 集約ID
     */
    String getAggregateId();

    /**
     * メッセージを取得します。
     *
     * @return メッセージ
     */
    Message getMessage();

    /**
     * 集約のタイプを取得します。
     *
     * @return 集約タイプ
     */
    String getAggregateType();

    /**
     * イベントIDを取得します。
     *
     * @return イベントID
     */
    String getEventId();

    /**
     * ドメインイベントを取得します。
     *
     * @return ドメインイベント
     */
    T getEvent();
}
