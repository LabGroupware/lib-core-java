package org.cresplanex.core.saga.simpledsl;

/**
 * シンプルなサガDSL（ドメイン固有言語）を提供するインターフェースです。
 *
 * @param <Data> サガで使用されるデータの型
 */
public interface SimpleSagaDsl<Data> {

    /**
     * サガのステップを定義するためのビルダーを取得します。
     *
     * @return StepBuilderのインスタンス
     */
    default StepBuilder<Data> step() {
        SimpleSagaDefinitionBuilder<Data> builder = new SimpleSagaDefinitionBuilder<>();
        return new StepBuilder<>(builder);
    }

}
