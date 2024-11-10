package org.cresplanex.core.saga.orchestration.command;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandWithDestination;

/**
 * コマンドとその送信先、タイプ（コマンドか通知）を表すクラス。
 */
public class CommandWithDestinationAndType {

    private final CommandWithDestination commandWithDestination;
    private final boolean notification;

    /**
     * 指定したチャンネルとリソースにコマンドを送信するインスタンスを作成します。
     *
     * @param channel 送信先チャンネル
     * @param resource 送信リソース
     * @param command 送信するコマンド
     * @param commandType コマンドのタイプ
     * @return コマンドとして送信されるインスタンス
     */
    public static CommandWithDestinationAndType command(String channel, String resource, Command command, String commandType) {
        return command(new CommandWithDestination(channel, resource, command, commandType));
    }

    /**
     * 指定した送信先コマンドでコマンドとして送信するインスタンスを作成します。
     *
     * @param command 送信先コマンド
     * @return コマンドとして送信されるインスタンス
     */
    public static CommandWithDestinationAndType command(CommandWithDestination command) {
        return new CommandWithDestinationAndType(command, false);
    }

    /**
     * 指定したチャンネルとリソースに通知を送信するインスタンスを作成します。
     *
     * @param channel 送信先チャンネル
     * @param resource 送信リソース
     * @param command 送信するコマンド
     * @return 通知として送信されるインスタンス
     */
    public static CommandWithDestinationAndType notification(String channel, String resource, Command command) {
        return notification(new CommandWithDestination(channel, resource, command));
    }

    /**
     * 指定した送信先コマンドで通知として送信するインスタンスを作成します。
     *
     * @param command 送信先コマンド
     * @return 通知として送信されるインスタンス
     */
    public static CommandWithDestinationAndType notification(CommandWithDestination command) {
        return new CommandWithDestinationAndType(command, true);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * コンストラクタ。指定した送信先コマンドと通知フラグでインスタンスを作成します。
     *
     * @param commandWithDestination 送信先のコマンド
     * @param notification 通知フラグ
     */
    public CommandWithDestinationAndType(CommandWithDestination commandWithDestination, boolean notification) {
        this.commandWithDestination = commandWithDestination;
        this.notification = notification;
    }

    /**
     * 送信先のコマンドを取得します。
     *
     * @return 送信先のコマンド
     */
    public CommandWithDestination getCommandWithDestination() {
        return commandWithDestination;
    }

    /**
     * 通知であるかどうかを判定します。
     *
     * @return 通知であればtrue、コマンドであればfalse
     */
    public boolean isNotification() {
        return notification;
    }

    /**
     * コマンドであるかどうかを判定します。
     *
     * @return コマンドであればtrue、通知であればfalse
     */
    public boolean isCommand() {
        return !isNotification();
    }
}
