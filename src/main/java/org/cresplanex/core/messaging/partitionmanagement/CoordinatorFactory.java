package org.cresplanex.core.messaging.partitionmanagement;

import java.util.Set;
import java.util.function.Consumer;

import org.cresplanex.core.coordination.leadership.LeaderSelectedCallback;

public interface CoordinatorFactory {

    Coordinator makeCoordinator(String subscriberId,
            Set<String> channels,
            String subscriptionId,
            Consumer<Assignment> assignmentUpdatedCallback,
            String lockId,
            LeaderSelectedCallback leaderSelected,
            Runnable leaderRemoved);
}
