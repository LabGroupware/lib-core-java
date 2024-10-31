package org.cresplanex.core.commands.consumer;

import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.messaging.common.Message;

/**
 * コマンドメッセージを保持するクラス。 メッセージID、コマンド本体、相関ヘッダー、およびメッセージデータを含みます。
 *
 * @param <T> コマンドのタイプ
 */
public class CommandMessage<T> {

    private final String messageId;
    private final T command;
    private final Map<String, String> correlationHeaders;
    private final Message message;

    /**
     * メッセージID、コマンド、相関ヘッダー、メッセージを指定してインスタンスを作成します。
     *
     * @param messageId メッセージID
     * @param command コマンド本体
     * @param correlationHeaders 相関ヘッダー
     * @param message メッセージデータ
     */
    public CommandMessage(String messageId, T command, Map<String, String> correlationHeaders, Message message) {
        this.messageId = messageId;
        this.command = command;
        this.correlationHeaders = correlationHeaders;
        this.message = message;
    }

    /**
     * メッセージデータを取得します。
     *
     * @return メッセージ
     */
    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * メッセージIDを取得します。
     *
     * @return メッセージID
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * コマンドを取得します。
     *
     * @return コマンド
     */
    public T getCommand() {
        return command;
    }

    /**
     * 相関ヘッダーを取得します。
     *
     * @return 相関ヘッダーのマップ
     */
    public Map<String, String> getCorrelationHeaders() {
        return correlationHeaders;
    }
}
