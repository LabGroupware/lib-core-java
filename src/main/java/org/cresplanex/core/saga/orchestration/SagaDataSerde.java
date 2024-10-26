package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SagaDataSerde {

    private final static Logger logger = LoggerFactory.getLogger(SagaDataSerde.class);

    public static <Data> SerializedSagaData serializeSagaData(Data sagaData) {
        return new SerializedSagaData(sagaData.getClass().getName(), JSonMapper.toJson(sagaData));
    }

    @SuppressWarnings("unchecked")
    public static <Data> Data deserializeSagaData(SerializedSagaData serializedSagaData) {
        Class<?> clasz = null;
        try {
            clasz = Thread.currentThread().getContextClassLoader().loadClass(serializedSagaData.getSagaDataType());
        } catch (ClassNotFoundException e) {
            logger.error("Class not found", e);
            throw new RuntimeException("Class not found", e);
        }
        Object x = JSonMapper.fromJson(serializedSagaData.getSagaDataJSON(), clasz);
        return (Data) x;
    }
}
