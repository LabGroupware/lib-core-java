package org.cresplanex.core.saga.participant;

import org.cresplanex.core.saga.common.LockTarget;

import java.util.Optional;

import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.Success;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.producer.MessageBuilder;

public class SagaReplyMessageBuilder extends MessageBuilder {

    private Optional<LockTarget> lockTarget = Optional.empty();

    public SagaReplyMessageBuilder(LockTarget lockTarget) {
        this.lockTarget = Optional.of(lockTarget);
    }

    public static SagaReplyMessageBuilder withLock(Class<?> type, Object id) {
        return new SagaReplyMessageBuilder(new LockTarget(type, id));
    }

    private <T> Message with(T reply, CommandReplyOutcome outcome) {
        this.body = JSonMapper.toJson(reply);
        withHeader(ReplyMessageHeaders.REPLY_OUTCOME, outcome.name());
        withHeader(ReplyMessageHeaders.REPLY_TYPE, reply.getClass().getName());
        return new SagaReplyMessage(body, headers, lockTarget);
    }

    public Message withSuccess(Object reply) {
        return with(reply, CommandReplyOutcome.SUCCESS);
    }

    public Message withSuccess() {
        return withSuccess(new Success());
    }

}
