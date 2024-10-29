package org.cresplanex.core.messaging.kafka.common;

/**
 * アグリゲートタイプからKafkaトピック名へのマッピングを提供するクラス。
 * <p>
 * アグリゲートタイプをKafkaトピック名として利用できる形式に変換します。
 * </p>
 */
public class AggregateTopicMapping {

    /**
     * アグリゲートタイプをKafkaトピック名に変換します。
     *
     * @param aggregateType アグリゲートタイプの名前
     * @return 変換されたKafkaトピック名
     */
    public static String aggregateTypeToTopic(String aggregateType) {
        return TopicCleaner.clean(aggregateType);
    }
}
