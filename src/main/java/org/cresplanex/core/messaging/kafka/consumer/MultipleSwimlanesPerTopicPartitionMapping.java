package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.common.TopicPartition;

public class MultipleSwimlanesPerTopicPartitionMapping implements TopicPartitionToSwimlaneMapping {

    private final int swimlanesPerTopicPartition;

    private final ConcurrentHashMap<TopicPartition, Integer> mapping = new ConcurrentHashMap<>();

    public MultipleSwimlanesPerTopicPartitionMapping(int swimlanesPerTopicPartition) {
        this.swimlanesPerTopicPartition = swimlanesPerTopicPartition;
    }

    @Override
    public Integer toSwimlane(TopicPartition topicPartition, String messageKey) {
        int startingSwimlane = mapping.computeIfAbsent(topicPartition, tp -> mapping.size() * swimlanesPerTopicPartition);
        return startingSwimlane + messageKey.hashCode() % swimlanesPerTopicPartition;
    }
}
