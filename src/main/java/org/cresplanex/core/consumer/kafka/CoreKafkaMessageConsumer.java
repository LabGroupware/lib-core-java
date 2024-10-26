package org.cresplanex.core.consumer.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.consumer.common.MessageConsumerImplementation;
import org.cresplanex.core.messaging.common.MessageImpl;
import org.cresplanex.core.messaging.consumer.MessageHandler;
import org.cresplanex.core.messaging.consumer.MessageSubscription;
import org.cresplanex.core.messaging.kafka.consumer.KafkaSubscription;
import org.cresplanex.core.messaging.kafka.consumer.MessageConsumerKafkaImpl;

public class CoreKafkaMessageConsumer implements MessageConsumerImplementation {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MessageConsumerKafkaImpl messageConsumerKafka;

    public CoreKafkaMessageConsumer(MessageConsumerKafkaImpl messageConsumerKafka) {
        this.messageConsumerKafka = messageConsumerKafka;
    }

    @Override
    public MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler) {
        logger.info("Subscribing: subscriberId = {}, channels = {}", subscriberId, channels);

        KafkaSubscription subscription = messageConsumerKafka.subscribe(subscriberId,
                channels, message -> handler.accept(JSonMapper.fromJson(message.getPayload(), MessageImpl.class)));

        logger.info("Subscribed: subscriberId = {}, channels = {}", subscriberId, channels);

        return subscription::close;
    }

    @Override
    public String getId() {
        return messageConsumerKafka.getId();
    }

    @Override
    public void close() {
        logger.info("Closing consumer");

        messageConsumerKafka.close();

        logger.info("Closed consumer");
    }
}
