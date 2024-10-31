package org.cresplanex.core.commands.consumer;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;

/**
 * コマンドハンドラの引数を格納するクラス。コマンドメッセージ、パス変数、 コマンド返信トークンを含みます。
 *
 * @param <CommandType> 処理するコマンドのタイプ
 */
public class CommandHandlerArgs<CommandType extends Command> {

    private final CommandMessage<CommandType> commandMessage;
    private final PathVariables pathVars;
    private final CommandReplyToken commandReplyToken;

    /**
     * CommandHandlerArgs のインスタンスを生成します。
     *
     * @param commandMessage 処理対象のコマンドメッセージ
     * @param pathVars パス変数
     * @param commandReplyToken コマンド返信トークン
     */
    public CommandHandlerArgs(CommandMessage<CommandType> commandMessage, PathVariables pathVars, CommandReplyToken commandReplyToken) {
        this.commandMessage = commandMessage;
        this.pathVars = pathVars;
        this.commandReplyToken = commandReplyToken;
    }

    /**
     * BiFunction から Function を作成し、ハンドラの適用を簡単にします。
     *
     * @param <C> コマンドの型
     * @param <Result> 結果の型
     * @param handler コマンドメッセージとパス変数を引数に取るハンドラ
     * @return CommandHandlerArgs を引数に取る Function
     */
    public static <C extends Command, Result> Function<CommandHandlerArgs<C>, Result> makeFn(BiFunction<CommandMessage<C>, PathVariables, Result> handler) {
        return args -> handler.apply(args.getCommandMessage(), args.getPathVars());
    }

    /**
     * コマンドメッセージを取得します。
     *
     * @return コマンドメッセージ
     */
    public CommandMessage<CommandType> getCommandMessage() {
        return commandMessage;
    }

    /**
     * パス変数を取得します。
     *
     * @return パス変数
     */
    public PathVariables getPathVars() {
        return pathVars;
    }

    /**
     * コマンド返信トークンを取得します。
     *
     * @return コマンド返信トークン
     */
    public CommandReplyToken getCommandReplyToken() {
        return commandReplyToken;
    }

}
