package org.cresplanex.core.common.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * アウトボックスパーティショニング仕様の設定クラス。
 * <p>
 * アウトボックスパーティショニング仕様を設定し、Beanを作成します。
 * </p>
 */
@Configuration
public class OutboxPartitioningSpecConfiguration {

    /**
     * アウトボックスパーティショニング仕様のBeanを作成します。
     * <p>
     * アプリケーション設定からアウトボックステーブル数とメッセージパーティション数を取得し、
     * {@link OutboxPartitioningSpec} のインスタンスを生成します。
     * </p>
     *
     * @param outboxTables
     * @param outboxTablePartitions
     * @return
     */
    @Bean
    public OutboxPartitioningSpec outboxPartitioningSpec(@Value("${core.outbox.partitioning.outbox.tables:#{null}}") Integer outboxTables,
            @Value("${core.outbox.partitioning.message.partitions:#{null}}") Integer outboxTablePartitions) {
        return new OutboxPartitioningSpec(outboxTables, outboxTablePartitions);
    }
}
