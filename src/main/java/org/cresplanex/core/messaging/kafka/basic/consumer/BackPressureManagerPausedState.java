package org.cresplanex.core.messaging.kafka.basic.consumer;

import org.apache.kafka.common.TopicPartition;

import java.util.HashSet;
import java.util.Set;

/**
 * 一時停止状態におけるバックプレッシャー管理状態クラス。
 * <p>
 * 負荷が低下した場合に通常状態へと遷移します。
 * </p>
 */
public class BackPressureManagerPausedState implements BackPressureManagerState {

    /** 一時停止中のトピックパーティションのセット */
    private final Set<TopicPartition> suspendedPartitions;

    /**
     * 一時停止するトピックパーティションで初期化します。
     *
     * @param pausedTopic 一時停止対象のトピックパーティション
     */
    public BackPressureManagerPausedState(Set<TopicPartition> pausedTopic) {
        this.suspendedPartitions = new HashSet<>(pausedTopic);
    }

    /**
     * 指定されたトピックパーティションで一時停止状態へ遷移します。
     *
     * @param allTopicPartitions 全トピックパーティションのセット
     * @return 一時停止状態と一時停止アクションを含む状態およびアクションのペア
     */
    public static BackPressureManagerStateAndActions transitionTo(Set<TopicPartition> allTopicPartitions) {
        return new BackPressureManagerStateAndActions(BackPressureActions.pause(allTopicPartitions), new BackPressureManagerPausedState(allTopicPartitions));
    }

    @Override
    public BackPressureManagerStateAndActions update(Set<TopicPartition> allTopicPartitions, int backlog, BackPressureConfig backPressureConfig) {
        if (backlog <= backPressureConfig.getLow()) { // しきい値を下回った場合
            return BackPressureManagerNormalState.transitionTo(suspendedPartitions); // 停止中のパーティションを再開して通常状態へ遷移
        } else {
            Set<TopicPartition> toSuspend = new HashSet<>(allTopicPartitions);
            toSuspend.removeAll(suspendedPartitions);
            suspendedPartitions.addAll(toSuspend); // 新たに一時停止するパーティションを追加
            return new BackPressureManagerStateAndActions(BackPressureActions.pause(toSuspend), this); // 追加したパーティションを一時停止
        }
    }
}
