package org.cresplanex.core.messaging.kafka.common;

public class AggregateTopicMapping {

    public static String aggregateTypeToTopic(String aggregateType) {
        return TopicCleaner.clean(aggregateType);
    }

}
