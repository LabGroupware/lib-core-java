package org.cresplanex.core.messaging.producer;

import org.cresplanex.core.messaging.common.Message;

public interface MessageProducerImplementation {

    void send(Message message);

    default void setMessageIdIfNecessary(Message message) {
        //do nothing by default
    }

    default void withContext(Runnable runnable) {
        runnable.run();
    }
}
