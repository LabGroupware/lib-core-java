package org.cresplanex.core.consumer.common;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

public interface DuplicateMessageDetector {
  boolean isDuplicate(String consumerId, String messageId);
  void doWithMessage(SubscriberIdAndMessage subscriberIdAndMessage, Runnable callback);
}
