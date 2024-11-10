package org.cresplanex.core.saga.simpledsl;

import java.util.Set;

import org.cresplanex.core.commands.common.Command;

/**
 * コマンドのエンドポイントを表現するクラスです。
 *
 * @param <C> コマンドのクラス
 */
public class CommandEndpoint<C extends Command> {

    private final String commandChannel;
    private final Class<C> commandClass;
    private String commandType;
    private final Set<Class<?>> replyClasses;

    /**
     * コンストラクタ。
     *
     * @param commandChannel コマンドを送信するチャンネル名
     * @param commandClass コマンドのクラス
     * @param replyClasses 受信する返信クラスのセット
     */
    public CommandEndpoint(String commandChannel, Class<C> commandClass, Set<Class<?>> replyClasses) {
        this.commandChannel = commandChannel;
        this.commandClass = commandClass;
        this.replyClasses = replyClasses;
    }

    /**
     * コンストラクタ。
     *
     * @param commandChannel コマンドを送信するチャンネル名
     * @param commandClass コマンドのクラス
     * @param replyClasses 受信する返信クラスのセット
     * @param commandType コマンドタイプ
     */
    public CommandEndpoint(String commandChannel, Class<C> commandClass, String commandType, Set<Class<?>> replyClasses) {
        this.commandChannel = commandChannel;
        this.commandClass = commandClass;
        this.replyClasses = replyClasses;
        this.commandType = commandType;
    }

    /**
     * コマンドチャンネルを取得します。
     *
     * @return コマンドチャンネル
     */
    public String getCommandChannel() {
        return commandChannel;
    }

    /**
     * コマンドクラスを取得します。
     *
     * @return コマンドクラス
     */
    public Class<C> getCommandClass() {
        return commandClass;
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
     * 受信する返信クラスのセットを取得します。
     *
     * @return 返信クラスのセット
     */
    public Set<Class<?>> getReplyClasses() {
        return replyClasses;
    }
}
