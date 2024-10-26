package org.cresplanex.core.messaging.producer;

import java.util.Arrays;
import java.util.UUID;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageProducerImpl implements MessageProducer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageInterceptor[] messageInterceptors;
    private final ChannelMapping channelMapping;
    private final MessageProducerImplementation implementation;

    public MessageProducerImpl(MessageInterceptor[] messageInterceptors, ChannelMapping channelMapping, MessageProducerImplementation implementation) {
        this.messageInterceptors = messageInterceptors;
        this.channelMapping = channelMapping;
        this.implementation = implementation;
    }

    private void preSend(Message message) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.preSend(message));
    }

    private void postSend(Message message, RuntimeException e) {
        Arrays.stream(messageInterceptors).forEach(mi -> mi.postSend(message, e));
    }

    @Override
    public void send(String destination, Message message) {
        prepareMessageHeaders(destination, message);
        implementation.withContext(() -> send(message));
    }

    protected void prepareMessageHeaders(String destination, Message message) {
        implementation.setMessageIdIfNecessary(message);
        message.getHeaders().put(Message.DESTINATION, channelMapping.transform(destination));
        message.getHeaders().put(Message.DATE, HttpDateHeaderFormatUtil.nowAsHttpDateString());
        if (message.getHeaders().get(Message.PARTITION_ID) == null) {
            message.getHeaders().put(Message.PARTITION_ID, UUID.randomUUID().toString());
        }
    }

    protected void send(Message message) {
        preSend(message);
        try {
            implementation.send(message);
            postSend(message, null);
        } catch (RuntimeException e) {
            logger.error("Sending failed", e);
            postSend(message, e);
            throw e;
        }
    }
}
