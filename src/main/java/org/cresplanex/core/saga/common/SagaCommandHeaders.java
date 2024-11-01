package org.cresplanex.core.saga.common;

import org.cresplanex.core.commands.common.CommandMessageHeaders;

/**
 * Saga のコマンドに使用するヘッダー定義クラス。
 */
public class SagaCommandHeaders {

    /**
     * Saga のタイプを示すヘッダーキー。
     */
    public static final String SAGA_TYPE = CommandMessageHeaders.COMMAND_HEADER_PREFIX + "saga_type";

    /**
     * Saga の ID を示すヘッダーキー。
     */
    public static final String SAGA_ID = CommandMessageHeaders.COMMAND_HEADER_PREFIX + "saga_id";
}
