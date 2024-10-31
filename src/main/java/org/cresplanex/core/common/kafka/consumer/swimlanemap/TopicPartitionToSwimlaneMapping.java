package org.cresplanex.core.common.kafka.consumer.swimlanemap;

import org.apache.kafka.common.TopicPartition;

/**
 * Kafkaのトピックパーティションを基に、メッセージ処理のスイムレーンIDを割り当てるためのインターフェースです。
 */
public interface TopicPartitionToSwimlaneMapping {

    /**
     * 指定されたトピックパーティションとメッセージキーに基づいて、スイムレーンIDを取得します。
     *
     * @param topicPartition Kafkaトピックパーティション
     * @param messageKey メッセージキー（任意）
     * @return 割り当てられたスイムレーンID
     */
    Integer toSwimlane(TopicPartition topicPartition, String messageKey);
}
