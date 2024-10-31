package org.cresplanex.core.commands.consumer;

import java.util.Collections;
import java.util.Map;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.paths.ResourcePathPattern;

/**
 * コマンド送信先を構築するためのビルダークラス。 宛先チャネルやリソース、追加のヘッダーを設定できます。
 */
public class CommandWithDestinationBuilder {

    private final Command command;
    private String destinationChannel;
    private String resource;
    private Map<String, String> extraHeaders = Collections.emptyMap();

    /**
     * 指定されたコマンドでビルダーを初期化します。
     *
     * @param command 実行するコマンド
     */
    public CommandWithDestinationBuilder(Command command) {
        this.command = command;
    }

    /**
     * コマンド送信先のビルダーを開始します。
     *
     * @param command 実行するコマンド
     * @return コマンド送信先ビルダー
     */
    public static CommandWithDestinationBuilder send(Command command) {
        return new CommandWithDestinationBuilder(command);
    }

    /**
     * 宛先チャネルを設定します。
     *
     * @param destinationChannel 宛先チャネル
     * @return ビルダー自身を返し、メソッドチェーンを可能にします
     */
    public CommandWithDestinationBuilder to(String destinationChannel) {
        this.destinationChannel = destinationChannel;
        return this;
    }

    /**
     * リソースとパスパラメータを設定します。
     *
     * @param resource リソースのパスパターン
     * @param pathParams リソースに必要なパスパラメータ
     * @return ビルダー自身を返し、メソッドチェーンを可能にします
     */
    public CommandWithDestinationBuilder forResource(String resource, Object... pathParams) {
        this.resource = new ResourcePathPattern(resource).replacePlaceholders(pathParams).toPath();
        return this;
    }

    /**
     * 追加のヘッダーを設定します。
     *
     * @param headers 追加するヘッダーのマッピング
     * @return ビルダー自身を返し、メソッドチェーンを可能にします
     */
    public CommandWithDestinationBuilder withExtraHeaders(Map<String, String> headers) {
        this.extraHeaders = headers;
        return this;
    }

    /**
     * 構築されたコマンド送信先を返します。
     *
     * @return 設定されたプロパティを持つ `CommandWithDestination`
     */
    public CommandWithDestination build() {
        return new CommandWithDestination(destinationChannel, resource, command, extraHeaders);
    }
}
