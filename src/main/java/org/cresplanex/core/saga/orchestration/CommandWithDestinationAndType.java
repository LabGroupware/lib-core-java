package org.cresplanex.core.saga.orchestration;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandWithDestination;

public class CommandWithDestinationAndType {

    private final CommandWithDestination commandWithDestination;
    private final boolean notification;

    public static CommandWithDestinationAndType command(String channel, String resource, Command command) {
        return command(new CommandWithDestination(channel, resource, command));
    }

    public static CommandWithDestinationAndType command(CommandWithDestination command) {
        return new CommandWithDestinationAndType(command, false);
    }

    public static CommandWithDestinationAndType notification(String channel, String resource, Command command) {
        return notification(new CommandWithDestination(channel, resource, command));
    }

    public static CommandWithDestinationAndType notification(CommandWithDestination command) {
        return new CommandWithDestinationAndType(command, true);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public CommandWithDestinationAndType(CommandWithDestination commandWithDestination, boolean notification) {
        this.commandWithDestination = commandWithDestination;
        this.notification = notification;
    }

    public CommandWithDestination getCommandWithDestination() {
        return commandWithDestination;
    }

    public boolean isNotification() {
        return notification;
    }

    boolean isCommand() {
        return !isNotification();
    }
}
