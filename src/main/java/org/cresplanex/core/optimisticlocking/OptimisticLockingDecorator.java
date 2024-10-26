package org.cresplanex.core.optimisticlocking;

import org.cresplanex.core.consumer.common.MessageHandlerDecorator;
import org.cresplanex.core.consumer.common.MessageHandlerDecoratorChain;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.springframework.core.Ordered;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class OptimisticLockingDecorator implements MessageHandlerDecorator, Ordered {

    @Override
    @Retryable(retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 100))
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
    }

    @Override
    public int getOrder() {
        return 150;
    }
}
