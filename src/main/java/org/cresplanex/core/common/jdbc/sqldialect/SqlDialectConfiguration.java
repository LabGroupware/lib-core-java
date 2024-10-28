package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SQL方言の設定クラス。
 * <p>
 * 各SQL方言のBeanを作成し、SQL方言セレクターを作成します。
 * </p>
 */
@Configuration
public class SqlDialectConfiguration {

    /**
     * MySQL用のSQL方言Beanを作成します。
     *
     * @return MySQL用のSQL方言Bean
     */
    @Bean
    public MySqlDialect mySqlDialect() {
        return new MySqlDialect();
    }

    /**
     * PostgreSQL用のSQL方言Beanを作成します。
     *
     * @return PostgreSQL用のSQL方言Bean
     */
    @Bean
    public PostgresDialect postgreSQLDialect() {
        return new PostgresDialect();
    }

    /**
     * SQL Server用のSQL方言Beanを作成します。
     *
     * @return SQL Server用のSQL方言Bean
     */
    @Bean
    public MsSqlDialect msSqlDialect() {
        return new MsSqlDialect();
    }

    /**
     * default用のSQL方言Beanを作成します。
     *
     * @param customCurrentTimeInMillisecondsExpression
     * @return
     */
    @Bean
    public DefaultCoreSqlDialect defaultSqlDialect(@Value("${core.current.time.in.milliseconds.sql:#{null}}") String customCurrentTimeInMillisecondsExpression) {
        return new DefaultCoreSqlDialect(customCurrentTimeInMillisecondsExpression);
    }

    /**
     * SQL方言セレクターBeanを作成します。
     *
     * @param dialects SQL方言のコレクション
     * @return SQL方言セレクターBean
     */
    @Bean
    public SqlDialectSelector sqlDialectSelector(Collection<CoreSqlDialect> dialects) {
        return new SqlDialectSelector(dialects);
    }
}
