package org.cresplanex.core.consumer.common;

import java.util.List;

import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerBaseCommonConfiguration {

  @Autowired(required=false)
  private final MessageInterceptor[] messageInterceptors = new MessageInterceptor[0];

  @Bean
  public DecoratedMessageHandlerFactory subscribedMessageHandlerChainFactory(List<MessageHandlerDecorator> decorators) {
    return new DecoratedMessageHandlerFactory(decorators);
  }

  @Bean
  public PrePostReceiveMessageHandlerDecorator prePostReceiveMessageHandlerDecoratorDecorator() {
    return new PrePostReceiveMessageHandlerDecorator(messageInterceptors);
  }

  @Bean
  public DuplicateDetectingMessageHandlerDecorator duplicateDetectingMessageHandlerDecorator(DuplicateMessageDetector duplicateMessageDetector) {
    return new DuplicateDetectingMessageHandlerDecorator(duplicateMessageDetector);
  }

  @Bean
  public PrePostHandlerMessageHandlerDecorator prePostHandlerMessageHandlerDecorator() {
    return new PrePostHandlerMessageHandlerDecorator(messageInterceptors);
  }
}
