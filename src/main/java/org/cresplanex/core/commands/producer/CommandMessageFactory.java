package org.cresplanex.core.commands.producer;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;

public class CommandMessageFactory {

    public static Message makeMessage(CommandNameMapping commandNameMapping, String channel, Command command, String replyTo, Map<String, String> headers) {
        return makeMessage(commandNameMapping, channel, null, command, replyTo, headers);
    }

    public static Message makeMessage(CommandNameMapping commandNameMapping, String channel, String resource, Command command, String replyTo, Map<String, String> headers) {
        MessageBuilder builder = MessageBuilder.withPayload(JSonMapper.toJson(command))
                .withExtraHeaders("", headers) // TODO should these be prefixed??!
                .withHeader(CommandMessageHeaders.DESTINATION, channel)
                .withHeader(CommandMessageHeaders.COMMAND_TYPE, commandNameMapping.commandToExternalCommandType(command));

        if (replyTo != null) {
            builder.withHeader(CommandMessageHeaders.REPLY_TO, replyTo);
        }

        if (resource != null) {
            builder.withHeader(CommandMessageHeaders.RESOURCE, resource);
        }

        return builder.build();
    }
}
