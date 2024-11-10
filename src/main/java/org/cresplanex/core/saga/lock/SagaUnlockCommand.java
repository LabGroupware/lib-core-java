package org.cresplanex.core.saga.lock;

import org.cresplanex.core.commands.common.Command;

/**
 * Saga のロック解除を指示するコマンドクラス。
 */
public class SagaUnlockCommand implements Command {
    public static final String TYPE = "core.sagaUnlock";
}
