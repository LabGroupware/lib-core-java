package org.cresplanex.core.messaging.consumer.subscribermap;

/**
 * デフォルトのサブスクライバーIDマッピング
 *
 */
public class DefaultSubscriberMapping implements SubscriberMapping {

    @Override
    public String toExternal(String subscriberId) {
        return subscriberId; // サブスクライバーIDをそのまま返す
    }

}
