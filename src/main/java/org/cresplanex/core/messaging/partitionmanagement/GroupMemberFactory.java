package org.cresplanex.core.messaging.partitionmanagement;

public interface GroupMemberFactory {

    GroupMember create(String groupId, String memberId);
}
