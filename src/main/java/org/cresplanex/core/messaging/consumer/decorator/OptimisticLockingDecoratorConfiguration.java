package org.cresplanex.core.messaging.consumer.decorator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * 楽観的ロッキングデコレータの設定クラス。
 * <p>
 * リトライ機能を有効化し、`OptimisticLockingDecorator`のビーンを登録します。</p>
 */
@Configuration
@EnableRetry
public class OptimisticLockingDecoratorConfiguration {

    /**
     * `OptimisticLockingDecorator` ビーンを生成します。
     *
     * @return OptimisticLockingDecoratorインスタンス
     */
    @Bean
    public OptimisticLockingDecorator optimisticLockingDecorator() {
        return new OptimisticLockingDecorator();
    }
}
