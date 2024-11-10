package org.cresplanex.core.commands.producer;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandNameMapping;
import static org.cresplanex.core.commands.producer.CommandMessageFactory.makeMessage;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.producer.MessageProducer;

/**
 * コマンド送信を実装するクラスです。
 */
public class CommandProducerImpl implements CommandProducer {

    private final MessageProducer messageProducer;
    private final CommandNameMapping commandNameMapping;

    /**
     * コンストラクタ。MessageProducerおよびCommandNameMappingのインスタンスを受け取ります。
     *
     * @param messageProducer メッセージ送信機能を提供するMessageProducer
     * @param commandNameMapping コマンド名のマッピングを提供するCommandNameMapping
     */
    public CommandProducerImpl(MessageProducer messageProducer, CommandNameMapping commandNameMapping) {
        this.messageProducer = messageProducer;
        this.commandNameMapping = commandNameMapping;
    }

    /**
     * コマンドを指定されたチャンネルに送信します。
     *
     * @param channel 送信先のチャンネル
     * @param command 送信するコマンド
     * @param replyTo 応答先のチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    @Override
    public String send(String channel, Command command, String replyTo, Map<String, String> headers) {
        return send(channel, null, command, replyTo, headers);
    }

    /**
     * 通知としてコマンドを送信します。
     *
     * @param channel 送信先のチャンネル
     * @param command 送信するコマンド
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    @Override
    public String sendNotification(String channel, Command command, Map<String, String> headers) {
        return send(channel, null, command, null, headers);
    }

    /**
     * コマンドタイプを指定して通知としてコマンドを送信します。
     *
     * @param channel 送信先のチャンネル
     * @param resource 対象リソース
     * @param command 送信するコマンド
     * @param commandType コマンドタイプ
     * @param replyTo 応答先のチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    @Override
    public String sendNotification(String channel, String resource, Command command, String commandType, String replyTo, Map<String, String> headers) {
        return send(channel, resource, command, commandType, replyTo, headers);
    }

    /**
     * リソースを指定してコマンドを送信します。
     *
     * @param channel 送信先のチャンネル
     * @param resource 対象リソース
     * @param command 送信するコマンド
     * @param replyTo 応答先のチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    @Override
    public String send(String channel, String resource, Command command, String replyTo, Map<String, String> headers) {
        Message message = makeMessage(commandNameMapping, channel, resource, command, replyTo, headers);
        messageProducer.send(channel, message);
        return message.getId();
    }

    @Override
    public String send(String channel, String resource, Command command, String commandType, String replyTo, Map<String, String> headers) {
        Message message = makeMessage(channel, resource, command, replyTo, headers, commandType);
        messageProducer.send(channel, message);
        return message.getId();
    }
}
