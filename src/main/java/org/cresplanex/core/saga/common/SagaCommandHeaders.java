package org.cresplanex.core.saga.common;

import org.cresplanex.core.commands.common.CommandMessageHeaders;

public class SagaCommandHeaders {

    public static final String SAGA_TYPE = CommandMessageHeaders.COMMAND_HEADER_PREFIX + "saga_type";
    public static final String SAGA_ID = CommandMessageHeaders.COMMAND_HEADER_PREFIX + "saga_id";

}
