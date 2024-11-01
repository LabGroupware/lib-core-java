package org.cresplanex.core.saga.orchestration;

/**
 * SagaのタイプとIDを保持するクラス。
 */
public class SagaTypeAndId {

    private final String sagaType;
    private final String sagaId;

    /**
     * SagaTypeAndIdのインスタンスを構築します。
     *
     * @param sagaType Sagaのタイプ
     * @param sagaId SagaのID
     */
    public SagaTypeAndId(String sagaType, String sagaId) {
        this.sagaType = sagaType;
        this.sagaId = sagaId;
    }

    /**
     * SagaのIDを取得します。
     *
     * @return SagaのID
     */
    public String getSagaId() {
        return sagaId;
    }

    /**
     * Sagaのタイプを取得します。
     *
     * @return Sagaのタイプ
     */
    public String getSagaType() {
        return sagaType;
    }
}
