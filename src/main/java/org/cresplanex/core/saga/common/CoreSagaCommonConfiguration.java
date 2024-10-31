package org.cresplanex.core.saga.common;

// import org.cresplanex.core.common.jdbc.CoreCommonJdbcOperationsConfiguration;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Import;

@Configuration
// @Import(CoreCommonJdbcOperationsConfiguration.class)
public class CoreSagaCommonConfiguration {

    @Bean
    public SagaLockManager sagaLockManager(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSchema coreSchema) {
        return new SagaLockManagerImpl(coreJdbcStatementExecutor, coreSchema);
    }
}
