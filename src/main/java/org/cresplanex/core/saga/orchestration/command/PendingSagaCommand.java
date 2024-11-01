package org.cresplanex.core.saga.orchestration.command;

import org.cresplanex.core.commands.common.Command;

/**
 * サーガ内で保留中のコマンド情報を保持するクラス。
 */
public class PendingSagaCommand {

    private final String destination;
    private final String resource;
    private final Command command;

    /**
     * 指定された宛先、リソース、およびコマンドでインスタンスを作成します。
     *
     * @param destination コマンドを送信する宛先
     * @param resource コマンドに関連するリソース
     * @param command 実行するコマンド
     */
    public PendingSagaCommand(String destination, String resource, Command command) {
        this.destination = destination;
        this.resource = resource;
        this.command = command;
    }

    /**
     * コマンドの宛先を取得します。
     *
     * @return コマンドの宛先
     */
    public String getDestination() {
        return destination;
    }

    /**
     * コマンドに関連するリソースを取得します。
     *
     * @return コマンドに関連するリソース
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
}
