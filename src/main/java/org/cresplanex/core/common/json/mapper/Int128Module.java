package org.cresplanex.core.common.json.mapper;

import java.io.IOException;

import org.cresplanex.core.common.id.Int128;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

/**
 * Jackson用のカスタムモジュールで、Int128型のシリアライズおよびデシリアライズ機能を提供します。
 * <p>
 * このモジュールは、Int128型をJSON文字列として処理するためのカスタムシリアライザとデシリアライザを追加します。
 * </p>
 */
public class Int128Module extends SimpleModule {

    /**
     * Int128型のカスタムデシリアライザ。
     * <p>
     * JSON文字列からInt128オブジェクトを生成するデシリアライザです。
     * </p>
     */
    class IdDeserializer extends StdScalarDeserializer<Int128> {

        /**
         * Int128型のデシリアライザのコンストラクタ。
         */
        public IdDeserializer() {
            super(Int128.class);
        }

        /**
         * JSON文字列をInt128オブジェクトにデシリアライズします。
         *
         * @param jp JSONパーサ
         * @param ctxt デシリアライズコンテキスト
         * @return デシリアライズされたInt128オブジェクト
         * @throws IOException パース中にエラーが発生した場合
         */
        @Override
        public Int128 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonToken token = jp.getCurrentToken();
            if (token == JsonToken.VALUE_STRING) {
                String str = jp.getText().trim();
                if (str.isEmpty()) {
                    return null; 
                }else {
                    return Int128.fromString(str);
                }
            } else {
                throw ctxt.wrongTokenException(jp, handledType(), JsonToken.VALUE_STRING, "expected JSON String");
            }
        }
    }

    /**
     * Int128型のカスタムシリアライザ。
     * <p>
     * Int128オブジェクトをJSON文字列としてシリアライズします。
     * </p>
     */
    class IdSerializer extends StdScalarSerializer<Int128> {

        /**
         * Int128型のシリアライザのコンストラクタ。
         */
        public IdSerializer() {
            super(Int128.class);
        }

        /**
         * Int128オブジェクトをJSON文字列にシリアライズします。
         *
         * @param value シリアライズ対象のInt128オブジェクト
         * @param jgen JSONジェネレータ
         * @param provider シリアライゼーションプロバイダ
         * @throws IOException シリアライズ中にエラーが発生した場合
         */
        @Override
        public void serialize(Int128 value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(value.asString());
        }
    }

    /**
     * このモジュールの名前を返します。
     *
     * @return モジュール名 "IdJsonModule"
     */
    @Override
    public String getModuleName() {
        return "IdJsonModule";
    }

    /**
     * Int128Moduleのコンストラクタ。
     * <p>
     * Int128型用のカスタムシリアライザとデシリアライザを設定します。
     * </p>
     */
    public Int128Module() {
        addDeserializer(Int128.class, new IdDeserializer());
        addSerializer(Int128.class, new IdSerializer());
    }
}
