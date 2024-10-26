package org.cresplanex.core.consumer.common;

import java.util.Arrays;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.cresplanex.core.messaging.consumer.BuiltInMessageHandlerDecoratorOrder;

public class PrePostReceiveMessageHandlerDecorator implements MessageHandlerDecorator {

  // private final Logger logger = LoggerFactory.getLogger(getClass());
  private final MessageInterceptor[] messageInterceptors;

  public PrePostReceiveMessageHandlerDecorator(MessageInterceptor[] messageInterceptors) {
    this.messageInterceptors = messageInterceptors;
  }

  @Override
  public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
    Message message = subscriberIdAndMessage.getMessage();
    preReceive(message);
    try {
      messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
    } finally {
      postReceive(message);
    }
  }

  private void preReceive(Message message) {
    Arrays.stream(messageInterceptors).forEach(mi -> mi.preReceive(message));
  }


  private void postReceive(Message message) {
    Arrays.stream(messageInterceptors).forEach(mi -> mi.postReceive(message));
  }

  @Override
  public int getOrder() {
    return BuiltInMessageHandlerDecoratorOrder.PRE_POST_RECEIVE_MESSAGE_HANDLER_DECORATOR;
  }
}
