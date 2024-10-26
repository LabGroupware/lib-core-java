package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashSet;
import java.util.Set;

import org.apache.kafka.common.TopicPartition;

public class BackPressureManager {

    private final BackPressureConfig backPressureConfig;
    private final Set<TopicPartition> allTopicPartitions = new HashSet<>();

    private BackPressureManagerState state = new BackPressureManagerNormalState();

    public BackPressureManager(BackPressureConfig backPressureConfig) {
        this.backPressureConfig = backPressureConfig;
    }

    public BackPressureActions update(Set<TopicPartition> topicPartitions, int backlog) {
        allTopicPartitions.addAll(topicPartitions);
        BackPressureManagerStateAndActions stateAndActions = state.update(allTopicPartitions, backlog, backPressureConfig);
        this.state = stateAndActions.state;
        return stateAndActions.actions;
    }

}
