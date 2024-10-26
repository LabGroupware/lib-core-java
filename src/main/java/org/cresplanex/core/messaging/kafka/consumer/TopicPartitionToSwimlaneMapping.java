package org.cresplanex.core.messaging.kafka.consumer;

import org.apache.kafka.common.TopicPartition;

public interface TopicPartitionToSwimlaneMapping {

    Integer toSwimlane(TopicPartition topicPartition, String messageKey);
}
