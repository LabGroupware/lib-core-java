package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.messaging.consumer.duplicate.DuplicateMessageDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class ReplyExceptionHandleDecoratorConfiguration {

    private final CommandReplyProducer commandReplyProducer;
    private final DuplicateMessageDetector duplicateMessageDetector;

    public ReplyExceptionHandleDecoratorConfiguration(CommandReplyProducer commandReplyProducer, DuplicateMessageDetector duplicateMessageDetector) {
        this.commandReplyProducer = commandReplyProducer;
        this.duplicateMessageDetector = duplicateMessageDetector;
    }

    @Bean("org.cresplanex.core.messaging.consumer.decorator.ReplyExceptionHandleDecorator")
    public ReplyExceptionHandleDecorator replyExceptionHandleDecorator() {
        return new ReplyExceptionHandleDecorator(commandReplyProducer, duplicateMessageDetector);
    }
}
