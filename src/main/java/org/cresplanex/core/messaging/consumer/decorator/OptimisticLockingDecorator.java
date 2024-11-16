package org.cresplanex.core.messaging.consumer.decorator;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 楽観的ロッキングデコレータ。
 * <p>
 * このデコレータは、楽観的ロックの失敗時にリトライを行い、メッセージの処理チェーンを実行します。
 * `OptimisticLockingFailureException`が発生した場合、最大10回のリトライを試みます。</p>
 */
@Transactional
public class OptimisticLockingDecorator implements MessageHandlerDecorator, Ordered {

    private static final Logger log = LoggerFactory.getLogger(OptimisticLockingDecorator.class);

    /**
     * `OptimisticLockingFailureException`が発生した際にリトライを実施しながら、
     * メッセージ処理チェーンを次に進めます。
     *
     * @param subscriberIdAndMessage 処理対象のサブスクライバIDとメッセージ情報
     * @param messageHandlerDecoratorChain メッセージ処理チェーン
     */
    @Override
    @Retryable(retryFor = {OptimisticLockingFailureException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 100))
    public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
        messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
    }

    /**
     * デコレータの順序を返します。 このデコレータの順序は150に設定されています。
     *
     * @return 順序値 (150)
     */
    @Override
    public int getOrder() {
        return 150;
    }
}
