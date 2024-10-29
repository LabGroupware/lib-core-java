package org.cresplanex.core.messaging.kafka.basic.consumer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * CoreKafkaConsumerSpringConfigurationPropertiesクラスのBean設定クラス。
 * <p>
 * Kafkaコンシューマーのプロパティを注入するために使用され、
 * CoreKafkaConsumerConfigurationPropertiesとしてSpringにBean登録されます。
 * </p>
 */
@EnableConfigurationProperties(CoreKafkaConsumerSpringConfigurationProperties.class)
public class CoreKafkaConsumerSpringConfigurationPropertiesConfiguration {

    /**
     * CoreKafkaConsumerConfigurationProperties Beanを作成し、
     * SpringのDIコンテナに登録します。
     *
     * @param coreKafkaConsumerSpringConfigurationProperties Springから注入されたKafkaコンシューマー構成プロパティ
     * @return CoreKafkaConsumerConfigurationPropertiesのインスタンス
     */
    @Bean
    public CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties(
            CoreKafkaConsumerSpringConfigurationProperties coreKafkaConsumerSpringConfigurationProperties) {
        
        CoreKafkaConsumerConfigurationProperties coreKafkaConsumerConfigurationProperties = 
                new CoreKafkaConsumerConfigurationProperties(coreKafkaConsumerSpringConfigurationProperties.getProperties());
        
        coreKafkaConsumerConfigurationProperties.setBackPressure(coreKafkaConsumerSpringConfigurationProperties.getBackPressure());
        coreKafkaConsumerConfigurationProperties.setPollTimeout(coreKafkaConsumerSpringConfigurationProperties.getPollTimeout());
        
        return coreKafkaConsumerConfigurationProperties;
    }
}
