package org.cresplanex.core.saga.participant;

import org.cresplanex.core.commands.common.CommandNameMapping;
import org.cresplanex.core.commands.consumer.CommandReplyProducer;
import org.cresplanex.core.commands.consumer.CommandReplyProducerConfiguration;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.common.CoreSagaCommonConfiguration;
import org.cresplanex.core.saga.common.SagaLockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreSagaCommonConfiguration.class, CommandReplyProducerConfiguration.class})
public class SagaParticipantConfiguration {

    @Bean
    public SagaCommandDispatcherFactory sagaCommandDispatcherFactory(MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            CommandNameMapping commandNameMapping,
            CommandReplyProducer commandReplyProducer) {
        return new SagaCommandDispatcherFactory(messageConsumer, sagaLockManager, commandNameMapping, commandReplyProducer);
    }

}
