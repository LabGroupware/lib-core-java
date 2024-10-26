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

public class Int128Module extends SimpleModule {

  class IdDeserializer extends StdScalarDeserializer<Int128> {

    public IdDeserializer() {
      super(Int128.class);
    }

    @Override
    public Int128 deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
      JsonToken token = jp.getCurrentToken();
      if (token == JsonToken.VALUE_STRING) {
        String str = jp.getText().trim();
        if (str.isEmpty())
          return null;
        else
          return Int128.fromString(str);
      } else

      throw ctxt.wrongTokenException(jp, handledType(), JsonToken.VALUE_STRING, "expected JSON String");
    }
  }

  class IdSerializer extends StdScalarSerializer<Int128> {
    public IdSerializer() {
      super(Int128.class);
    }

    @Override
    public void serialize(Int128 value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
      jgen.writeString(value.asString());
    }

    // @Override
    // public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
    //   return createSchemaNode("string", true);
    // }
  }

  @Override
  public String getModuleName() {
    return "IdJsonModule";
  }

  public Int128Module() {
    addDeserializer(Int128.class, new IdDeserializer());
    addSerializer(Int128.class, new IdSerializer());
  }

}