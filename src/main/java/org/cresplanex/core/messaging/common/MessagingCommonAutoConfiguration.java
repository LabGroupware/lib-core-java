package org.cresplanex.core.messaging.common;

import org.cresplanex.core.messaging.consumer.DefaultSubscriberMapping;
import org.cresplanex.core.messaging.consumer.SubscriberMapping;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MessagingCommonAutoConfiguration {

  // デフォルトのチャネルマッピングを提供する
  @ConditionalOnMissingBean(ChannelMapping.class)
  @Bean
  public ChannelMapping channelMapping() {
    return new DefaultChannelMapping.DefaultChannelMappingBuilder().build();
  }

  // デフォルトのサブスクライバーマッピングを提供する
  @ConditionalOnMissingBean(SubscriberMapping.class)
  @Bean
  public SubscriberMapping subscriberMapping() {
    return new DefaultSubscriberMapping();
  }
}
