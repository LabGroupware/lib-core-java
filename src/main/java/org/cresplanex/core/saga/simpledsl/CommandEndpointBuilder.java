package org.cresplanex.core.saga.simpledsl;

import java.util.HashSet;
import java.util.Set;

import org.cresplanex.core.commands.common.Command;

public class CommandEndpointBuilder<C extends Command> {

  private String channel;
  private final Class<C> commandClass;
  private final Set<Class<?>> replyClasses = new HashSet<>();

  public CommandEndpointBuilder(Class<C> commandClass) {
    this.commandClass = commandClass;
  }

  public static <C extends Command> CommandEndpointBuilder<C> forCommand(Class<C> commandClass) {
    return new CommandEndpointBuilder<>(commandClass);
  }

  public CommandEndpointBuilder<C> withChannel(String channel) {
    this.channel = channel;
    return this;
  }


  public <T> CommandEndpointBuilder<C> withReply(Class<T> replyClass) {
    this.replyClasses.add(replyClass);
    return this;
  }

  public CommandEndpoint<C> build() {
    return new CommandEndpoint<>(channel, commandClass, replyClasses);
  }
}
