package org.cresplanex.core.saga.orchestration.repository;

import java.util.Set;
import org.cresplanex.core.saga.orchestration.DestinationAndResource;
import org.cresplanex.core.saga.orchestration.SerializedSagaData;

/**
 * Sagaのインスタンスを表現するクラス。Sagaの実行状態や関連データを保持します。
 */
public class SagaInstance {

    private String sagaType;
    private String id;
    private String lastRequestId;
    private SerializedSagaData serializedSagaData;
    private String stateName;
    private Set<DestinationAndResource> destinationsAndResources;
    private Boolean endState = false;
    private Boolean compensating = false;
    private Boolean failed = false;

    /**
     * このSagaインスタンスの状態を文字列形式で返します。
     *
     * @return Sagaインスタンスの状態を表す文字列
     */
    @Override
    public String toString() {
        return "SagaInstance{"
                + "sagaType='" + sagaType + '\''
                + ", id='" + id + '\''
                + ", lastRequestId='" + lastRequestId + '\''
                + ", serializedSagaData=" + serializedSagaData
                + ", stateName='" + stateName + '\''
                + ", destinationsAndResources=" + destinationsAndResources
                + ", endState=" + endState
                + ", compensating=" + compensating
                + ", failed=" + failed
                + '}';
    }

    /**
     * SagaInstanceの全フィールドを指定して初期化します。
     *
     * @param sagaType Sagaの種類
     * @param sagaId SagaのID
     * @param stateName 現在の状態
     * @param lastRequestId 最後のリクエストID
     * @param serializedSagaData Sagaのシリアル化されたデータ
     * @param destinationsAndResources 目的地とリソースのセット
     * @param endState 終了状態かどうか
     * @param compensating 補償中かどうか
     * @param failed 失敗状態かどうか
     */
    public SagaInstance(String sagaType, String sagaId, String stateName, String lastRequestId, SerializedSagaData serializedSagaData, Set<DestinationAndResource> destinationsAndResources,
            boolean endState, boolean compensating, boolean failed) {
        this(sagaType, sagaId, stateName, lastRequestId, serializedSagaData, destinationsAndResources);
        this.endState = endState;
        this.compensating = compensating;
        this.failed = failed;
    }

    /**
     * 補償や終了状態の情報を除いた、基本的なフィールドでSagaインスタンスを初期化します。
     *
     * @param sagaType Sagaの種類
     * @param sagaId SagaのID
     * @param stateName 現在の状態
     * @param lastRequestId 最後のリクエストID
     * @param serializedSagaData Sagaのシリアル化されたデータ
     * @param destinationsAndResources 目的地とリソースのセット
     */
    public SagaInstance(String sagaType, String sagaId, String stateName, String lastRequestId, SerializedSagaData serializedSagaData, Set<DestinationAndResource> destinationsAndResources) {
        this.sagaType = sagaType;
        this.id = sagaId;
        this.stateName = stateName;
        this.lastRequestId = lastRequestId;
        this.serializedSagaData = serializedSagaData;
        this.destinationsAndResources = destinationsAndResources;
    }

    // GetterとSetterメソッド
    public String getSagaType() {
        return sagaType;
    }

    public void setSagaType(String sagaType) {
        this.sagaType = sagaType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastRequestId() {
        return lastRequestId;
    }

    public void setLastRequestId(String requestId) {
        this.lastRequestId = requestId;
    }

    public SerializedSagaData getSerializedSagaData() {
        return serializedSagaData;
    }

    public void setSerializedSagaData(SerializedSagaData serializedSagaData) {
        this.serializedSagaData = serializedSagaData;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Set<DestinationAndResource> getDestinationsAndResources() {
        return destinationsAndResources;
    }

    public void addDestinationsAndResources(Set<DestinationAndResource> destinationAndResources) {
        this.destinationsAndResources.addAll(destinationAndResources);
    }

    public Boolean isEndState() {
        return endState;
    }

    public void setEndState(Boolean endState) {
        this.endState = endState;
    }

    public Boolean isCompensating() {
        return compensating;
    }

    public void setCompensating(Boolean compensating) {
        this.compensating = compensating;
    }

    public Boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }
}
