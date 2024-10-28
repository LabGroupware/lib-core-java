package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.consumer.CommandWithDestination;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.CommandWithDestinationAndType;

public class ParticipantInvocationImpl<Data, C extends Command> extends AbstractParticipantInvocation<Data> {
  private final boolean notification;
  private final Function<Data, CommandWithDestination> commandBuilder;


  public ParticipantInvocationImpl(Optional<Predicate<Data>> invocablePredicate, Function<Data, CommandWithDestination> commandBuilder) {
    this(invocablePredicate, commandBuilder, false);
  }

  public ParticipantInvocationImpl(Optional<Predicate<Data>> invocablePredicate, Function<Data, CommandWithDestination> commandBuilder, boolean notification) {
    super(invocablePredicate);
    this.commandBuilder = commandBuilder;
    this.notification = notification;
  }

    @Override
  public boolean isSuccessfulReply(Message message) {
    return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
  }

  @Override
  public CommandWithDestinationAndType makeCommandToSend(Data data) {
    return new CommandWithDestinationAndType(commandBuilder.apply(data), notification);
  }
}
