package org.cresplanex.core.saga.common;

import org.cresplanex.core.messaging.common.Message;

public class StashedMessage {

    private final String sagaType;
    private final String sagaId;
    private final Message message;

    public String getSagaType() {
        return sagaType;
    }

    public StashedMessage(String sagaType, String sagaId, Message message) {
        this.sagaType = sagaType;
        this.sagaId = sagaId;
        this.message = message;
    }

    public String getSagaId() {
        return sagaId;
    }

    public Message getMessage() {
        return message;
    }
}
