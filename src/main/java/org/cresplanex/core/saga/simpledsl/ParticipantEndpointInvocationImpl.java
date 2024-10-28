package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.CommandWithDestinationAndType;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.messaging.common.Message;

public class ParticipantEndpointInvocationImpl<Data, C extends Command> extends AbstractParticipantInvocation<Data> {


  private final CommandEndpoint<C> commandEndpoint;
  private final Function<Data, C> commandProvider;

  public ParticipantEndpointInvocationImpl(Optional<Predicate<Data>> invocablePredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
    super(invocablePredicate);
    this.commandEndpoint = commandEndpoint;
    this.commandProvider = commandProvider;
  }

  @Override
  public boolean isSuccessfulReply(Message message) {
    return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
  }

  @Override
  public CommandWithDestinationAndType makeCommandToSend(Data data) {
    return CommandWithDestinationAndType.command(commandEndpoint.getCommandChannel(), null, commandProvider.apply(data)); // TODO notifications
  }
}
