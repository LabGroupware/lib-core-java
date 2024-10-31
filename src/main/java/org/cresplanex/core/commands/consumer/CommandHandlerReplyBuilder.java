package org.cresplanex.core.commands.consumer;

import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.Failure;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.Success;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;

public class CommandHandlerReplyBuilder {

    private static <T> Message with(T reply, CommandReplyOutcome outcome) {
        MessageBuilder messageBuilder = MessageBuilder
                .withPayload(JSonMapper.toJson(reply))
                .withHeader(ReplyMessageHeaders.REPLY_OUTCOME, outcome.name())
                .withHeader(ReplyMessageHeaders.REPLY_TYPE, reply.getClass().getName());
        return messageBuilder.build();
    }

    public static Message withSuccess(Object reply) {
        return with(reply, CommandReplyOutcome.SUCCESS);
    }

    public static Message withSuccess() {
        return withSuccess(new Success());
    }

    public static Message withFailure() {
        return withFailure(new Failure());
    }

    public static Message withFailure(Object reply) {
        return with(reply, CommandReplyOutcome.FAILURE);
    }

}
