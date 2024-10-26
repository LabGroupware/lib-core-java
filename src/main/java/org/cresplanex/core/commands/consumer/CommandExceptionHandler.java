package org.cresplanex.core.commands.consumer;

import java.util.List;

import org.cresplanex.core.messaging.common.Message;

public class CommandExceptionHandler {

    public List<Message> invoke(Throwable cause) {
        throw new UnsupportedOperationException();
    }
}
