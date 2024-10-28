package org.cresplanex.core.common.json.mapper;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/**
 * JSONのシリアライズおよびデシリアライズ用のユーティリティクラス。
 * <p>
 * Jacksonの {@link ObjectMapper} を設定し、シリアライズとデシリアライズの操作を提供します。
 * </p>
 */
public class JSonMapper {

    /**
     * JSON操作に使用する {@link ObjectMapper} のインスタンス。
     * 構成済みであり、BigDecimalのフォーマット、未知プロパティの無視、空オブジェクトの許容などの設定が含まれています。
     */
    final public static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(com.fasterxml.jackson.core.JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new Int128Module());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_ABSENT);
        objectMapper.registerModule(new Jdk8Module());
    }

    /**
     * オブジェクトをJSON文字列に変換します。
     *
     * @param x JSONにシリアライズするオブジェクト
     * @return 変換されたJSON文字列
     * @throws RuntimeException シリアライズ中にエラーが発生した場合
     */
    public static String toJson(Object x) {
        try {
            return objectMapper.writeValueAsString(x);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON文字列を指定された型にデシリアライズします。
     *
     * @param json JSON文字列
     * @param targetType デシリアライズ後のオブジェクト型
     * @param <T> デシリアライズするオブジェクトの型
     * @return デシリアライズされたオブジェクト
     * @throws RuntimeException デシリアライズ中にエラーが発生した場合
     */
    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * クラス名を指定してJSON文字列をデシリアライズします。
     * <p>
     * クラス名を文字列で指定し、リフレクションを使用してデシリアライズする方法です。
     * </p>
     *
     * @param json JSON文字列
     * @param targetType デシリアライズ後のオブジェクト型の完全修飾名
     * @param <T> デシリアライズするオブジェクトの型
     * @return デシリアライズされたオブジェクト
     * @throws RuntimeException デシリアライズ中またはクラスが見つからない場合に発生する例外
     */
    public static <T> T fromJsonByName(String json, String targetType) {
        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(targetType);
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructType(clazz));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
