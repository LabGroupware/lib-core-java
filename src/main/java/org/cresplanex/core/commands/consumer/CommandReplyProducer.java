package org.cresplanex.core.commands.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.cresplanex.core.messaging.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * コマンドの返信メッセージを送信するためのクラス。
 */
public class CommandReplyProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MessageProducer messageProducer;

    /**
     * 指定されたメッセージプロデューサでインスタンスを初期化します。
     *
     * @param messageProducer メッセージを送信するプロデューサ
     */
    public CommandReplyProducer(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    /**
     * 複数の返信メッセージを送信します。
     *
     * @param commandReplyToken コマンド返信トークン
     * @param replies 返信メッセージの配列
     * @return 送信したメッセージのリスト
     */
    public List<Message> sendReplies(CommandReplyToken commandReplyToken, Message... replies) {
        return sendReplies(commandReplyToken, Arrays.asList(replies));
    }

    /**
     * 返信メッセージのリストを送信します。
     *
     * @param commandReplyToken コマンド返信トークン
     * @param replies 返信メッセージのリスト
     * @return 送信したメッセージのリスト
     */
    public List<Message> sendReplies(CommandReplyToken commandReplyToken, List<Message> replies) {
        if (commandReplyToken.getReplyChannel() == null) {
            if (!replies.isEmpty()) {
                throw new RuntimeException("Reply channel is not specified, but a reply message exists.");
            }
            return Collections.emptyList();
        }

        if (replies.isEmpty()) {
            logger.trace("Reply message is empty. Not publishing.");
        }

        String replyChannel = commandReplyToken.getReplyChannel();
        List<Message> results = new ArrayList<>(replies.size());

        for (Message reply : replies) {
            Message message = MessageBuilder
                    .withMessage(reply)
                    .withExtraHeaders("", commandReplyToken.getReplyHeaders())
                    .build();
            messageProducer.send(replyChannel, message);
            results.add(message);
        }
        return results;
    }
}
