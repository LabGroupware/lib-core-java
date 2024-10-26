package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.commands.common.Command;

public class PendingSagaCommand {

    private final String destination;
    private final String resource;
    private final Command command;

    public PendingSagaCommand(String destination, String resource, Command command) {
        this.destination = destination;
        this.resource = resource;
        this.command = command;
    }

    public String getDestination() {
        return destination;
    }

    public String getResource() {
        return resource;
    }

    public Command getCommand() {
        return command;
    }
}
