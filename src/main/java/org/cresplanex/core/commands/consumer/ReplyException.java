package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.messaging.common.Message;

import java.util.List;

public class ReplyException extends RuntimeException {

    private final CommandReplyToken commandReplyToken;
    private final List<Message> replies;

    public ReplyException(CommandReplyToken commandReplyToken, List<Message> replies) {
        super("ReplyException");
        this.commandReplyToken = commandReplyToken;
        this.replies = replies;
    }

    public CommandReplyToken getCommandReplyToken() {
        return commandReplyToken;
    }

    public List<Message> getReplies() {
        return replies;
    }
}
