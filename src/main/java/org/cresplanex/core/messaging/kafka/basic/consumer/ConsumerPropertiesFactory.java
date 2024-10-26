package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.Properties;

public class ConsumerPropertiesFactory {

    public static Properties makeDefaultConsumerProperties(String bootstrapServers, String subscriberId) {
        Properties consumerProperties = new Properties();
        consumerProperties.put("bootstrap.servers", bootstrapServers);
        consumerProperties.put("group.id", subscriberId);
        consumerProperties.put("enable.auto.commit", "false");
        consumerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
        consumerProperties.put("auto.offset.reset", "earliest");
        return consumerProperties;
    }
}
