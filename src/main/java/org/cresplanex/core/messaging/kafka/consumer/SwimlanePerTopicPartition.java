package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.common.TopicPartition;

/**
 * Kafkaのトピックパーティションごとに個別のスイムレーンを割り当てるクラスです。 トピックパーティションごとに一意のスイムレーンIDを提供します。
 */
public class SwimlanePerTopicPartition implements TopicPartitionToSwimlaneMapping {

    private final ConcurrentHashMap<TopicPartition, Integer> mapping = new ConcurrentHashMap<>();

    /**
     * 指定されたトピックパーティションに対するスイムレーンIDを取得します。 まだ割り当てられていない場合は、新たにスイムレーンIDを割り当てます。
     *
     * @param topicPartition Kafkaトピックパーティション
     * @param messageKey メッセージキー（未使用）
     * @return トピックパーティションに対応するスイムレーンID
     */
    @Override
    public Integer toSwimlane(TopicPartition topicPartition, String messageKey) {
        return mapping.computeIfAbsent(topicPartition, tp -> mapping.size());
    }
}
