package org.cresplanex.core.saga.participant;

import org.cresplanex.core.saga.common.LockTarget;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandHandler;
import org.cresplanex.core.commands.consumer.CommandHandlerArgs;
import org.cresplanex.core.commands.consumer.CommandMessage;
import org.cresplanex.core.commands.consumer.PathVariables;
import org.cresplanex.core.messaging.common.Message;

public class SagaCommandHandler extends CommandHandler {

    private Optional<BiFunction<CommandMessage<?>, PathVariables, LockTarget>> preLock = Optional.empty();
    private Optional<PostLockFunction<?>> postLock = Optional.empty();

    public <C extends Command> SagaCommandHandler(String channel, Class<C> commandClass, Function<CommandHandlerArgs<C>, List<Message>> handler) {
        super(channel, Optional.empty(), commandClass, handler);
    }

    public void setPreLock(BiFunction<CommandMessage<?>, PathVariables, LockTarget> preLock) {
        this.preLock = Optional.of(preLock);
    }

    public void setPostLock(PostLockFunction<?> postLock) {
        this.postLock = Optional.of(postLock);
    }

    public Optional<BiFunction<CommandMessage<?>, PathVariables, LockTarget>> getPreLock() {
        return preLock;
    }

    public Optional<PostLockFunction<?>> getPostLock() {
        return postLock;
    }
}
