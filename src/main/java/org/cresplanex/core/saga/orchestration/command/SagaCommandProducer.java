package org.cresplanex.core.saga.orchestration.command;

import java.util.List;

/**
 * サーガのコマンドを生成および送信するインターフェース。
 */
public interface SagaCommandProducer {

    /**
     * サガ内のコマンドを送信します。
     *
     * @param sagaType サガのタイプ
     * @param sagaId サガのID
     * @param commands 送信するコマンドのリスト
     * @param sagaReplyChannel サガの返信チャネル
     * @return 最後に送信したメッセージのID
     */
    String sendCommands(String sagaType, String sagaId, List<CommandWithDestinationAndType> commands, String sagaReplyChannel);
}
