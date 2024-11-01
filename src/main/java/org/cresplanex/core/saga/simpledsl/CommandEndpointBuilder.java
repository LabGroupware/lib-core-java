package org.cresplanex.core.saga.simpledsl;

import java.util.HashSet;
import java.util.Set;

import org.cresplanex.core.commands.common.Command;

/**
 * CommandEndpointを構築するためのビルダークラスです。
 *
 * @param <C> コマンドのクラス
 */
public class CommandEndpointBuilder<C extends Command> {

    private String channel;
    private final Class<C> commandClass;
    private final Set<Class<?>> replyClasses = new HashSet<>();

    /**
     * コンストラクタ。
     *
     * @param commandClass コマンドのクラス
     */
    public CommandEndpointBuilder(Class<C> commandClass) {
        this.commandClass = commandClass;
    }

    /**
     * コマンドクラスに対してビルダーを初期化します。
     *
     * @param commandClass コマンドのクラス
     * @param <C> コマンドのクラス
     * @return CommandEndpointBuilderのインスタンス
     */
    public static <C extends Command> CommandEndpointBuilder<C> forCommand(Class<C> commandClass) {
        return new CommandEndpointBuilder<>(commandClass);
    }

    /**
     * チャンネル名を設定します。
     *
     * @param channel チャンネル名
     * @return 更新されたCommandEndpointBuilderインスタンス
     */
    public CommandEndpointBuilder<C> withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    /**
     * 返信クラスを追加します。
     *
     * @param replyClass 返信クラス
     * @param <T> 返信クラスの型
     * @return 更新されたCommandEndpointBuilderインスタンス
     */
    public <T> CommandEndpointBuilder<C> withReply(Class<T> replyClass) {
        this.replyClasses.add(replyClass);
        return this;
    }

    /**
     * CommandEndpointのインスタンスを構築します。
     *
     * @return 新規のCommandEndpointインスタンス
     */
    public CommandEndpoint<C> build() {
        return new CommandEndpoint<>(channel, commandClass, replyClasses);
    }
}
