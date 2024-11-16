package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class ReplyExceptionHandleDecoratorConfiguration {

    private final CommandReplyProducer commandReplyProducer;

    public ReplyExceptionHandleDecoratorConfiguration(CommandReplyProducer commandReplyProducer) {
        this.commandReplyProducer = commandReplyProducer;
    }

    @Bean("org.cresplanex.core.messaging.consumer.decorator.ReplyExceptionHandleDecorator")
    public ReplyExceptionHandleDecorator replyExceptionHandleDecorator() {
        return new ReplyExceptionHandleDecorator(commandReplyProducer);
    }
}
