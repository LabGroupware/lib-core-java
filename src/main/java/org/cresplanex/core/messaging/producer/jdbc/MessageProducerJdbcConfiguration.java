package org.cresplanex.core.messaging.producer.jdbc;

import org.cresplanex.core.common.id.IdGenerator;
import org.cresplanex.core.common.id.IdGeneratorConfiguration;
import org.cresplanex.core.common.jdbc.CoreCommonJdbcOperations;
import org.cresplanex.core.common.jdbc.CoreCommonJdbcOperationsConfiguration;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.jdbc.OutboxPartitioningSpec;
import org.cresplanex.core.common.jdbc.sqldialect.SqlDialectConfiguration;
import org.cresplanex.core.messaging.producer.MessageProducerImplementation;
import org.cresplanex.core.messaging.producer.MessagingCommonProducerConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SqlDialectConfiguration.class,
    MessagingCommonProducerConfiguration.class,
    CoreCommonJdbcOperationsConfiguration.class,
    IdGeneratorConfiguration.class})
public class MessageProducerJdbcConfiguration {

    @Bean
    public OutboxPartitioningSpec outboxPartitioningSpec(@Value("${core.outbox.partitioning.outbox.tables:#{null}}") Integer outboxTables,
            @Value("${core.outbox.partitioning.message.partitions:#{null}}") Integer outboxTablePartitions) {
        return new OutboxPartitioningSpec(outboxTables, outboxTablePartitions);
    }

    @Bean
    @ConditionalOnMissingBean(MessageProducerImplementation.class)
    public MessageProducerImplementation messageProducerImplementation(CoreCommonJdbcOperations coreCommonJdbcOperations,
            IdGenerator idGenerator,
            CoreSchema coreSchema) {
        return new MessageProducerJdbcImpl(coreCommonJdbcOperations,
                idGenerator,
                coreSchema);
    }
}
