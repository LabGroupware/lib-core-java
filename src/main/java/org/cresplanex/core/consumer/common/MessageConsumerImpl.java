package org.cresplanex.core.consumer.common;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.cresplanex.core.messaging.common.ChannelMapping;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.messaging.consumer.MessageHandler;
import org.cresplanex.core.messaging.consumer.MessageSubscription;
import org.cresplanex.core.messaging.consumer.SubscriberMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MessageConsumerImpl implements MessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // This could be implemented as Around advice
    private final ChannelMapping channelMapping;
    private final MessageConsumerImplementation target;
    private final DecoratedMessageHandlerFactory decoratedMessageHandlerFactory;
    private final SubscriberMapping subscriberMapping;

    public MessageConsumerImpl(ChannelMapping channelMapping,
            MessageConsumerImplementation target,
            DecoratedMessageHandlerFactory decoratedMessageHandlerFactory,
            SubscriberMapping subscriberMapping) {
        this.channelMapping = channelMapping;
        this.target = target;
        this.decoratedMessageHandlerFactory = decoratedMessageHandlerFactory;
        this.subscriberMapping = subscriberMapping;
    }

    @Override
    public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {
        logger.info("Subscribing: subscriberId = {}, channels = {}", subscriberId, channels);

        Consumer<SubscriberIdAndMessage> decoratedHandler = decoratedMessageHandlerFactory.decorate(handler);

        MessageSubscription messageSubscription = target.subscribe(subscriberMapping.toExternal(subscriberId),
                channels.stream().map(channelMapping::transform).collect(Collectors.toSet()),
                message -> decoratedHandler.accept(new SubscriberIdAndMessage(subscriberId, message)));

        logger.info("Subscribed: subscriberId = {}, channels = {}", subscriberId, channels);

        return messageSubscription;
    }

    @Override
    public String getId() {
        return target.getId();
    }

    @Override
    public void close() {
        logger.info("Closing consumer");

        target.close();

        logger.info("Closed consumer");
    }

}
