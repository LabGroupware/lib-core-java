package org.cresplanex.core.messaging.partitionmanagement;

public interface SubscriptionLeaderHook {

    void leaderUpdated(Boolean leader, String subscriptionId);
}
