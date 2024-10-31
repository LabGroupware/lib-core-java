package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

import java.util.function.BiConsumer;

/**
 * メッセージハンドラデコレータのインターフェースです。
 * <p>
 * このインターフェースを実装することで、メッセージ処理チェーンにデコレータを追加できます。</p>
 */
public interface MessageHandlerDecorator extends BiConsumer<SubscriberIdAndMessage, MessageHandlerDecoratorChain> {

    /**
     * デコレータの順序を取得します。
     *
     * @return デコレータの適用順序
     */
    int getOrder();
}
