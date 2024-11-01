package org.cresplanex.core.saga.lock;

import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SagaLockManagerJdbc の設定クラス。 JDBC を用いた Saga のロック管理を構成します。
 */
@Configuration
public class SagaLockManagerJdbcConfiguration {

    /**
     * SagaLockManager の Bean を作成します。
     *
     * @param coreJdbcStatementExecutor JDBC ステートメントエグゼキューター
     * @param coreSchema データベーススキーマ
     * @return SagaLockManagerJdbc のインスタンス
     */
    @Bean
    public SagaLockManager sagaLockManager(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSchema coreSchema) {
        return new SagaLockManagerJdbc(coreJdbcStatementExecutor, coreSchema);
    }
}
