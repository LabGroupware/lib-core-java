package org.cresplanex.core.saga.orchestration.command;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.cresplanex.core.commands.producer.CommandProducer;
import org.cresplanex.core.commands.producer.CoreCommandProducerConfiguration;

/**
 * SagaCommandProducerの構成クラス。SagaCommandProducerのインスタンスを提供します。
 */
@Configuration
@Import({CoreCommandProducerConfiguration.class})
public class SagaCommandProducerConfiguration {

    /**
     * SagaCommandProducerのBeanを生成します。
     *
     * @param commandProducer コマンドプロデューサ
     * @return SagaCommandProducerのインスタンス
     */
    @Bean
    public SagaCommandProducer sagaCommandProducer(CommandProducer commandProducer) {
        return new SagaCommandProducerImpl(commandProducer);
    }
}
