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

/**
 * JDBC操作のための設定クラス。
 * <p>
 * 必要なコンポーネント（スキーマ設定、SQL方言設定、トランザクションテンプレート設定）を インポートし、JDBC操作用のBeanを作成します。
 * </p>
 */
@Configuration
@Import({CoreSchemaConfiguration.class,
    SqlDialectConfiguration.class,
    CoreTransactionTemplateConfiguration.class})
public class CoreCommonJdbcOperationsConfiguration {

    /**
     * デフォルトのアウトボックスパーティショニング仕様を使用するオプション設定。
     */
    @Autowired(required = false)
    private final OutboxPartitioningSpec outboxPartitioningSpec = OutboxPartitioningSpec.DEFAULT;

    /**
     * {@link CoreJdbcStatementExecutor} のBeanを作成します。
     *
     * @param jdbcTemplate SpringのJdbcTemplateインスタンス
     * @return CoreJdbcStatementExecutorインスタンス
     */
    @Bean
    public CoreJdbcStatementExecutor coreJdbcStatementExecutor(JdbcTemplate jdbcTemplate) {
        return new CoreSpringJdbcStatementExecutor(jdbcTemplate);
    }

    /**
     * {@link CoreCommonJdbcOperations} のBeanを作成します。
     *
     * @param coreJdbcStatementExecutor ステートメント実行オブジェクト
     * @param sqlDialectSelector SQL方言のセレクター
     * @param driver データソースのドライバクラス名（プロパティから取得）
     * @return CoreCommonJdbcOperationsインスタンス
     */
    @Bean
    public CoreCommonJdbcOperations coreCommonJdbcOperations(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            SqlDialectSelector sqlDialectSelector,
            @Value("${spring.datasource.driver-class-name}") String driver) {
        CoreSqlDialect coreSqlDialect = sqlDialectSelector.getDialect(driver);

        return new CoreCommonJdbcOperations(new CoreJdbcOperationsUtils(coreSqlDialect),
                coreJdbcStatementExecutor, coreSqlDialect, outboxPartitioningSpec);
    }
}
