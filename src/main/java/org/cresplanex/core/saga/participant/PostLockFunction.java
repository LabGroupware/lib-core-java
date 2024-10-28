package org.cresplanex.core.saga.participant;

import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.common.LockTarget;

public interface PostLockFunction<C> {

    public LockTarget apply(CommandMessage<Object> raw, PathVariables pvs, Message reply);
}
