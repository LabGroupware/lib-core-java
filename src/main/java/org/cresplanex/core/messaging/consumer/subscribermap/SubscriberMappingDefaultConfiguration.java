package org.cresplanex.core.messaging.consumer.subscribermap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SubscriberMappingDefaultConfiguration {
  // デフォルトのサブスクライバーマッピングを提供する
  @Bean("org.cresplanex.core.messaging.consumer.subscribermap.SubscriberMapping")
  public SubscriberMapping subscriberMapping() {
    return new DefaultSubscriberMapping();
  }
}
