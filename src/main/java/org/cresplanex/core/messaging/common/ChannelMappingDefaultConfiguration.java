package org.cresplanex.core.messaging.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChannelMappingDefaultConfiguration {

    // デフォルトのチャネルマッピングを提供する
    @Bean
    public ChannelMapping channelMappingDefault() {
        return new DefaultChannelMapping.DefaultChannelMappingBuilder().build();
    }
}
