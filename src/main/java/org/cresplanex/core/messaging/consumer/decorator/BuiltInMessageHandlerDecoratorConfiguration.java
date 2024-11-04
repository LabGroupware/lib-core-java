package org.cresplanex.core.messaging.consumer.decorator;

import java.util.List;

import org.cresplanex.core.messaging.common.MessageInterceptor;
import org.cresplanex.core.messaging.consumer.duplicate.DuplicateMessageDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 組み込みのメッセージハンドラデコレータの設定クラス。
 * <p>
 * このクラスでは、デフォルトのメッセージハンドラデコレータを設定し、メッセージ処理チェーンを構成するビーンを定義します。</p>
 */
@Configuration
public class BuiltInMessageHandlerDecoratorConfiguration {

    // メッセージインターセプターの配列（オプション）
    @Autowired(required = false)
    private final MessageInterceptor[] messageInterceptors = new MessageInterceptor[0];

    /**
     * メッセージハンドラデコレータのリストを使用して、DecoratedMessageHandlerFactoryを生成します。
     *
     * @param decorators メッセージハンドラデコレータのリスト
     * @return DecoratedMessageHandlerFactory インスタンス
     */
    @Bean("org.cresplanex.core.messaging.consumer.decorator.DecoratedMessageHandlerFactory")
    public DecoratedMessageHandlerFactory subscribedMessageHandlerChainFactory(List<MessageHandlerDecorator> decorators) {
        return new DecoratedMessageHandlerFactory(decorators);
    }

    /**
     * 受信前後のメッセージ処理を行うデコレータのビーンを生成します。
     *
     * @return PrePostReceiveMessageHandlerDecorator インスタンス
     */
    @Bean("org.cresplanex.core.messaging.consumer.decorator.PrePostReceiveMessageHandlerDecorator")
    public PrePostReceiveMessageHandlerDecorator prePostReceiveMessageHandlerDecoratorDecorator() {
        return new PrePostReceiveMessageHandlerDecorator(messageInterceptors);
    }

    /**
     * 重複検出を行うメッセージハンドラデコレータのビーンを生成します。
     *
     * @param duplicateMessageDetector 重複メッセージ検出機能を持つインスタンス
     * @return DuplicateDetectingMessageHandlerDecorator インスタンス
     */
    @Bean("org.cresplanex.core.messaging.consumer.decorator.DuplicateDetectingMessageHandlerDecorator")
    public DuplicateDetectingMessageHandlerDecorator duplicateDetectingMessageHandlerDecorator(DuplicateMessageDetector duplicateMessageDetector) {
        return new DuplicateDetectingMessageHandlerDecorator(duplicateMessageDetector);
    }

    /**
     * ハンドラの処理前後に追加の処理を行うデコレータのビーンを生成します。
     *
     * @return PrePostHandlerMessageHandlerDecorator インスタンス
     */
    @Bean("org.cresplanex.core.messaging.consumer.decorator.PrePostHandlerMessageHandlerDecorator")
    public PrePostHandlerMessageHandlerDecorator prePostHandlerMessageHandlerDecorator() {
        return new PrePostHandlerMessageHandlerDecorator(messageInterceptors);
    }
}
