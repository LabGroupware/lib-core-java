package org.cresplanex.core.consumer.common;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

public interface MessageHandlerDecoratorChain {
  void invokeNext(SubscriberIdAndMessage subscriberIdAndMessage);
}
