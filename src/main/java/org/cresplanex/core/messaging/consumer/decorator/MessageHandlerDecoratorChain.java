package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

/**
 * メッセージハンドラデコレータチェーンを表すインターフェース。
 * <p>
 * チェーン内の次のデコレータを呼び出すためのメソッドを提供します。</p>
 */
public interface MessageHandlerDecoratorChain {

    /**
     * チェーン内の次のデコレータを呼び出します。
     *
     * @param subscriberIdAndMessage サブスクライバIDとメッセージ情報を持つオブジェクト
     */
    void invokeNext(SubscriberIdAndMessage subscriberIdAndMessage);
}
