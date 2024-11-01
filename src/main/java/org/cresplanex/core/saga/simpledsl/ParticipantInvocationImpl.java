package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.common.CommandReplyOutcome;
import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.commands.consumer.CommandWithDestination;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.command.CommandWithDestinationAndType;

/**
 * サガにおいて参加者のアクションを表現するクラスです。
 *
 * @param <Data> サガデータのタイプ
 * @param <C> コマンドのタイプ
 */
public class ParticipantInvocationImpl<Data, C extends Command> extends AbstractParticipantInvocation<Data> {

    private final boolean notification;
    private final Function<Data, CommandWithDestination> commandBuilder;

    /**
     * コンストラクタ。通知を使用しない場合の初期化を行います。
     *
     * @param invocablePredicate 実行条件
     * @param commandBuilder コマンドを作成する関数
     */
    public ParticipantInvocationImpl(Optional<Predicate<Data>> invocablePredicate, Function<Data, CommandWithDestination> commandBuilder) {
        this(invocablePredicate, commandBuilder, false);
    }

    /**
     * コンストラクタ。
     *
     * @param invocablePredicate 実行条件
     * @param commandBuilder コマンドを作成する関数
     * @param notification 通知であるかどうか
     */
    public ParticipantInvocationImpl(Optional<Predicate<Data>> invocablePredicate, Function<Data, CommandWithDestination> commandBuilder, boolean notification) {
        super(invocablePredicate);
        this.commandBuilder = commandBuilder;
        this.notification = notification;
    }

    @Override
    public boolean isSuccessfulReply(Message message) {
        return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
    }

    @Override
    public CommandWithDestinationAndType makeCommandToSend(Data data) {
        return new CommandWithDestinationAndType(commandBuilder.apply(data), notification);
    }
}
