package org.cresplanex.core.messaging.consumer;

import java.util.Set;

/**
 * メッセージコンシューマのインターフェースを定義します。このインターフェースは、 メッセージの購読、IDの取得、およびクローズ操作を提供します。
 */
public interface MessageConsumerImplementation {

    /**
     * 指定されたサブスクライバIDとチャンネルでメッセージを購読します。 購読されたメッセージはハンドラーで処理されます。
     *
     * @param subscriberId サブスクライバの一意のID
     * @param channels 購読するチャンネルのセット
     * @param handler メッセージ処理を行うハンドラー
     * @return 購読情報を含むMessageSubscriptionオブジェクト
     */
    MessageSubscription subscribe(String subscriberId, Set<String> channels, MessageHandler handler);

    /**
     * メッセージコンシューマの一意のIDを取得します。
     *
     * @return コンシューマのIDを示す文字列
     */
    String getId();

    /**
     * コンシューマを閉じ、リソースを解放します。
     */
    void close();
}
