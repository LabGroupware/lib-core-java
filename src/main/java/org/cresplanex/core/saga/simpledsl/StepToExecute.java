package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaActions;

/**
 * 特定のサガステップを実行するためのクラス。
 *
 * @param <Data> サガで使用されるデータの型
 */
public class StepToExecute<Data> extends AbstractStepToExecute<Data, SagaStep<Data>> {

    public StepToExecute(SagaStep<Data> step, int skipped, boolean compensating) {
        super(step, skipped, compensating);
    }

    /**
     * ステップを実行し、新しいサガアクションを生成します。
     *
     * @param data 実行に使用するデータ
     * @param currentState 現在のサガの実行状態
     * @return 新しいサガアクション
     */
    public SagaActions<Data> executeStep(Data data, SagaExecutionState currentState) {
        SagaExecutionState newState = currentState.nextState(size());
        SagaActions.Builder<Data> builder = SagaActions.builder();

        // Outcomeがlocalである場合,
        // localExceptionの登録(nullの場合もある), local=trueにする
        // remoteである場合,
        // actionのcommandsにcommandリストを登録.
        step.makeStepOutcome(data, this.compensating).visit(builder::withIsLocal, builder::withCommands);

        return makeSagaActions(builder, data, newState, currentState.isCompensating());
    }
}
