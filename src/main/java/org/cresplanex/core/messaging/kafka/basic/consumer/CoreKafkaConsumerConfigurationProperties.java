package org.cresplanex.core.messaging.kafka.basic.consumer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafkaコンシューマーの構成プロパティクラス。
 * <p>
 * Kafkaコンシューマーのプロパティ設定、バックプレッシャー設定、およびポーリングタイムアウトを管理します。
 * </p>
 */
public class CoreKafkaConsumerConfigurationProperties {

    private Map<String, String> properties = new HashMap<>();
    private BackPressureConfig backPressure = new BackPressureConfig();
    private long pollTimeout;

    /**
     * 現在のバックプレッシャー設定を取得します。
     *
     * @return BackPressureConfig オブジェクト
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
     * @return ポーリングタイムアウトの値（ミリ秒単位）
     */
    public long getPollTimeout() {
        return pollTimeout;
    }

    /**
     * ポーリングタイムアウトを設定します。
     *
     * @param pollTimeout ポーリングタイムアウトの値（ミリ秒単位）
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

    /**
     * デフォルトコンストラクタ。
     */
    public CoreKafkaConsumerConfigurationProperties() {
    }

    /**
     * コンシューマプロパティマップを指定してインスタンスを作成します。
     *
     * @param properties Kafkaコンシューマープロパティのマップ
     */
    public CoreKafkaConsumerConfigurationProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * Kafkaコンシューマープロパティのマップを設定します。
     *
     * @param properties 設定するプロパティマップ
     */
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    /**
     * 空の設定プロパティオブジェクトを生成します。
     *
     * @return 空のCoreKafkaConsumerConfigurationPropertiesインスタンス
     */
    public static CoreKafkaConsumerConfigurationProperties empty() {
        return new CoreKafkaConsumerConfigurationProperties();
    }
}

