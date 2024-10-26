package org.cresplanex.core.commands.consumer;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.messaging.common.Message;

public class CommandMessage<T> {

  private final String messageId;
  private final T command;
  private final Map<String, String> correlationHeaders;
  private final Message message;

  public Message getMessage() {
    return message;
  }

  public CommandMessage(String messageId, T command, Map<String, String> correlationHeaders, Message message) {
    this.messageId = messageId;
    this.command = command;
    this.correlationHeaders = correlationHeaders;
    this.message = message;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public String getMessageId() {
    return messageId;
  }

  public T getCommand() {
    return command;
  }

  public Map<String, String> getCorrelationHeaders() {
    return correlationHeaders;
  }
}
