package org.cresplanex.core.messaging.consumer.decorator;

import java.util.Arrays;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メッセージの受信前後に特定の処理を行うデコレータクラス。
 * <p>
 * 受信前にインターセプターの preReceive メソッドを実行し、 メッセージ処理後に postReceive メソッドを実行します。</p>
 */
public class PrePostReceiveMessageHandlerDecorator implements MessageHandlerDecorator {

    private static final Logger log = LoggerFactory.getLogger(PrePostReceiveMessageHandlerDecorator.class);
    // private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageInterceptor[] messageInterceptors;

    /**
     * コンストラクタ。
     *
     * @param messageInterceptors メッセージ受信の前後処理に使用するインターセプターの配列
     */
    public PrePostReceiveMessageHandlerDecorator(MessageInterceptor[] messageInterceptors) {
        this.messageInterceptors = messageInterceptors;
    }

    /**
     * メッセージの受信前後にインターセプターの処理を実行します。
     *
     * @param subscriberIdAndMessage サブスクライバIDとメッセージ情報を持つオブジェクト
     * @param messageHandlerDecoratorChain メッセージハンドラデコレータのチェーン
     */
    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        Message message = subscriberIdAndMessage.getMessage();
        preReceive(message);
        try {
            messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
        } catch (Throwable t) {
            log.error("Failed to handle message: subscriberId = {}, message = {}", subscriberIdAndMessage.getSubscriberId(), subscriberIdAndMessage.getMessage(), t);
            throw t;
        } finally {
            postReceive(message);
        }
    }

    /**
     * メッセージの受信前の処理を行います。
     *
     * @param message 受信するメッセージ
     */
    private void preReceive(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preReceive(message));
    }

    /**
     * メッセージの受信後の処理を行います。
     *
     * @param message 受信したメッセージ
     */
    private void postReceive(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postReceive(message));
    }

    /**
     * このデコレータの適用順序を取得します。
     *
     * @return デコレータの適用順序
     */
    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.PRE_POST_RECEIVE_MESSAGE_HANDLER_DECORATOR;
    }
}
