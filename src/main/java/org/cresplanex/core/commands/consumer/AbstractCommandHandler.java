package org.cresplanex.core.commands.consumer;

import java.util.Optional;
import java.util.function.Function;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandMessageHeaders;
import org.cresplanex.core.commands.common.paths.ResourcePath;
import org.cresplanex.core.commands.common.paths.ResourcePathPattern;
import org.cresplanex.core.messaging.common.Message;

/**
 * コマンド処理を抽象化するためのクラス。特定のチャンネル、リソースパス、コマンドタイプに基づいて メッセージの処理を定義します。
 *
 * @param <RESULT> コマンド処理結果の型
 */
public class AbstractCommandHandler<RESULT> {

    private final String channel;
    private final Optional<String> resource;
    private final Class<? extends Command> commandClass;
    private final Function<CommandHandlerArgs<Command>, RESULT> handler;

    /**
     * AbstractCommandHandlerのコンストラクタ。
     *
     * @param channel チャンネル名
     * @param resource リソースパス（オプション）
     * @param commandClass コマンドのクラス型
     * @param handler コマンド処理を行う関数
     * @param <C> コマンドの型
     */
    @SuppressWarnings("unchecked")
    public <C extends Command> AbstractCommandHandler(String channel, Optional<String> resource,
            Class<C> commandClass,
            Function<CommandHandlerArgs<C>, RESULT> handler) {
        this.channel = channel;
        this.resource = resource;
        this.commandClass = commandClass;
        this.handler = (CommandHandlerArgs<Command> args) -> handler.apply((CommandHandlerArgs<C>) args);
    }

    /**
     * 処理対象のチャンネルを取得します。
     *
     * @return チャンネル名
     */
    public String getChannel() {
        return channel;
    }

    /**
     * メッセージがこのハンドラーによって処理可能かを確認します。
     *
     * @param message 処理対象のメッセージ
     * @return 処理可能な場合はtrue、そうでない場合はfalse
     */
    public boolean handles(Message message) {
        return commandTypeMatches(message) && resourceMatches(message);
    }

    /**
     * メッセージのリソースが一致するかを確認します。
     *
     * ハンドラのリソースが存在しない場合は、常にtrueを返します。 要するに, ハンドラでのリソースの指定がない場合は,
     * 全てのリソースに対して処理を行うことを意味します。 また, resourceMatches(String messageResource,
     * String methodPath)の使用上, ハンドラにはプレースホルダ'{}'を利用することができる.
     *
     * @param message 処理対象のメッセージ
     * @return リソースが一致する場合はtrue、そうでない場合はfalse
     */
    private boolean resourceMatches(Message message) {
        return !resource.isPresent() || message.getHeader(CommandMessageHeaders.RESOURCE).map(m -> resourceMatches(m, resource.get())).orElse(false);
    }

    /**
     * メッセージのコマンドタイプが一致するかを確認します。
     *
     * @param message 処理対象のメッセージ
     * @return コマンドタイプが一致する場合はtrue、そうでない場合はfalse
     */
    private boolean commandTypeMatches(Message message) {
        return commandClass.getName().equals(
                message.getRequiredHeader(CommandMessageHeaders.COMMAND_TYPE));
    }

    /**
     * リソースパスのパターンが一致するかを確認します。
     *
     * @param messageResource メッセージのリソースパス
     * @param methodPath メソッドのリソースパス
     * @return リソースパスが一致する場合はtrue、そうでない場合はfalse
     */
    private boolean resourceMatches(String messageResource, String methodPath) {
        ResourcePathPattern r = ResourcePathPattern.parse(methodPath);
        ResourcePath mr = ResourcePath.parse(messageResource);
        return r.isSatisfiedBy(mr);
    }

    /**
     * コマンドのクラス型を取得します。
     *
     * @return コマンドのクラス型
     */
    public Class<? extends Command> getCommandClass() {
        return commandClass;
    }

    /**
     * リソースパスのオプションを取得します。
     *
     * @return リソースパスのオプション
     */
    public Optional<String> getResource() {
        return resource;
    }

    /**
     * コマンドハンドラーのメソッドを実行し、結果を返します。
     *
     * @param commandHandlerArgs コマンドハンドラーの引数
     * @return コマンド処理の結果
     */
    public RESULT invokeMethod(CommandHandlerArgs<Command> commandHandlerArgs) {
        return (RESULT) handler.apply(commandHandlerArgs);
    }
}
