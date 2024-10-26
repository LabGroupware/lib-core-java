package org.cresplanex.core.commands.consumer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.common.paths.ResourcePath;
import org.cresplanex.core.commands.common.paths.ResourcePathPattern;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;

public class CommandHandlerParams {

    private final Object command;
    private final Map<String, String> correlationHeaders;
    private final Map<String, String> pathVars;
    private final Optional<String> defaultReplyChannel;

    public CommandHandlerParams(Message message, Class<?> commandClass, Optional<String> resource) {
        command = JSonMapper.fromJson(message.getPayload(), commandClass);
        pathVars = getPathVars(message, resource);
        correlationHeaders = getCorrelationHeaders(message.getHeaders());
        defaultReplyChannel = message.getHeader(CommandMessageHeaders.REPLY_TO);
    }

    public Object getCommand() {
        return command;
    }

    public Map<String, String> getCorrelationHeaders() {
        return correlationHeaders;
    }

    public Map<String, String> getPathVars() {
        return pathVars;
    }

    public Optional<String> getDefaultReplyChannel() {
        return defaultReplyChannel;
    }

    private Map<String, String> getCorrelationHeaders(Map<String, String> headers) {
        Map<String, String> m = headers.entrySet()
                .stream()
                .filter(e -> e.getKey().startsWith(CommandMessageHeaders.COMMAND_HEADER_PREFIX))
                .collect(Collectors.toMap(e -> CommandMessageHeaders.inReply(e.getKey()),
                        Map.Entry::getValue));
        m.put(ReplyMessageHeaders.IN_REPLY_TO, headers.get(Message.ID));
        return m;
    }

    private Map<String, String> getPathVars(Message message, Optional<String> handlerResource) {
        return handlerResource.flatMap(res -> {
            ResourcePathPattern r = ResourcePathPattern.parse(res);
            return message.getHeader(CommandMessageHeaders.RESOURCE).map(h -> {
                ResourcePath mr = ResourcePath.parse(h);
                return r.getPathVariableValues(mr);
            });
        }).orElse(Collections.emptyMap());

        //   return handlerResource.flatMap(res -> {
        //     ResourcePathPattern r = ResourcePathPattern.parse(res);
        //     return message.getHeader(CommandMessageHeaders.RESOURCE).map(h -> {
        //         ResourcePath mr = ResourcePath.parse(h);
        //         return r.getPathVariableValues(mr);
        //     });
        // }).orElse(EMPTY_MAP);
    }
}
