package org.cresplanex.core.interceptor;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A message interceptor that logs messages.
 *
 * メッセージをログに記録するためのインターセプター.
 */
public class LoggingMessageInterceptor implements MessageInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("org.cresplanex.core");

    @Override
    public void postSend(Message message, Exception e) {
        logger.info("Message Sent: {}", message); // 送信したメッセージをログに記録
    }

    @Override
    public void preHandle(String subscriberId, Message message) {
        logger.info("message received: {} {}", subscriberId, message); // 受信したメッセージとサブスクライバーIDを処理前にログに記録
    }
}
