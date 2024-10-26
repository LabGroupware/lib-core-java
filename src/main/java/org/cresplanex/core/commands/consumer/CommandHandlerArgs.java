package org.cresplanex.core.commands.consumer;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;

public class CommandHandlerArgs<CommandType extends Command> {

    private final CommandMessage<CommandType> commandMessage;
    private final PathVariables pathVars;
    private final CommandReplyToken commandReplyToken;

    public CommandHandlerArgs(CommandMessage<CommandType> commandMessage, PathVariables pathVars, CommandReplyToken commandReplyToken) {
        this.commandMessage = commandMessage;
        this.pathVars = pathVars;
        this.commandReplyToken = commandReplyToken;
    }

    public static <C extends Command, Result> Function<CommandHandlerArgs<C>, Result> makeFn(BiFunction<CommandMessage<C>, PathVariables, Result> handler) {
        return args -> handler.apply(args.getCommandMessage(), args.getPathVars());
    }

    public CommandMessage<CommandType> getCommandMessage() {
        return commandMessage;
    }

    public PathVariables getPathVars() {
        return pathVars;
    }

    public CommandReplyToken getCommandReplyToken() {
        return commandReplyToken;
    }

}
