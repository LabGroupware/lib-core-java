package org.cresplanex.core.common.kafka.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.cresplanex.core.messaging.common.MessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Kafkaメッセージを表すクラス。
 * <p>
 * Kafkaメッセージのペイロード（内容）を保持します。</p>
 */
public class KafkaMessage {

    private static final Logger log = LoggerFactory.getLogger(KafkaMessage.class);
    /**
     * メッセージの内容を表す文字列。
     */
    private final String payload;

    /**
     * KafkaMessageのコンストラクタ。
     *
     * @param payload メッセージの内容
     */
    public KafkaMessage(String payload) {
        this.payload = payload;
    }

    /**
     * メッセージの内容を取得します。
     *
     * @return メッセージの内容
     */
    public String getPayload() {
        return payload;
    }

    /**
     * JSON内の"payload"フィールドの内容を取得して返すメソッド
     *
     * @return "payload"フィールドの内容（JSON文字列）、存在しない場合はnull
     */
    public MessageImpl getPayloadContent() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(payload);
            JsonNode payloadNode = rootNode.path("payload"); // "payload"フィールドを取得

            if (payloadNode.isMissingNode()) {
                return null;
            }
            JsonNode headersNode = payloadNode.path("headers");
            JsonNode payloadOnlyNode = payloadNode.path("payload");

            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(Map.class, String.class, String.class);

            // TODO: Escape文字列があっても正常に動作する?
            String payloadStr = payloadOnlyNode.toString();
            payloadStr = payloadStr.substring(1, payloadStr.length() - 1).replace("\\\"", "\"");

            String headersStr = headersNode.toString();
            headersStr = headersStr.substring(1, headersStr.length() - 1).replace("\\\"", "\"");

            Map<String, String> headers = mapper.readValue(headersStr, mapType);

            return  new MessageImpl(payloadStr, headers);
        } catch (Exception e) {
            return null;
        }
    }
}
