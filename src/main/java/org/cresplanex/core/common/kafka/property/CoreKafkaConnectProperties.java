package org.cresplanex.core.common.kafka.property;

/**
 * Kafkaの設定プロパティを管理するクラス。
 * <p>
 * Kafkaサーバーの接続設定を保持します。
 * </p>
 */
public class CoreKafkaConnectProperties {

    /** Kafkaブートストラップサーバーのアドレス */
    private final String bootstrapServers;

    /** 接続検証のタイムアウト時間 */
    private final long connectionValidationTimeout;

    /**
     * 指定されたサーバーアドレスと接続タイムアウトを持つインスタンスを初期化します。
     *
     * @param bootstrapServers Kafkaブートストラップサーバーのアドレス
     * @param connectionValidationTimeout 接続検証のタイムアウト時間
     */
    public CoreKafkaConnectProperties(String bootstrapServers, long connectionValidationTimeout) {
        this.bootstrapServers = bootstrapServers;
        this.connectionValidationTimeout = connectionValidationTimeout;
    }

    /**
     * Kafkaのブートストラップサーバーのアドレスを取得します。
     *
     * @return ブートストラップサーバーのアドレス
     */
    public String getBootstrapServers() {
        return bootstrapServers;
    }

    /**
     * 接続検証のタイムアウト時間を取得します。
     *
     * @return 接続検証のタイムアウト時間
     */
    public long getConnectionValidationTimeout() {
        return connectionValidationTimeout;
    }
}
