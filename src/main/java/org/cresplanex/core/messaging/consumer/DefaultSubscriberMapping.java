package org.cresplanex.core.messaging.consumer;

public class DefaultSubscriberMapping implements SubscriberMapping {

    @Override
    public String toExternal(String subscriberId) {
        return subscriberId; // サブスクライバーIDをそのまま返す
    }

}
