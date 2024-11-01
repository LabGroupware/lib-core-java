package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandWithDestination;

/**
 * サガのステップを定義するためのビルダークラスです。
 *
 * @param <Data> サガで使用されるデータの型
 */
public class StepBuilder<Data> implements WithCompensationBuilder<Data> {

    private final SimpleSagaDefinitionBuilder<Data> parent;

    /**
     * コンストラクタ
     *
     * @param builder SimpleSagaDefinitionBuilderのインスタンス
     */
    public StepBuilder(SimpleSagaDefinitionBuilder<Data> builder) {
        this.parent = builder;
    }

    /**
     * ローカルでのアクションを実行するステップを追加します。
     *
     * @param localFunction 実行するローカル関数
     * @return LocalStepBuilderのインスタンス
     */
    public LocalStepBuilder<Data> invokeLocal(Consumer<Data> localFunction) {
        return new LocalStepBuilder<>(parent, localFunction);
    }

    /**
     * パーティシパント（参加者）にアクションを実行させるステップを追加します。
     *
     * @param action 実行するコマンド
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public InvokeParticipantStepBuilder<Data> invokeParticipant(Function<Data, CommandWithDestination> action) {
        return new InvokeParticipantStepBuilder<>(parent).withAction(Optional.empty(), action);
    }

    /**
     * 条件に応じてパーティシパントにアクションを実行させるステップを追加します。
     *
     * @param participantInvocationPredicate 条件を判定する述語
     * @param action 実行するコマンド
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public InvokeParticipantStepBuilder<Data> invokeParticipant(Predicate<Data> participantInvocationPredicate, Function<Data, CommandWithDestination> action) {
        return new InvokeParticipantStepBuilder<>(parent).withAction(Optional.of(participantInvocationPredicate), action);
    }

    /**
     * 特定のエンドポイントに対してパーティシパントにアクションを実行させるステップを追加します。
     *
     * @param commandEndpoint コマンドエンドポイント
     * @param commandProvider コマンドを生成する関数
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public <C extends Command> InvokeParticipantStepBuilder<Data> invokeParticipant(CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        return new InvokeParticipantStepBuilder<>(parent).withAction(Optional.empty(), commandEndpoint, commandProvider);
    }

    /**
     * 条件に応じて、特定のエンドポイントに対してパーティシパントにアクションを実行させるステップを追加します。
     *
     * @param participantInvocationPredicate 条件を判定する述語
     * @param commandEndpoint コマンドエンドポイント
     * @param commandProvider コマンドを生成する関数
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public <C extends Command> InvokeParticipantStepBuilder<Data> invokeParticipant(Predicate<Data> participantInvocationPredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        return new InvokeParticipantStepBuilder<>(parent).withAction(Optional.of(participantInvocationPredicate), commandEndpoint, commandProvider);
    }

    /**
     * パーティシパントに対して補償（コンペンセーション）を実行するステップを追加します。
     */
    @Override
    public InvokeParticipantStepBuilder<Data> withCompensation(Function<Data, CommandWithDestination> compensation) {
        return new InvokeParticipantStepBuilder<>(parent).withCompensation(compensation);
    }

    /**
     * 条件に応じて、パーティシパントに対して補償（コンペンセーション）を実行するステップを追加します。
     */
    @Override
    public InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, Function<Data, CommandWithDestination> compensation) {
        return new InvokeParticipantStepBuilder<>(parent).withCompensation(compensation);
    }

    /**
     * 特定のエンドポイントに対してパーティシパントに対して補償（コンペンセーション）を実行するステップを追加します。
     */
    @Override
    public <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        return new InvokeParticipantStepBuilder<>(parent).withCompensation(commandEndpoint, commandProvider);
    }

    /**
     * 条件に応じて、特定のエンドポイントに対してパーティシパントに対して補償（コンペンセーション）を実行するステップを追加します。
     */
    @Override
    public <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        return new InvokeParticipantStepBuilder<>(parent).withCompensation(compensationPredicate, commandEndpoint, commandProvider);
    }

    /**
     * パーティシパントに通知を送信するステップを追加します。
     *
     * @param notificationAction 実行する通知アクション
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public InvokeParticipantStepBuilder<Data> notifyParticipant(Function<Data, CommandWithDestination> notificationAction) {
        return new InvokeParticipantStepBuilder<>(parent).withNotificationAction(Optional.empty(), notificationAction);
    }

    /**
     * 条件に応じて、パーティシパントに通知を送信するステップを追加します。
     *
     * @param participantInvocationPredicate 条件を判定する述語
     * @param notificationAction 実行する通知アクション
     * @return InvokeParticipantStepBuilderのインスタンス
     */
    public InvokeParticipantStepBuilder<Data> notifyParticipant(Predicate<Data> participantInvocationPredicate, Function<Data, CommandWithDestination> notificationAction) {
        return new InvokeParticipantStepBuilder<>(parent).withNotificationAction(Optional.of(participantInvocationPredicate), notificationAction);
    }
}
