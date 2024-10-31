package org.cresplanex.core.common.kafka.property;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * CoreKafkaConsumerSpringConfigurationPropertiesクラスのBean設定クラス。
 * <p>
 * Kafkaコンシューマーのプロパティを注入するために使用され、
 * CoreKafkaConsumerConfigurationPropertiesとしてSpringにBean登録されます。
 * </p>
 */
@EnableConfigurationProperties(CoreKafkaConsumerSpringProperties.class)
public class CoreKafkaConsumerSpringPropertiesConfiguration {

    /**
     * CoreKafkaConsumerConfigurationProperties Beanを作成し、 SpringのDIコンテナに登録します。
     *
     * @param coreKafkaConsumerSpringConfigurationProperties
     * Springから注入されたKafkaコンシューマー構成プロパティ
     * @return CoreKafkaConsumerConfigurationPropertiesのインスタンス
     */
    @Bean
    public CoreKafkaConsumerProperties coreKafkaConsumerProperties(
            CoreKafkaConsumerSpringProperties coreKafkaConsumerSpringConfigurationProperties) {

        CoreKafkaConsumerProperties coreKafkaConsumerProperties
                = new CoreKafkaConsumerProperties(coreKafkaConsumerSpringConfigurationProperties.getProperties());

        coreKafkaConsumerProperties.setBackPressure(coreKafkaConsumerSpringConfigurationProperties.getBackPressure());
        coreKafkaConsumerProperties.setPollTimeout(coreKafkaConsumerSpringConfigurationProperties.getPollTimeout());

        return coreKafkaConsumerProperties;
    }
}
