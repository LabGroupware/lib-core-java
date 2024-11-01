package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.BiConsumer;

import org.cresplanex.core.messaging.common.Message;

/**
 * サガの各ステップを表すインターフェースです。
 *
 * @param <Data> ステップで処理されるデータの型
 */
public interface SagaStep<Data> extends ISagaStep<Data> {

    /**
     * 応答メッセージに基づいたハンドラを取得します。
     *
     * @param message 応答メッセージ
     * @param compensating 補償実行中かどうか
     * @return 応答ハンドラのオプショナル
     */
    Optional<BiConsumer<Data, Object>> getReplyHandler(Message message, boolean compensating);

    /**
     * ステップの実行結果を生成します。
     *
     * @param data ステップで使用するデータ
     * @param compensating 補償実行中かどうか
     * @return ステップの実行結果
     */
    StepOutcome makeStepOutcome(Data data, boolean compensating);

}
