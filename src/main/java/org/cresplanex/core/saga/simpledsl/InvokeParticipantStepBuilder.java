package org.cresplanex.core.saga.simpledsl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.cresplanex.core.commands.common.Command;
import org.cresplanex.core.commands.consumer.CommandWithDestination;
import org.cresplanex.core.saga.orchestration.SagaDefinition;

/**
 * 参加者ステップを構築するためのビルダークラスです。
 *
 * @param <Data> サガデータの型
 */
public class InvokeParticipantStepBuilder<Data> implements WithCompensationBuilder<Data> {

    private final SimpleSagaDefinitionBuilder<Data> parent;
    private Optional<ParticipantInvocation<Data>> action = Optional.empty();
    private Optional<ParticipantInvocation<Data>> compensation = Optional.empty();
    private final Map<String, SagaStep.HandlerAndClass<Data>> actionReplyHandlers = new HashMap<>();
    private final Map<String, SagaStep.HandlerAndClass<Data>> compensationReplyHandlers = new HashMap<>();

    /**
     * コンストラクタ。
     *
     * @param parent 親のSimpleSagaDefinitionBuilderインスタンス
     */
    public InvokeParticipantStepBuilder(SimpleSagaDefinitionBuilder<Data> parent) {
        this.parent = parent;
    }

    /**
     * アクションを指定します。
     *
     * @param participantInvocationPredicate 実行条件のためのオプションの述語
     * @param action アクションを実行する関数
     * @return このビルダー
     */
    InvokeParticipantStepBuilder<Data> withAction(Optional<Predicate<Data>> participantInvocationPredicate, Function<Data, CommandWithDestination> action) {
        this.action = Optional.of(new ParticipantInvocationImpl<>(participantInvocationPredicate, action));
        return this;
    }

    /**
     * 通知アクションを指定します。
     *
     * @param participantInvocationPredicate 実行条件のためのオプションの述語
     * @param notificationAction 通知アクションを実行する関数
     * @return このビルダー
     */
    public InvokeParticipantStepBuilder<Data> withNotificationAction(Optional<Predicate<Data>> participantInvocationPredicate, Function<Data, CommandWithDestination> notificationAction) {
        this.action = Optional.of(new ParticipantInvocationImpl<>(participantInvocationPredicate, notificationAction, true));
        return this;
    }

    /**
     * コマンドエンドポイントを指定してアクションを構築します。
     *
     * @param participantInvocationPredicate 実行条件のためのオプションの述語
     * @param commandEndpoint コマンドエンドポイント
     * @param commandProvider コマンドを提供する関数
     * @param <C> コマンドの型
     * @return このビルダー
     */
    <C extends Command> InvokeParticipantStepBuilder<Data> withAction(Optional<Predicate<Data>> participantInvocationPredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        this.action = Optional.of(new ParticipantEndpointInvocationImpl<>(participantInvocationPredicate, commandEndpoint, commandProvider));
        return this;
    }

    @Override
    public InvokeParticipantStepBuilder<Data> withCompensation(Function<Data, CommandWithDestination> compensation) {
        this.compensation = Optional.of(new ParticipantInvocationImpl<>(Optional.empty(), compensation));
        return this;
    }

    /**
     * 通知補償を指定します。
     *
     * @param compensation 補償アクションを実行する関数
     * @return このビルダー
     */
    public InvokeParticipantStepBuilder<Data> withCompensationNotification(Function<Data, CommandWithDestination> compensation) {
        this.compensation = Optional.of(new ParticipantInvocationImpl<>(Optional.empty(), compensation, true));
        return this;
    }

    /**
     * 条件付きの通知補償を指定します。
     *
     * @param compensationPredicate 補償アクションの実行条件
     * @param compensation 補償アクションを実行する関数
     * @return このビルダー
     */
    public InvokeParticipantStepBuilder<Data> withCompensationNotification(Predicate<Data> compensationPredicate, Function<Data, CommandWithDestination> compensation) {
        this.compensation = Optional.of(new ParticipantInvocationImpl<>(Optional.of(compensationPredicate), compensation, true));
        return this;
    }

    @Override
    public InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, Function<Data, CommandWithDestination> compensation) {
        this.compensation = Optional.of(new ParticipantInvocationImpl<>(Optional.of(compensationPredicate), compensation));
        return this;
    }

    @Override
    public <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        this.compensation = Optional.of(new ParticipantEndpointInvocationImpl<>(Optional.empty(), commandEndpoint, commandProvider));
        return this;
    }

    @Override
    public <C extends Command> InvokeParticipantStepBuilder<Data> withCompensation(Predicate<Data> compensationPredicate, CommandEndpoint<C> commandEndpoint, Function<Data, C> commandProvider) {
        this.compensation = Optional.of(new ParticipantEndpointInvocationImpl<>(Optional.of(compensationPredicate), commandEndpoint, commandProvider));
        return this;
    }

    /**
     * 返信ハンドラを追加します。
     *
     * @param replyClass 返信のクラス
     * @param replyType 返信のタイプ
     * @param replyHandler 返信を処理するハンドラ
     * @param <T> 返信の型
     * @return このビルダー
     */
    public <T> InvokeParticipantStepBuilder<Data> onReply(Class<T> replyClass, String replyType, BiConsumer<Data, T> replyHandler) {
        if (compensation.isPresent()) {
            BiConsumer<Data, Object> handler = (data, rawReply) -> replyHandler.accept(data, replyClass.cast(rawReply));
            compensationReplyHandlers.put(replyType, new SagaStep.HandlerAndClass<>(handler, replyClass));
        } else {
            BiConsumer<Data, Object> handler = (data, rawReply) -> replyHandler.accept(data, replyClass.cast(rawReply));
            actionReplyHandlers.put(replyType, new SagaStep.HandlerAndClass<>(handler, replyClass));
        }
        return this;
    }

    /**
     * 次のステップのビルダーを取得します。
     *
     * @return 新しいStepBuilderインスタンス
     */
    public StepBuilder<Data> step() {
        addStep();
        return new StepBuilder<>(parent);
    }

    /**
     * サガ定義を構築します。
     *
     * @return 構築されたSagaDefinition
     */
    public SagaDefinition<Data> build() {
        // TODO see comment in local step
        addStep();
        return parent.build();
    }

    /**
     * ステップを追加します。
     */
    private void addStep() {
        parent.addStep(new ParticipantInvocationStep<>(action, compensation, actionReplyHandlers, compensationReplyHandlers));
    }

}
