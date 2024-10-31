package org.cresplanex.core.common.kafka.baskpressure;

import java.util.HashSet;
import java.util.Set;

import org.apache.kafka.common.TopicPartition;

/**
 * バックプレッシャーの管理を行うクラス。
 * <p>
 * Kafkaの消費者の負荷状況に応じて、トピックパーティションの一時停止や再開を制御します。
 * </p>
 */
public class BackPressureManager {

    /** バックプレッシャー設定 */
    private final BackPressureConfig backPressureConfig;

    /** 全トピックパーティションのセット */
    private final Set<TopicPartition> allTopicPartitions = new HashSet<>();

    /** 現在のバックプレッシャー管理状態 */
    private BackPressureManagerState state = new BackPressureManagerNormalState();

    /**
     * 指定されたバックプレッシャー設定で初期化します。
     *
     * @param backPressureConfig バックプレッシャー設定
     */
    public BackPressureManager(BackPressureConfig backPressureConfig) {
        this.backPressureConfig = backPressureConfig;
    }

    /**
     * 現在の負荷状況に基づき、バックプレッシャーアクションを更新します。
     *
     * @param topicPartitions 更新対象のトピックパーティション
     * @param backlog 現在のバックログの数
     * @return 新しいバックプレッシャーアクション
     */
    public BackPressureActions update(Set<TopicPartition> topicPartitions, int backlog) {
        allTopicPartitions.addAll(topicPartitions);
        BackPressureManagerStateAndActions stateAndActions = state.update(allTopicPartitions, backlog, backPressureConfig);
        this.state = stateAndActions.state;
        return stateAndActions.actions;
    }
}
