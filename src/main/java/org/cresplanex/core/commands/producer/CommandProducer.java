package org.cresplanex.core.commands.producer;

import java.util.Map;

import org.cresplanex.core.commands.common.Command;

public interface CommandProducer {

    /**
     * コマンドを送信します。
     *
     * @param channel コマンドを送信するチャンネル
     * @param command 送信するコマンド
     * @param replyTo 応答を送信するチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    String send(String channel, Command command, String replyTo,
            Map<String, String> headers);

    /**
     * コマンドを通知として送信します。
     *
     * @param channel コマンドを送信するチャンネル
     * @param command 送信するコマンド
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    String sendNotification(String channel, Command command,
            Map<String, String> headers);

    /**
     * コマンドタイプを指定してコマンドを通知として送信します。
     *
     * @param channel コマンドを送信するチャンネル
     * @param resource 対象リソース
     * @param command 送信するコマンド
     * @param replyTo 応答を送信するチャンネル
     * @param headers 追加のヘッダー情報
     * @param commandType コマンドタイプ
     * @return 送信されたコマンドのID
     */
    String sendNotification(String channel, String resource, Command command,
            String commandType, String replyTo, Map<String, String> headers);

    /**
     * リソースを指定してコマンドを送信します。
     *
     * @param channel コマンドを送信するチャンネル
     * @param resource 対象リソース
     * @param command 送信するコマンド
     * @param replyTo 応答を送信するチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    String send(String channel, String resource, Command command, String replyTo, Map<String, String> headers);

    /**
     * コマンドタイプを指定してコマンドを送信します。
     *
     * @param channel コマンドを送信するチャンネル
     * @param resource 対象リソース
     * @param command 送信するコマンド
     * @param commandType コマンドタイプ
     * @param replyTo 応答を送信するチャンネル
     * @param headers 追加のヘッダー情報
     * @return 送信されたコマンドのID
     */
    String send(String channel, String resource, Command command, String commandType, String replyTo, Map<String, String> headers);
}
