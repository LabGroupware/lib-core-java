package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.LinkedBlockingQueue;

import org.cresplanex.core.messaging.kafka.basic.consumer.MessageConsumerBacklog;

public class SwimlaneDispatcherBacklog implements MessageConsumerBacklog {

    private final LinkedBlockingQueue<?> queue;

    public SwimlaneDispatcherBacklog(LinkedBlockingQueue<?> queue) {
        this.queue = queue;
    }

    @Override
    public int size() {
        return queue.size();
    }
}
