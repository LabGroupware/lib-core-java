package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KafkaコンシューマーのSpring構成プロパティクラス。
 * <p>
 * プロパティファイルの"core.local.kafka.consumer"プレフィックスに基づき、
 * Kafkaコンシューマーの設定を行います。
 * </p>
 */
@ConfigurationProperties("core.local.kafka.consumer")
public class CoreKafkaConsumerSpringConfigurationProperties {

    Map<String, String> properties = new HashMap<>();
    private BackPressureConfig backPressure = new BackPressureConfig();
    private long pollTimeout = 100;

    /**
     * バックプレッシャー設定を取得します。
     *
     * @return 現在のバックプレッシャー設定
     */
    public BackPressureConfig getBackPressure() {
        return backPressure;
    }

    /**
     * バックプレッシャー設定を設定します。
     *
     * @param backPressure バックプレッシャーの設定
     */
    public void setBackPressure(BackPressureConfig backPressure) {
        this.backPressure = backPressure;
    }

    /**
     * ポーリングタイムアウトを取得します。
     *
     * @return ポーリングタイムアウトの値（ミリ秒）
     */
    public long getPollTimeout() {
        return pollTimeout;
    }

    /**
     * ポーリングタイムアウトを設定します。
     *
     * @param pollTimeout ポーリングタイムアウトの値（ミリ秒）
     */
    public void setPollTimeout(long pollTimeout) {
        this.pollTimeout = pollTimeout;
    }

    /**
     * Kafkaコンシューマープロパティのマップを取得します。
     *
     * @return プロパティのマップ
     */
    public Map<String, String> getProperties() {
        return properties;
    }
}
