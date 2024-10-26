package org.cresplanex.core.messaging.kafka.consumer;

public class KafkaMessage {

    private final String payload;

    public KafkaMessage(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}
