package org.cresplanex.core.messaging.kafka.basic.consumer;

public interface ConsumerCallbacks {

    void onTryCommitCallback();

    void onCommitedCallback();

    void onCommitFailedCallback();
}
