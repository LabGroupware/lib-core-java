package org.cresplanex.core.common.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * トランザクションテンプレートの設定クラス。
 * <p>
 * Springのトランザクション管理を使用して {@link CoreTransactionTemplate} を構成します。
 * </p>
 */
@Configuration
public class CoreTransactionTemplateConfiguration {

    /**
     * CoreTransactionTemplateのBeanを作成します。
     * <p>
     * Springの {@link TransactionTemplate} を使用して、トランザクション管理を行う
     * {@link CoreSpringTransactionTemplate} のインスタンスを提供します。
     * </p>
     *
     * @param transactionTemplate Springのトランザクションテンプレート
     * @return CoreTransactionTemplateのインスタンス
     */
    @Bean
    public CoreTransactionTemplate coreTransactionTemplate(TransactionTemplate transactionTemplate) {
        return new CoreSpringTransactionTemplate(transactionTemplate);
    }
}
