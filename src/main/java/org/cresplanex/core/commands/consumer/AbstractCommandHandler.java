package org.cresplanex.core.commands.consumer;

import java.util.Optional;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.paths.ResourcePath;
import org.cresplanex.core.commands.common.paths.ResourcePathPattern;
import org.cresplanex.core.messaging.common.Message;

public class AbstractCommandHandler<RESULT> {

    private final String channel;
    private final Optional<String> resource;
    private final Class<? extends Command> commandClass;
    // private final Class commandClass;
    private final Function<CommandHandlerArgs<Command>, RESULT> handler;

    @SuppressWarnings("unchecked")
    public <C extends Command> AbstractCommandHandler(String channel, Optional<String> resource,
            Class<C> commandClass,
            Function<CommandHandlerArgs<C>, RESULT> handler) {
        this.channel = channel;
        this.resource = resource;
        this.commandClass = commandClass;
        this.handler = (CommandHandlerArgs<Command> args) -> handler.apply((CommandHandlerArgs<C>) args);
    }

    public String getChannel() {
        return channel;
    }

    public boolean handles(Message message) {
        return commandTypeMatches(message) && resourceMatches(message);
    }

    private boolean resourceMatches(Message message) {
        return !resource.isPresent() || message.getHeader(CommandMessageHeaders.RESOURCE).map(m -> resourceMatches(m, resource.get())).orElse(false);
    }

    private boolean commandTypeMatches(Message message) {
        return commandClass.getName().equals(
                message.getRequiredHeader(CommandMessageHeaders.COMMAND_TYPE));
    }

    private boolean resourceMatches(String messageResource, String methodPath) {
        ResourcePathPattern r = ResourcePathPattern.parse(methodPath);
        ResourcePath mr = ResourcePath.parse(messageResource);
        return r.isSatisfiedBy(mr);
    }

    // public Class getCommandClass() {
    //     return commandClass;
    // }
    public Class<? extends Command> getCommandClass() {
        return commandClass;
    }

    public Optional<String> getResource() {
        return resource;
    }

    public RESULT invokeMethod(CommandHandlerArgs<Command> commandHandlerArgs) {
        return (RESULT) handler.apply(commandHandlerArgs);
    }

    // public RESULT invokeMethod(CommandHandlerArgs commandHandlerArgs) {
    //     return (RESULT) handler.apply(commandHandlerArgs);
    // }
}
