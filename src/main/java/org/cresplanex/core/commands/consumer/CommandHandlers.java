package org.cresplanex.core.commands.consumer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

import org.cresplanex.core.messaging.common.Message;

public class CommandHandlers {

    private final List<CommandHandler> handlers;

    public CommandHandlers(List<CommandHandler> handlers) {
        this.handlers = handlers;
    }

    public Set<String> getChannels() {
        return handlers.stream().map(CommandHandler::getChannel).collect(toSet());
    }

    public Optional<CommandHandler> findTargetMethod(Message message) {
        return handlers.stream().filter(h -> h.handles(message)).findFirst();
    }

    public Optional<CommandExceptionHandler> findExceptionHandler(CommandHandler commandHandler, Throwable cause) {
        throw new UnsupportedOperationException(String.format("A command handler for command of type %s on channel %s threw an exception",
                commandHandler.getCommandClass().getName(),
                commandHandler.getChannel()),
                cause);
    }

    public List<CommandHandler> getHandlers() {
        return handlers;
    }
}
