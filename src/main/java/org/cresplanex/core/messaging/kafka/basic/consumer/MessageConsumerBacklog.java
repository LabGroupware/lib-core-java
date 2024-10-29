package org.cresplanex.core.messaging.kafka.basic.consumer;

/**
 * メッセージコンシューマのバックログを表すインターフェース。
 * <p>
 * このインターフェースは、バックログのサイズを取得するメソッドを提供します。
 * </p>
 */
public interface MessageConsumerBacklog {

    /**
     * バックログのサイズを返します。
     *
     * @return バックログのサイズ
     */
    int size();
}
