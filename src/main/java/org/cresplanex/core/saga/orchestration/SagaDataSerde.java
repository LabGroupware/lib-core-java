package org.cresplanex.core.saga.orchestration;

import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * サーガデータのシリアル化とデシリアル化を行うユーティリティクラス。
 */
public class SagaDataSerde {

    private final static Logger logger = LoggerFactory.getLogger(SagaDataSerde.class);

    /**
     * サーガデータをシリアル化して {@link SerializedSagaData} オブジェクトに変換します。
     *
     * @param <Data> シリアル化するサーガデータの型
     * @param sagaData シリアル化するサーガデータ
     * @return シリアル化されたサーガデータ
     */
    public static <Data> SerializedSagaData serializeSagaData(Data sagaData) {
        return new SerializedSagaData(sagaData.getClass().getName(), JSonMapper.toJson(sagaData));
    }

    /**
     * シリアル化されたサーガデータをデシリアル化して、オリジナルのサーガデータ型に変換します。
     *
     * @param <Data> デシリアル化するサーガデータの型
     * @param serializedSagaData デシリアル化するシリアル化済みサーガデータ
     * @return デシリアル化されたサーガデータ
     * @throws RuntimeException クラスが見つからない場合に発生する例外
     */
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
