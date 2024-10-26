package org.cresplanex.core.consumer.jdbc;

import org.cresplanex.core.common.jdbc.CoreTransactionTemplate;
import org.cresplanex.core.common.jdbc.CoreTransactionTemplateConfiguration;
import org.cresplanex.core.consumer.common.DuplicateMessageDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(CoreTransactionTemplateConfiguration.class)
public class TransactionalNoopDuplicateMessageDetectorConfiguration {

    @Bean
    public DuplicateMessageDetector duplicateMessageDetector(CoreTransactionTemplate coreTransactionTemplate) {
        return new TransactionalNoopDuplicateMessageDetector(coreTransactionTemplate);
    }
}
