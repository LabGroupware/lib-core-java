package org.cresplanex.core.consumer.jdbc;

import org.cresplanex.core.common.jdbc.CoreCommonJdbcOperationsConfiguration;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.jdbc.CoreTransactionTemplate;
import org.cresplanex.core.common.jdbc.sqldialect.SqlDialectSelector;
import org.cresplanex.core.consumer.common.DuplicateMessageDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(CoreCommonJdbcOperationsConfiguration.class)
@ConditionalOnMissingBean(DuplicateMessageDetector.class)
public class ConsumerJdbcAutoConfiguration {

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    public DuplicateMessageDetector duplicateMessageDetector(CoreSchema coreSchema,
            SqlDialectSelector sqlDialectSelector,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreTransactionTemplate coreTransactionTemplate) {
        return new SqlTableBasedDuplicateMessageDetector(coreSchema,
                sqlDialectSelector.getDialect(driver).getCurrentTimeInMillisecondsExpression(),
                coreJdbcStatementExecutor,
                coreTransactionTemplate);
    }

}
