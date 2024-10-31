package org.cresplanex.core.common.kafka.baskpressure;

import org.apache.kafka.common.TopicPartition;

import java.util.Set;

/**
 * 通常状態におけるバックプレッシャー管理状態クラス。
 * <p>
 * 負荷が増大した場合には一時停止状態に移行します。
 * </p>
 */
public class BackPressureManagerNormalState implements BackPressureManagerState {

    /**
     * 一時停止されたパーティションを使用して通常状態へ遷移します。
     *
     * @param suspendedPartitions 一時停止されたトピックパーティションのセット
     * @return 通常状態と再開アクションを含む状態およびアクションのペア
     */
    public static BackPressureManagerStateAndActions transitionTo(Set<TopicPartition> suspendedPartitions) {
        return new BackPressureManagerStateAndActions(BackPressureActions.resume(suspendedPartitions), new BackPressureManagerNormalState());
    }

    @Override
    public BackPressureManagerStateAndActions update(Set<TopicPartition> allTopicPartitions, int backlog, BackPressureConfig backPressureConfig) {
        if (backlog > backPressureConfig.getHigh()) { // しきい値を超えた場合
            return BackPressureManagerPausedState.transitionTo(allTopicPartitions); // 一時停止状態へ遷移
        } else {
            return new BackPressureManagerStateAndActions(this); // 通常状態を維持
        }
    }
}
