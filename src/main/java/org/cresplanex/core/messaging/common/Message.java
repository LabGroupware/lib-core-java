package org.cresplanex.core.messaging.common;

import java.util.Map;
import java.util.Optional;

/**
 * A message that can be sent.
 *
 * 送信可能なメッセージ.
 */
public interface Message {

    /**
     * Returns the ID of the message.
     *
     * メッセージのIDを返します.
     *
     * @return the ID of the message
     */
    String getId();

    /**
     * Returns the map of headers.
     *
     * ヘッダーのマップを返します.
     *
     * @return the map of headers
     */
    Map<String, String> getHeaders();

    /**
     * Returns the payload of the message.
     *
     * メッセージのペイロードを返します.
     *
     * @return the payload of the message
     */
    String getPayload();

    // 決定ヘッダー情報
    String ID = "ID";
    String PARTITION_ID = "PARTITION_ID";
    String DESTINATION = "DESTINATION";
    String DATE = "DATE";

    /**
     * Returns the value of the header with the given name.
     *
     * 指定された名前のヘッダーの値を返します.
     *
     * @param name the name of the header
     * @return the value of the header with the given name
     */
    Optional<String> getHeader(String name);

    /**
     * Returns the value of the header with the given name. If the header is not
     * found, an exception is thrown.
     *
     * 指定された名前のヘッダーの値を返します. ヘッダーが見つからない場合は例外がスローされます.
     *
     * @param name the name of the header
     * @return the value of the header with the given name
     */
    String getRequiredHeader(String name);

    /**
     * Returns whether the message has a header with the given name.
     *
     * メッセージに指定された名前のヘッダーがあるかどうかを返します.
     *
     * @param name the name of the header
     * @return whether the message has a header with the given name
     */
    boolean hasHeader(String name);

    /**
     * Sets the Payload of the message.
     *
     * メッセージのペイロードを設定します.
     *
     * @param payload the payload of the message
     */
    void setPayload(String payload);

    /**
     * Sets the Headers of the message.
     *
     * メッセージのヘッダーを設定します.
     *
     * @param headers the headers of the message
     */
    
    void setHeaders(Map<String, String> headers);
    /**
     * Sets the value of the header with the given name.
     *
     * 指定された名前のヘッダーの値を設定します.
     *
     * @param name the name of the header
     * @param value the value of the header
     */
    
    void setHeader(String name, String value);
    /**
     * Removes the header with the given name.
     *
     * 指定された名前のヘッダーを削除します.
     *
     * @param key the name of the header
     */
    
    void removeHeader(String key);
}
