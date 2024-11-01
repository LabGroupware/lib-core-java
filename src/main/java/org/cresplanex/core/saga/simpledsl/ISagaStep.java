package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.messaging.common.Message;

/**
 * サガのステップを表すインターフェース。 各ステップは、アクションや補償の有無、成功した応答かどうかを判定する機能を持ちます。
 *
 * @param <Data> サガデータの型
 */
public interface ISagaStep<Data> {

    /**
     * メッセージが成功した応答かどうかを判定します。
     *
     * @param compensating 補償動作中かどうか
     * @param message メッセージインスタンス
     * @return 応答が成功である場合は true
     */
    boolean isSuccessfulReply(boolean compensating, Message message);

    /**
     * データに基づいてアクションが存在するかを判定します。
     *
     * @param data サガのデータ
     * @return アクションが存在する場合は true
     */
    boolean hasAction(Data data);

    /**
     * データに基づいて補償動作が存在するかを判定します。
     *
     * @param data サガのデータ
     * @return 補償動作が存在する場合は true
     */
    boolean hasCompensation(Data data);
}
