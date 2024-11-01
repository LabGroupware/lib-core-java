package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.messaging.common.Message;

/**
 * サーガの開始とメッセージ応答のハンドリングを定義するインターフェース。
 *
 * @param <Data> サーガが処理するデータの型
 */
public interface SagaDefinition<Data> {

    /**
     * サーガの開始時に実行されるアクションを定義します。
     *
     * @param sagaData サーガのデータ
     * @return 開始時に実行するサーガアクション
     */
    SagaActions<Data> start(Data sagaData);

    /**
     * 応答メッセージを処理し、サーガの次のアクションを決定します。
     *
     * @param sagaType サーガのタイプ
     * @param sagaId サーガのID
     * @param currentState サーガの現在の状態
     * @param sagaData サーガのデータ
     * @param message 応答メッセージ
     * @return 応答処理後のサーガアクション
     */
    SagaActions<Data> handleReply(String sagaType, String sagaId, String currentState, Data sagaData, Message message);

}
