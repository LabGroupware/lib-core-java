package org.cresplanex.core.saga.simpledsl;

import java.util.Set;

import org.cresplanex.core.commands.common.Command;

public class CommandEndpoint<C extends Command> {

  private final String commandChannel;
  private final Class<C> commandClass;
  private final Set<Class<?>> replyClasses;

  public CommandEndpoint(String commandChannel, Class<C> commandClass, Set<Class<?>> replyClasses) {
    this.commandChannel = commandChannel;
    this.commandClass = commandClass;
    this.replyClasses = replyClasses;
  }

  public String getCommandChannel() {
    return commandChannel;
  }

  public Class<C> getCommandClass() {
    return commandClass;
  }

  public Set<Class<?>> getReplyClasses() {
    return replyClasses;
  }
}
