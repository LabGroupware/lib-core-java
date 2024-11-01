package org.cresplanex.core.saga.orchestration.repository;

/**
 * Sagaインスタンスとその関連するデータを保持するクラス。
 *
 * @param <Data> Sagaに関連するデータの型
 */
public class SagaInstanceData<Data> {

    private final SagaInstance sagaInstance;
    private final Data sagaData;

    /**
     * 指定されたSagaインスタンスと関連するデータでインスタンスを初期化します。
     *
     * @param sagaInstance Sagaのインスタンス
     * @param sagaData Sagaに関連するデータ
     */
    public SagaInstanceData(SagaInstance sagaInstance, Data sagaData) {
        this.sagaInstance = sagaInstance;
        this.sagaData = sagaData;
    }

    /**
     * Sagaのインスタンスを取得します。
     *
     * @return Sagaのインスタンス
     */
    public SagaInstance getSagaInstance() {
        return sagaInstance;
    }

    /**
     * Sagaに関連するデータを取得します。
     *
     * @return Sagaに関連するデータ
     */
    public Data getSagaData() {
        return sagaData;
    }
}
