package org.cresplanex.core.commands.consumer;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.messaging.common.Message;

public class CommandHandler extends AbstractCommandHandler<List<Message>> {

    public <C extends Command> CommandHandler(String channel, Optional<String> resource,
            Class<C> commandClass,
            Function<CommandHandlerArgs<C>, List<Message>> handler) {

        super(channel, resource, commandClass, handler);
    }
}
