package org.cresplanex.core.common.kafka.consumer.swimlanemap;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.common.TopicPartition;

/**
 * トピックパーティションごとに複数のスイムレーンをマッピングするクラスです。 メッセージキーのハッシュ値を使用してスイムレーンを分散させます。
 */
public class MultipleSwimlanesPerTopicPartitionMapping implements TopicPartitionToSwimlaneMapping {

    private final int swimlanesPerTopicPartition;
    private final ConcurrentHashMap<TopicPartition, Integer> mapping = new ConcurrentHashMap<>();

    /**
     * 指定されたトピックパーティションごとのスイムレーン数でマッピングを初期化します。
     *
     * @param swimlanesPerTopicPartition 各トピックパーティションに割り当てるスイムレーンの数
     */
    public MultipleSwimlanesPerTopicPartitionMapping(int swimlanesPerTopicPartition) {
        this.swimlanesPerTopicPartition = swimlanesPerTopicPartition;
    }

    /**
     * 指定されたトピックパーティションとメッセージキーに基づいてスイムレーンを取得します。
     *
     * @param topicPartition トピックパーティション
     * @param messageKey メッセージキー
     * @return スイムレーンのインデックス
     */
    @Override
    public Integer toSwimlane(TopicPartition topicPartition, String messageKey) {
        int startingSwimlane = mapping.computeIfAbsent(topicPartition, tp -> mapping.size() * swimlanesPerTopicPartition);
        return startingSwimlane + messageKey.hashCode() % swimlanesPerTopicPartition;
    }
}
