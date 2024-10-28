package org.cresplanex.core.saga.orchestration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collection;

import org.cresplanex.core.saga.common.SagaLockManager;
import org.cresplanex.core.commands.producer.CommandProducer;
import org.cresplanex.core.commands.producer.CommandProducerConfiguration;
import org.cresplanex.core.common.id.ApplicationIdGenerator;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.messaging.consumer.MessageConsumer;
import org.cresplanex.core.saga.common.CoreSagaCommonConfiguration;

@Configuration
@Import({CommandProducerConfiguration.class, CoreSagaCommonConfiguration.class})
public class SagaOrchestratorConfiguration {

    @Bean
    public SagaInstanceRepository sagaInstanceRepository(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSchema coreSchema) {
        return new SagaInstanceRepositoryJdbc(coreJdbcStatementExecutor, new ApplicationIdGenerator(), coreSchema);
    }

    @Bean
    public SagaCommandProducer sagaCommandProducer(CommandProducer commandProducer) {
        return new SagaCommandProducerImpl(commandProducer);
    }

    @Bean
    public SagaInstanceFactory sagaInstanceFactory(SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager, SagaCommandProducer sagaCommandProducer, Collection<Saga<?>> sagas) {
        SagaManagerFactory smf = new SagaManagerFactory(sagaInstanceRepository, commandProducer, messageConsumer,
                sagaLockManager, sagaCommandProducer);
        return new SagaInstanceFactory(smf, sagas);
    }
}
