package org.cresplanex.core.messaging.partitionmanagement;

import java.util.Set;

public interface SubscriptionLifecycleHook {

    void partitionsUpdated(String channel, String subscriptionId, Set<Integer> currentPartitions);
}
