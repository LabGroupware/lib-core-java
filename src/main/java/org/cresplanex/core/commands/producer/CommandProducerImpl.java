package org.cresplanex.core.commands.producer;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandNameMapping;
import static org.cresplanex.core.commands.producer.CommandMessageFactory.makeMessage;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.producer.MessageProducer;

public class CommandProducerImpl implements CommandProducer {

    private final MessageProducer messageProducer;
    private final CommandNameMapping commandNameMapping;

    public CommandProducerImpl(MessageProducer messageProducer, CommandNameMapping commandNameMapping) {
        this.messageProducer = messageProducer;
        this.commandNameMapping = commandNameMapping;
    }

    @Override
    public String send(String channel, Command command, String replyTo, Map<String, String> headers) {
        return send(channel, null, command, replyTo, headers);
    }

    @Override
    public String sendNotification(String channel, Command command, Map<String, String> headers) {
        return send(channel, null, command, null, headers);
    }

    @Override
    public String send(String channel, String resource, Command command, String replyTo, Map<String, String> headers) {
        Message message = makeMessage(commandNameMapping, channel, resource, command, replyTo, headers);
        messageProducer.send(channel, message);
        return message.getId();
    }
}
