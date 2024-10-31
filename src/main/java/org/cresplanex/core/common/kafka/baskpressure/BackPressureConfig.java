package org.cresplanex.core.common.kafka.baskpressure;

/**
 * バックプレッシャー（負荷制御）設定を表現するクラス。
 * <p>
 * Kafkaコンシューマの負荷制御のためのしきい値（低・高）を設定します。
 * </p>
 */
public class BackPressureConfig {

    /** バックプレッシャーの低しきい値 */
    private int low = 0;

    /** バックプレッシャーの高しきい値 */
    private int high = Integer.MAX_VALUE;

    /**
     * デフォルトのしきい値でバックプレッシャー設定を初期化します。
     */
    public BackPressureConfig() {
    }

    /**
     * 指定された低しきい値と高しきい値でバックプレッシャー設定を初期化します。
     *
     * @param low バックプレッシャーの低しきい値
     * @param high バックプレッシャーの高しきい値
     */
    public BackPressureConfig(int low, int high) {
        this.low = low;
        this.high = high;
    }

    /**
     * 低しきい値を取得します。
     *
     * @return 低しきい値
     */
    public int getLow() {
        return low;
    }

    /**
     * 低しきい値を設定します。
     *
     * @param low 設定する低しきい値
     */
    public void setLow(int low) {
        this.low = low;
    }

    /**
     * 高しきい値を取得します。
     *
     * @return 高しきい値
     */
    public int getHigh() {
        return high;
    }

    /**
     * 高しきい値を設定します。
     *
     * @param high 設定する高しきい値
     */
    public void setHigh(int high) {
        this.high = high;
    }
}
