package org.cresplanex.core.common.jdbc;

import org.cresplanex.core.common.jdbc.sqldialect.CoreSqlDialect;
import org.cresplanex.core.common.jdbc.sqldialect.SqlDialectConfiguration;
import org.cresplanex.core.common.jdbc.sqldialect.SqlDialectSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@Import({CoreSchemaConfiguration.class,
    SqlDialectConfiguration.class,
    CoreTransactionTemplateConfiguration.class})
public class CoreCommonJdbcOperationsConfiguration {

    @Autowired(required = false)
    private final OutboxPartitioningSpec outboxPartitioningSpec = OutboxPartitioningSpec.DEFAULT;

    @Bean
    public CoreJdbcStatementExecutor coreJdbcStatementExecutor(JdbcTemplate jdbcTemplate) {
        return new CoreSpringJdbcStatementExecutor(jdbcTemplate);
    }

    @Bean
    public CoreCommonJdbcOperations coreCommonJdbcOperations(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            SqlDialectSelector sqlDialectSelector,
            @Value("${spring.datasource.driver-class-name}") String driver) {
        CoreSqlDialect coreSqlDialect = sqlDialectSelector.getDialect(driver);

        return new CoreCommonJdbcOperations(new CoreJdbcOperationsUtils(coreSqlDialect),
                coreJdbcStatementExecutor, coreSqlDialect, outboxPartitioningSpec);
    }
}
