package org.cresplanex.core.messaging.kafka.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafkaの設定を提供するクラス。
 * <p>
 * Kafkaの設定を提供します。
 * </p>
 */
@Configuration
public class CoreKafkaPropertiesConfiguration {

    /**
     * Kafkaの設定を提供します。
     *
     * @param bootstrapServers Kafkaのブートストラップサーバー
     * @param connectionValidationTimeout 接続の検証タイムアウト
     * @return Kafkaの設定
     */
    @Bean
    public CoreKafkaConfigurationProperties coreKafkaConfigurationProperties(@Value("${core.kafka.bootstrap.servers}") String bootstrapServers,
            @Value("${core.kafka.connection.validation.timeout:#{1000}}") long connectionValidationTimeout) {
        return new CoreKafkaConfigurationProperties(bootstrapServers, connectionValidationTimeout);
    }
}
