package org.cresplanex.core.messaging.consumer.decorator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

/**
 * メッセージハンドラデコレータのチェーンを構築するためのビルダークラス。
 * <p>
 * 複数のデコレータを順に実行するチェーンを生成します。</p>
 */
public class MessageHandlerDecoratorChainBuilder {

    // チェーンに追加されるメッセージハンドラデコレータのリスト
    private final List<MessageHandlerDecorator> handlers = new LinkedList<>();

    /**
     * デコレータをチェーンに追加します。
     *
     * @param smh 追加するメッセージハンドラデコレータ
     */
    private void add(MessageHandlerDecorator smh) {
        this.handlers.add(smh);
    }

    /**
     * 新たなメッセージハンドラデコレータをチェーンに追加します。
     *
     * @param smh 追加するメッセージハンドラデコレータ
     * @return 現在のインスタンス（ビルダーのメソッドチェーンのため）
     */
    public MessageHandlerDecoratorChainBuilder andThen(MessageHandlerDecorator smh) {
        this.add(smh);
        return this;
    }

    /**
     * 最後に実行されるコンシューマーを指定して、デコレータチェーンを構築します。
     *
     * @param consumer チェーンの最後に実行する処理
     * @return 構築されたメッセージハンドラデコレータチェーン
     */
    public MessageHandlerDecoratorChain andFinally(Consumer<SubscriberIdAndMessage> consumer) {
        return buildChain(handlers, consumer);
    }

    /**
     * 指定されたデコレータリストと最終的な処理を元に、デコレータチェーンを再帰的に構築します。
     *
     * @param handlers チェーン内のメッセージハンドラデコレータのリスト
     * @param consumer チェーンの最後に実行する処理
     * @return 構築されたメッセージハンドラデコレータチェーン
     */
    private MessageHandlerDecoratorChain buildChain(List<MessageHandlerDecorator> handlers, Consumer<SubscriberIdAndMessage> consumer) {
        if (handlers.isEmpty()) {
            return consumer::accept; 
        }else {
            MessageHandlerDecorator head = handlers.get(0);
            List<MessageHandlerDecorator> tail = handlers.subList(1, handlers.size());
            return subscriberIdAndMessage -> head.accept(subscriberIdAndMessage, buildChain(tail, consumer));
        }
    }
}
