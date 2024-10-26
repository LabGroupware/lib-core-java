package org.cresplanex.core.logging;

import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class LoggingMessageInterceptorAutoConfiguration {

    @Bean
    public MessageInterceptor messageLoggingInterceptor() {
        return new LoggingMessageInterceptor();
    }
}
