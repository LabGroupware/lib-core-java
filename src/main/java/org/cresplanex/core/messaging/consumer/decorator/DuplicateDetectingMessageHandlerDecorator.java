package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.cresplanex.core.messaging.consumer.duplicate.DuplicateMessageDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重複メッセージを検出して処理するデコレータクラス。
 * <p>
 * メッセージが重複している場合は、メッセージハンドラの処理をスキップします。</p>
 */
public class DuplicateDetectingMessageHandlerDecorator implements MessageHandlerDecorator {

    private static final Logger log = LoggerFactory.getLogger(DuplicateDetectingMessageHandlerDecorator.class);
    private final DuplicateMessageDetector duplicateMessageDetector;

    /**
     * コンストラクタ。
     *
     * @param duplicateMessageDetector 重複メッセージを検出するためのデテクター
     */
    public DuplicateDetectingMessageHandlerDecorator(DuplicateMessageDetector duplicateMessageDetector) {
        this.duplicateMessageDetector = duplicateMessageDetector;
    }

    /**
     * 重複メッセージの検出処理を行い、重複していない場合のみ次の処理を呼び出します。
     *
     * @param subscriberIdAndMessage サブスクライバIDとメッセージ情報を持つオブジェクト
     * @param messageHandlerDecoratorChain メッセージハンドラデコレータのチェーン
     */
    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        duplicateMessageDetector.doWithMessage(subscriberIdAndMessage, () -> messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage));
    }

    /**
     * このデコレータの適用順序を取得します。
     *
     * @return デコレータの適用順序
     */
    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.DUPLICATE_DETECTING_MESSAGE_HANDLER_DECORATOR;
    }
}
