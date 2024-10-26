package org.cresplanex.core.messaging.kafka.basic.consumer;

public class KafkaMessageProcessorFailedException extends RuntimeException {

    public KafkaMessageProcessorFailedException(Throwable t) {
        super(t);
    }
}
