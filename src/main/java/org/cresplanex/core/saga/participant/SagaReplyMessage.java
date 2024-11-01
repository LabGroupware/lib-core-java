package org.cresplanex.core.saga.participant;

import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.messaging.common.MessageImpl;
import org.cresplanex.core.saga.lock.LockTarget;

/**
 * SagaReplyMessageは、サガの応答メッセージを表すクラスです。 ロック対象を含む場合、ロック情報を格納します。
 */
public class SagaReplyMessage extends MessageImpl {

    private final Optional<LockTarget> lockTarget;

    /**
     * コンストラクタ
     *
     * @param body メッセージの内容
     * @param headers メッセージヘッダー
     * @param lockTarget ロック対象のオプショナル
     */
    public SagaReplyMessage(String body, Map<String, String> headers, Optional<LockTarget> lockTarget) {
        super(body, headers);
        this.lockTarget = lockTarget;
    }

    /**
     * ロック対象を取得します。
     *
     * @return ロック対象のオプショナル
     */
    public Optional<LockTarget> getLockTarget() {
        return lockTarget;
    }

    /**
     * ロック対象が存在するかどうかを判定します。
     *
     * @return ロック対象が存在する場合はtrue、そうでない場合はfalse
     */
    public boolean hasLockTarget() {
        return lockTarget.isPresent();
    }
}
