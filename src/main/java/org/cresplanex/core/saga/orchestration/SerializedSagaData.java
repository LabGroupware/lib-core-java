package org.cresplanex.core.saga.orchestration;

public class SerializedSagaData {

    private final String sagaDataType;
    private final String sagaDataJSON;

    public SerializedSagaData(String sagaDataType, String sagaDataJSON) {
        this.sagaDataType = sagaDataType;
        this.sagaDataJSON = sagaDataJSON;
    }

    public String getSagaDataJSON() {
        return sagaDataJSON;
    }

    public String getSagaDataType() {
        return sagaDataType;
    }
}
