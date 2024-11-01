package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.command.CommandWithDestinationAndType;

/**
 * サガにおいて参加者のアクションやコマンドを定義するインターフェース。
 *
 * @param <Data> サガデータのタイプ
 */
public interface ParticipantInvocation<Data> {

    /**
     * 受信メッセージが成功の応答であるかを判定します。
     *
     * @param message 受信したメッセージ
     * @return 成功応答の場合はtrue、それ以外の場合はfalse
     */
    boolean isSuccessfulReply(Message message);

    /**
     * このインボケーションがサガデータに基づいて実行可能かどうかを判定します。
     *
     * @param data サガデータ
     * @return 実行可能な場合はtrue、そうでない場合はfalse
     */
    boolean isInvocable(Data data);

    /**
     * 送信するコマンドを生成します。
     *
     * @param data サガデータ
     * @return CommandWithDestinationAndType 送信するコマンド
     */
    CommandWithDestinationAndType makeCommandToSend(Data data);
}
