package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.cresplanex.core.messaging.consumer.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

/**
 * デコレータで装飾されたメッセージハンドラを生成するファクトリクラス。
 * <p>
 * 指定されたデコレータのリストに基づいてメッセージハンドラを装飾し、処理チェーンを構築します。</p>
 */
public class DecoratedMessageHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final List<MessageHandlerDecorator> decorators;

    /**
     * コンストラクタ。
     * <p>
     * 指定されたデコレータリストを優先順位でソートして、メッセージハンドラを装飾します。</p>
     *
     * @param decorators 使用するメッセージハンドラデコレータのリスト
     */
    public DecoratedMessageHandlerFactory(List<MessageHandlerDecorator> decorators) {
        decorators.sort(Comparator.comparingInt(MessageHandlerDecorator::getOrder));
        this.decorators = decorators;
    }

    /**
     * メッセージハンドラをデコレータで装飾し、処理チェーンを作成します。
     *
     * @param mh メッセージを処理するハンドラ
     * @return 装飾されたメッセージ処理チェーンを実行するコンシューマ
     */
    public Consumer<SubscriberIdAndMessage> decorate(MessageHandler mh) {
        MessageHandlerDecoratorChainBuilder builder = new MessageHandlerDecoratorChainBuilder();

        // デコレータをチェーンに追加
        for (MessageHandlerDecorator mhd : decorators) {
            builder = builder.andThen(mhd);
        }

        // 最後のチェーン処理で実際のメッセージハンドラを呼び出す
        MessageHandlerDecoratorChain chain = builder.andFinally((smh) -> {
            String subscriberId = smh.getSubscriberId();
            Message message = smh.getMessage();
            try {
                logger.trace("Invoking handler {} {}", subscriberId, message.getId());
                mh.accept(smh.getMessage());
                logger.trace("handled message {} {}", subscriberId, message.getId());
            } catch (Exception e) {
                logger.error("Got exception {} {}", subscriberId, message.getId());
                logger.error("Got exception ", e);
                throw e;
            }
        });
        return chain::invokeNext;
    }
}
