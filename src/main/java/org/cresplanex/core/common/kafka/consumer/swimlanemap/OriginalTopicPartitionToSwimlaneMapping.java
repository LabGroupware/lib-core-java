package org.cresplanex.core.common.kafka.consumer.swimlanemap;

import org.apache.kafka.common.TopicPartition;

/**
 * トピックパーティションのパーティションIDをそのままスイムレーンとして使用するクラスです。
 */
public class OriginalTopicPartitionToSwimlaneMapping implements TopicPartitionToSwimlaneMapping {

    /**
     * 指定されたトピックパーティションに対応するスイムレーンを取得します。
     *
     * @param topicPartition トピックパーティション
     * @param messageKey メッセージキー
     * @return トピックパーティションのパーティションID
     */
    @Override
    public Integer toSwimlane(TopicPartition topicPartition, String messageKey) {
        return topicPartition.partition();
    }
}
