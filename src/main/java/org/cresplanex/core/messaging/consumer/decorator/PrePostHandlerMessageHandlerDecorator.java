package org.cresplanex.core.messaging.consumer.decorator;

import java.util.Arrays;

import org.cresplanex.core.commands.consumer.ReplyException;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * メッセージの処理前後にフックを提供するデコレータクラスです。
 * <p>
 * 指定されたインターセプターを使用して、メッセージの事前および事後処理を行います。</p>
 */
public class PrePostHandlerMessageHandlerDecorator implements MessageHandlerDecorator {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageInterceptor[] messageInterceptors;

    /**
     * コンストラクタ。
     *
     * @param messageInterceptors メッセージの処理前後に適用するインターセプターの配列
     */
    public PrePostHandlerMessageHandlerDecorator(MessageInterceptor[] messageInterceptors) {
        this.messageInterceptors = messageInterceptors;
    }

    /**
     * メッセージの受信時に、事前および事後の処理を行います。
     *
     * @param subscriberIdAndMessage サブスクライバIDとメッセージ情報を持つオブジェクト
     * @param messageHandlerDecoratorChain メッセージハンドラのデコレータチェーン
     */
    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        Message message = subscriberIdAndMessage.getMessage();
        String subscriberId = subscriberIdAndMessage.getSubscriberId();
        preHandle(subscriberId, message);
        try {
            messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
            postHandle(subscriberId, message, null);
        } catch (ReplyException e) {
            postHandle(subscriberId, message, e);
            throw e;
        } catch (Throwable t) {
            logger.error("Failed to handle message: subscriberId = {}, message = {}", subscriberId, message, t);
            postHandle(subscriberId, message, t);
            throw t;
        }
    }

    /**
     * メッセージの事前処理を行います。
     *
     * @param subscriberId サブスクライバID
     * @param message 処理対象のメッセージ
     */
    private void preHandle(String subscriberId, Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preHandle(subscriberId, message));
    }

    /**
     * メッセージの事後処理を行います。
     *
     * @param subscriberId サブスクライバID
     * @param message 処理対象のメッセージ
     * @param t 処理中に発生した例外（発生しなかった場合はnull）
     */
    private void postHandle(String subscriberId, Message message, Throwable t) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postHandle(subscriberId, message, t));
    }

    /**
     * このデコレータの適用順序を取得します。
     *
     * @return デコレータの適用順序
     */
    @Override
    public int getOrder() {
        return BuiltInMessageHandlerDecoratorOrder.PRE_POST_HANDLER_MESSAGE_HANDLER_DECORATOR;
    }
}
