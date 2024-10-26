package org.cresplanex.core.commands.consumer;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.commands.common.Command;

public class CommandWithDestination {
  private final String destinationChannel;
  private final String resource;
  private final Command command;
  private final Map<String, String> extraHeaders;

  public CommandWithDestination(String destinationChannel, String resource, Command command, Map<String, String> extraHeaders) {
    this.destinationChannel = destinationChannel;
    this.resource = resource;
    this.command = command;
    this.extraHeaders = extraHeaders;
  }

  public CommandWithDestination(String destinationChannel, String resource, Command command) {
    this(destinationChannel, resource, command, Collections.emptyMap());
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }


  public String getDestinationChannel() {
    return destinationChannel;
  }

  public String getResource() {
    return resource;
  }

  public Command getCommand() {
    return command;
  }

  public Map<String, String> getExtraHeaders() {
    return extraHeaders;
  }
}
