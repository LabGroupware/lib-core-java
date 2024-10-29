package org.cresplanex.core.messaging.kafka.basic.consumer;

import org.apache.kafka.common.TopicPartition;

import java.util.Set;

/**
 * バックプレッシャー管理状態を表現するインターフェース。
 * <p>
 * Kafkaコンシューマの負荷状況に応じて、状態とアクションを更新するためのメソッドを提供します。
 * </p>
 */
public interface BackPressureManagerState {

    /**
     * バックプレッシャー状態を更新し、適切なアクションを取得します。
     *
     * @param allTopicPartitions 全トピックパーティションのセット
     * @param backlog 現在のバックログの数
     * @param backPressureConfig バックプレッシャー設定
     * @return 更新後の状態とアクションのペア
     */
    BackPressureManagerStateAndActions update(Set<TopicPartition> allTopicPartitions, int backlog, BackPressureConfig backPressureConfig);
}
