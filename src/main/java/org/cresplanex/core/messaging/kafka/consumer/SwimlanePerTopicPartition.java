package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.kafka.common.TopicPartition;

public class SwimlanePerTopicPartition implements TopicPartitionToSwimlaneMapping {

    private final ConcurrentHashMap<TopicPartition, Integer> mapping = new ConcurrentHashMap<>();

    @Override
    public Integer toSwimlane(TopicPartition topicPartition, String messageKey) {
        return mapping.computeIfAbsent(topicPartition, tp -> mapping.size());
    }
}
