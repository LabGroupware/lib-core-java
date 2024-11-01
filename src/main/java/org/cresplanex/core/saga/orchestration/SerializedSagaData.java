package org.cresplanex.core.saga.orchestration;

/**
 * シリアライズされたSagaデータを保持するクラス。 Sagaデータの型とそのJSON表現を格納します。
 */
public class SerializedSagaData {

    private final String sagaDataType;
    private final String sagaDataJSON;

    /**
     * シリアライズされたSagaデータを構築します。
     *
     * @param sagaDataType Sagaデータの型
     * @param sagaDataJSON SagaデータのJSON表現
     */
    public SerializedSagaData(String sagaDataType, String sagaDataJSON) {
        this.sagaDataType = sagaDataType;
        this.sagaDataJSON = sagaDataJSON;
    }

    /**
     * SagaデータのJSON表現を取得します。
     *
     * @return SagaデータのJSON表現
     */
    public String getSagaDataJSON() {
        return sagaDataJSON;
    }

    /**
     * Sagaデータの型を取得します。
     *
     * @return Sagaデータの型
     */
    public String getSagaDataType() {
        return sagaDataType;
    }
}
