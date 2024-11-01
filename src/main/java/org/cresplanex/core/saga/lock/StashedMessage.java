package org.cresplanex.core.saga.lock;

import org.cresplanex.core.messaging.common.Message;

/**
 * Saga の中断時に一時保管されるメッセージを表すクラス。
 */
public class StashedMessage {

    private final String sagaType;
    private final String sagaId;
    private final Message message;

    /**
     * 指定された Saga タイプ、Saga ID、メッセージで StashedMessage を構築します。
     *
     * @param sagaType Saga のタイプ
     * @param sagaId Saga の ID
     * @param message 保管されるメッセージ
     */
    public StashedMessage(String sagaType, String sagaId, Message message) {
        this.sagaType = sagaType;
        this.sagaId = sagaId;
        this.message = message;
    }

    /**
     * Saga のタイプを取得します。
     *
     * @return Saga のタイプ
     */
    public String getSagaType() {
        return sagaType;
    }

    /**
     * Saga の ID を取得します。
     *
     * @return Saga の ID
     */
    public String getSagaId() {
        return sagaId;
    }

    /**
     * 保管されているメッセージを取得します。
     *
     * @return 保管されたメッセージ
     */
    public Message getMessage() {
        return message;
    }
}
