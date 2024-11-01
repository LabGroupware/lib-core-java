package org.cresplanex.core.saga.common;

import org.cresplanex.core.commands.common.CommandMessageHeaders;

/**
 * Saga の返信時に使用するヘッダー定義クラス。
 */
public class SagaReplyHeaders {

    /**
     * Saga のタイプを示す返信ヘッダーキー。
     */
    public static final String REPLY_SAGA_TYPE = CommandMessageHeaders.inReply(SagaCommandHeaders.SAGA_TYPE);

    /**
     * Saga の ID を示す返信ヘッダーキー。
     */
    public static final String REPLY_SAGA_ID = CommandMessageHeaders.inReply(SagaCommandHeaders.SAGA_ID);

    /**
     * ロック対象を示す返信ヘッダーキー。
     */
    public static final String REPLY_LOCKED = "saga-locked-target";
}
