package org.cresplanex.core.saga.simpledsl;

import java.util.List;
import java.util.function.BiFunction;

import org.cresplanex.core.commands.common.ReplyMessageHeaders;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.SagaActions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * サガの実行を簡略化するための抽象クラスです。 このクラスは、サガのステップを順次実行し、成功または失敗に応じた アクションを提供します。
 *
 * @param <Data> サガデータのタイプ
 * @param <Step> サガの各ステップのタイプ
 * @param <ToExecute> 実行すべきステップの詳細を保持する型
 * @param <Provider> サガアクションプロバイダの型
 */
public abstract class AbstractSimpleSagaDefinition<Data, Step extends ISagaStep<Data>, ToExecute extends AbstractStepToExecute<Data, Step>, Provider extends AbstractSagaActionsProvider<Data, ?>> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected List<Step> steps;

    /**
     * コンストラクタ。
     *
     * @param steps サガの各ステップのリスト
     */
    public AbstractSimpleSagaDefinition(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * 初めて実行するステップを取得します。
     *
     * @param data サガデータ
     * @return 実行する最初のステップのプロバイダ
     */
    protected Provider firstStepToExecute(Data data) {
        return nextStepToExecute(SagaExecutionState.startingState(), data);
    }

    /**
     * 失敗した補償トランザクションを処理します。
     *
     * @param sagaType サガの種類
     * @param sagaId サガのID
     * @param state サガの実行状態
     * @param message メッセージ
     * @return 失敗処理後のサガアクションプロバイダ
     */
    protected Provider handleFailedCompensatingTransaction(String sagaType, String sagaId, SagaExecutionState state, Message message) {
        logger.error("Saga {} {} failed due to failed compensating transaction {}", sagaType, sagaId, message);
        return makeSagaActionsProvider(SagaActions.<Data>builder()
                .withUpdatedState(SagaExecutionStateJsonSerde.encodeState(SagaExecutionState.makeFailedEndState()))
                .withIsEndState(true)
                .withIsCompensating(state.isCompensating())
                .withIsFailed(true)
                .build());
    }

    /**
     * 次に実行するサガアクションを取得します。
     *
     * @param sagaType サガの種類
     * @param sagaId サガのID
     * @param sagaData サガデータ
     * @param message メッセージ
     * @param state サガの実行状態
     * @param currentStep 現在のステップ
     * @param compensating 補償処理かどうか
     * @return 次に実行するサガアクションプロバイダ
     */
    protected Provider sagaActionsForNextStep(String sagaType, String sagaId, Data sagaData, Message message,
            SagaExecutionState state, Step currentStep, boolean compensating) {
        if (currentStep.isSuccessfulReply(compensating, message)) {
            return nextStepToExecute(state, sagaData);
        } else if (compensating) {
            return handleFailedCompensatingTransaction(sagaType, sagaId, state, message);
        } else {
            return nextStepToExecute(state.startCompensating(), sagaData);
        }
    }

    /**
     * サガの次のステップを取得します。
     *
     * @param state サガの実行状態
     * @param data サガデータ
     * @return 実行する次のステップのプロバイダ
     */
    protected Provider nextStepToExecute(SagaExecutionState state, Data data) {
        int skipped = 0;
        boolean compensating = state.isCompensating();
        int direction = compensating ? -1 : +1;
        for (int i = state.getCurrentlyExecuting() + direction; i >= 0 && i < steps.size(); i = i + direction) {
            Step step = steps.get(i);
            if ((compensating ? step.hasCompensation(data) : step.hasAction(data))) {
                ToExecute stepToExecute = makeStepToExecute(skipped, compensating, step);
                return makeSagaActionsProvider(stepToExecute, data, state);
            } else {
                skipped++;
            }
        }
        // サガの終了状態を作成
        return makeSagaActionsProvider(makeEndStateSagaActions(state));
    }

    /**
     * メッセージに応じてリプライハンドラを呼び出します。
     *
     * @param message メッセージ
     * @param data サガデータ
     * @param handler リプライハンドラ
     * @param <T> ハンドラの戻り値の型
     * @return ハンドラの実行結果
     */
    protected <T> T invokeReplyHandler(Message message, Data data, BiFunction<Data, Object, T> handler, Class<?> replyClass) {
//        Class<?> m;
//        try {
//            // リプライタイプよりクラスを取得
//            String className = message.getRequiredHeader(ReplyMessageHeaders.REPLY_TYPE);
//            // クラスをロード
//            // TODO: 異なる言語やパッケージでのエラーを防ぐための処置が必要
//            m = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
//        } catch (ClassNotFoundException e) {
//            logger.error("Class not found", e);
//            throw new RuntimeException("Class not found", e);
//        }
        // リプライをデシリアライズ
        Object reply = JSonMapper.fromJson(message.getPayload(), replyClass);
        // ハンドラを実行
        return handler.apply(data, reply);
    }

    /**
     * サガの終了状態を作成します。
     *
     * @param state サガの実行状態
     * @return サガの終了状態アクション
     */
    protected SagaActions<Data> makeEndStateSagaActions(SagaExecutionState state) {
        return SagaActions.<Data>builder()
                .withUpdatedState(SagaExecutionStateJsonSerde.encodeState(SagaExecutionState.makeEndState()))
                .withIsEndState(true)
                .withIsCompensating(state.isCompensating())
                .build();
    }

    protected abstract ToExecute makeStepToExecute(int skipped, boolean compensating, Step step);

    protected abstract Provider makeSagaActionsProvider(SagaActions<Data> sagaActions);

    protected abstract Provider makeSagaActionsProvider(ToExecute stepToExecute, Data data, SagaExecutionState state);

}
