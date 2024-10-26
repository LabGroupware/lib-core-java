package org.cresplanex.core.common.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class CoreTransactionTemplateConfiguration {

    @Bean
    public CoreTransactionTemplate coreTransactionTemplate(TransactionTemplate transactionTemplate) {
        return new CoreSpringTransactionTemplate(transactionTemplate);
    }
}
