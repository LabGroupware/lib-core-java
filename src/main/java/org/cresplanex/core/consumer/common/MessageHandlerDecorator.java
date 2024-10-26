package org.cresplanex.core.consumer.common;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

import java.util.function.BiConsumer;

public interface MessageHandlerDecorator extends BiConsumer<SubscriberIdAndMessage, MessageHandlerDecoratorChain> {
  int getOrder();
}
