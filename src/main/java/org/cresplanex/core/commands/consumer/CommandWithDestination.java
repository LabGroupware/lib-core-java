package org.cresplanex.core.commands.consumer;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.commands.common.Command;

/**
 * コマンド送信先情報を保持するクラス。 宛先チャネル、リソース、コマンド、および追加のヘッダーを持ちます。
 */
public class CommandWithDestination {

    private final String destinationChannel;
    private final String resource;
    private final Command command;
    private final String commandType;
    private final Map<String, String> extraHeaders;

    /**
     * 指定されたパラメータで新しいインスタンスを作成します。
     *
     * @param destinationChannel 宛先チャネル
     * @param resource リソースパス
     * @param command 実行するコマンド
     * @param extraHeaders 追加のヘッダー
     */
    public CommandWithDestination(String destinationChannel, String resource, Command command, Map<String, String> extraHeaders) {
        this(destinationChannel, resource, command, extraHeaders, null);
    }

    /**
     * 指定されたパラメータで新しいインスタンスを作成します。
     *
     * @param destinationChannel 宛先チャネル
     * @param resource リソースパス
     * @param command 実行するコマンド
     * @param extraHeaders 追加のヘッダー
     * @param commandType コマンドタイプ
     */
    public CommandWithDestination(String destinationChannel, String resource, Command command, Map<String, String> extraHeaders, String commandType) {
        this.destinationChannel = destinationChannel;
        this.resource = resource;
        this.command = command;
        this.extraHeaders = extraHeaders;
        this.commandType = commandType;
    }

    /**
     * 宛先チャネルとリソース、およびコマンドのみで新しいインスタンスを作成します。
     *
     * @param destinationChannel 宛先チャネル
     * @param resource リソースパス
     * @param command 実行するコマンド
     */
    public CommandWithDestination(String destinationChannel, String resource, Command command) {
        this(destinationChannel, resource, command, Collections.emptyMap());
    }

    /**
     * 宛先チャネルとリソース、およびコマンドのみで新しいインスタンスを作成します。
     *
     * @param destinationChannel 宛先チャネル
     * @param resource リソースパス
     * @param command 実行するコマンド
     * @param commandType コマンドタイプ
     */
    public CommandWithDestination(String destinationChannel, String resource, Command command, String commandType) {
        this(destinationChannel, resource, command, Collections.emptyMap(), commandType);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * 宛先チャネルを取得します。
     *
     * @return 宛先チャネル
     */
    public String getDestinationChannel() {
        return destinationChannel;
    }

    /**
     * リソースパスを取得します。
     *
     * @return リソースパス
     */
    public String getResource() {
        return resource;
    }

    /**
     * コマンドを取得します。
     *
     * @return コマンド
     */
    public Command getCommand() {
        return command;
    }

    /**
     * コマンドタイプを取得します。
     *
     * @return コマンドタイプ
     */
    public String getCommandType() {
        return commandType;
    }

    /**
     * 追加のヘッダーを取得します。
     *
     * @return 追加のヘッダー
     */
    public Map<String, String> getExtraHeaders() {
        return extraHeaders;
    }
}
