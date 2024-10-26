package org.cresplanex.core.coordination.leadership;

public interface LeaderSelectorFactory {

    CoreLeaderSelector create(String lockId,
            String leaderId,
            LeaderSelectedCallback leaderSelectedCallback,
            Runnable leaderRemovedCallback);
}
