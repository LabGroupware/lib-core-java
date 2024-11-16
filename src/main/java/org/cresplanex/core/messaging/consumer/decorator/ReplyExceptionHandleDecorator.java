package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.commands.consumer.ReplyException;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

/**
 * リプライ例外を処理するデコレータクラス
 */
public class ReplyExceptionHandleDecorator implements MessageHandlerDecorator, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ReplyExceptionHandleDecorator.class);
    private final CommandReplyProducer commandReplyProducer;

    public ReplyExceptionHandleDecorator(CommandReplyProducer commandReplyProducer) {
        this.commandReplyProducer = commandReplyProducer;
    }

    /**
     * `OptimisticLockingFailureException`が発生した際にリトライを実施しながら、
     * メッセージ処理チェーンを次に進めます。
     *
     * @param subscriberIdAndMessage 処理対象のサブスクライバIDとメッセージ情報
     * @param messageHandlerDecoratorChain メッセージ処理チェーン
     */
    @Override
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        try {
            messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
        } catch (ReplyException e) {
            commandReplyProducer.sendReplies(e.getCommandReplyToken(), e.getReplies());
        }
    }

    @Override
    public int getOrder() {
        return 140;
    }
}
