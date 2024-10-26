package org.cresplanex.core.consumer.common;

import java.util.Set;

import org.cresplanex.core.messaging.consumer.MessageHandler;
import org.cresplanex.core.messaging.consumer.MessageSubscription;

public interface MessageConsumerImplementation {

    MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);

    String getId();

    void close();
}
