package org.cresplanex.core.saga.lock;

import java.util.Optional;

import org.cresplanex.core.messaging.common.Message;

/**
 * Sagaのロック管理を行うインターフェースです。 ロックの取得や解放、メッセージのスタッシュなどの操作を提供します。
 */
public interface SagaLockManager {

    /**
     * 指定されたSagaタイプとIDでロックを取得します。
     *
     * @param sagaType Sagaの種類
     * @param sagaId SagaのID
     * @param target ロック対象
     * @return ロックが成功した場合は{@code true}、失敗した場合は{@code false}
     */
    boolean claimLock(String sagaType, String sagaId, String target);

    /**
     * ロック中のメッセージをスタッシュします。
     *
     * @param sagaType Sagaの種類
     * @param sagaId SagaのID
     * @param target ロック対象
     * @param message スタッシュするメッセージ
     */
    void stashMessage(String sagaType, String sagaId, String target, Message message);

    /**
     * 指定されたターゲットのロックを解除し、スタッシュされていたメッセージを返します。
     *
     * @param sagaId SagaのID
     * @param target ロック対象
     * @return
     * スタッシュされていたメッセージがある場合は{@code Optional<Message>}、ない場合は{@code Optional.empty()}
     */
    Optional<Message> unlock(String sagaId, String target);
}
