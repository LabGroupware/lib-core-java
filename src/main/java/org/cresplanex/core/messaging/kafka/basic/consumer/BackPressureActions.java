package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.Collections;
import java.util.Set;

import org.apache.kafka.common.TopicPartition;

/**
 * バックプレッシャー（負荷制御）に基づくアクションを表現するクラス。
 * <p>
 * Kafkaの消費者に対して、指定されたトピックパーティションを一時停止または再開する操作を定義します。
 * </p>
 */
public class BackPressureActions {

    /** 一時停止するトピックパーティションのセット */
    public final Set<TopicPartition> pause;

    /** 再開するトピックパーティションのセット */
    public final Set<TopicPartition> resume;

    /**
     * 指定されたトピックパーティションのセットで初期化します。
     *
     * @param pause 一時停止するトピックパーティション
     * @param resume 再開するトピックパーティション
     */
    public BackPressureActions(Set<TopicPartition> pause, Set<TopicPartition> resume) {
        this.pause = pause;
        this.resume = resume;
    }

    /** バックプレッシャーアクションが存在しない場合を示す定数 */
    public static final BackPressureActions NONE = new BackPressureActions(Collections.emptySet(), Collections.emptySet());

    /**
     * 指定されたトピックパーティションを一時停止するアクションを生成します。
     *
     * @param topicPartitions 一時停止するトピックパーティションのセット
     * @return 一時停止アクション
     */
    public static BackPressureActions pause(Set<TopicPartition> topicPartitions) {
        return new BackPressureActions(topicPartitions, Collections.emptySet());
    }

    /**
     * 指定されたトピックパーティションを再開するアクションを生成します。
     *
     * @param topicPartitions 再開するトピックパーティションのセット
     * @return 再開アクション
     */
    public static BackPressureActions resume(Set<TopicPartition> topicPartitions) {
        return new BackPressureActions(Collections.emptySet(), topicPartitions);
    }
}
