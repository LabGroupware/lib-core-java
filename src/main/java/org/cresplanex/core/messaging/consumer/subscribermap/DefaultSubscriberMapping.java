package org.cresplanex.core.messaging.consumer.subscribermap;

public class DefaultSubscriberMapping implements SubscriberMapping {

    @Override
    public String toExternal(String subscriberId) {
        return subscriberId; // サブスクライバーIDをそのまま返す
    }

}
