package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface CoreKafkaConsumerMessageHandler
        extends BiFunction<ConsumerRecord<String, byte[]>, BiConsumer<Void, Throwable>, MessageConsumerBacklog> {
}
