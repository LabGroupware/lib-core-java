package org.cresplanex.core.commands.consumer;

import static java.util.Collections.singletonList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.commands.common.Failure;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandDispatcher {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String commandDispatcherId;

    private final CommandHandlers commandHandlers;

    private final MessageConsumer messageConsumer;

    private final CommandNameMapping commandNameMapping;

    private final CommandReplyProducer commandReplyProducer;

    public CommandDispatcher(String commandDispatcherId,
            CommandHandlers commandHandlers,
            MessageConsumer messageConsumer,
            CommandNameMapping commandNameMapping,
            CommandReplyProducer commandReplyProducer) {
        this.commandDispatcherId = commandDispatcherId;
        this.commandHandlers = commandHandlers;
        this.messageConsumer = messageConsumer;
        this.commandReplyProducer = commandReplyProducer;
        this.commandNameMapping = commandNameMapping;
    }

    public void initialize() {
        messageConsumer.subscribe(commandDispatcherId,
                commandHandlers.getChannels(),
                this::messageHandler);
    }

    public void messageHandler(Message message) {
        logger.trace("Received message {} {}", commandDispatcherId, message);

        message.setHeader(CommandMessageHeaders.COMMAND_TYPE,
                commandNameMapping.externalCommandTypeToCommandClassName(message.getRequiredHeader(CommandMessageHeaders.COMMAND_TYPE)));

        Optional<CommandHandler> possibleMethod = commandHandlers.findTargetMethod(message);
        if (!possibleMethod.isPresent()) {
            throw new RuntimeException("No method for " + message);
        }

        CommandHandler m = possibleMethod.get();

        CommandHandlerParams commandHandlerParams = new CommandHandlerParams(message, m.getCommandClass(), m.getResource());
        CommandReplyToken commandReplyToken = new CommandReplyToken(commandHandlerParams.getCorrelationHeaders(), commandHandlerParams.getDefaultReplyChannel().orElse(null));

        List<Message> replies;
        try {
            CommandMessage<Object> cm = new CommandMessage<>(message.getId(),
                    commandHandlerParams.getCommand(),
                    commandHandlerParams.getCorrelationHeaders(),
                    message);

            replies = invoke(m, cm, commandHandlerParams.getPathVars(), commandReplyToken);
            logger.trace("Generated replies {} {} {}", commandDispatcherId, message, replies);
        } catch (Exception e) {
            logger.error("Generated error {} {} {}", commandDispatcherId, message, e.getClass().getName());
            logger.error("Generated error", e);
            handleException(m, e, commandReplyToken);
            return;
        }

        commandReplyProducer.sendReplies(commandReplyToken, replies);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected List<Message> invoke(CommandHandler commandHandler, CommandMessage<?> cm, Map<String, String> pathVars, CommandReplyToken commandReplyToken) {
        return commandHandler.invokeMethod(new CommandHandlerArgs(cm, new PathVariables(pathVars), commandReplyToken));
    }

    private void handleException(CommandHandler commandHandler,
            Throwable cause,
            CommandReplyToken commandReplyToken) {
        Optional<CommandExceptionHandler> m = commandHandlers.findExceptionHandler(commandHandler, cause);

        logger.info("Handler for {} is {}", cause.getClass(), m);

        List<Message> replies = m
                .map(handler -> handler.invoke(cause))
                .orElseGet(() -> singletonList(MessageBuilder.withPayload(JSonMapper.toJson(new Failure())).build()));

        commandReplyProducer.sendReplies(commandReplyToken, replies);
    }
}
