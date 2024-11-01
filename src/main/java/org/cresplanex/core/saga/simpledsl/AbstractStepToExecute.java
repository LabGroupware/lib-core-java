package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaActions;
import static org.cresplanex.core.saga.simpledsl.SagaExecutionStateJsonSerde.encodeState;

/**
 * サガのステップを実行するための抽象クラスです。
 *
 * @param <Data> サガデータの型
 * @param <Step> サガのステップの型
 */
public class AbstractStepToExecute<Data, Step extends ISagaStep<Data>> {

    protected final SagaStep<Data> step;
    protected final int skipped;
    protected final boolean compensating;

    /**
     * コンストラクタ
     *
     * @param step 実行するステップ
     * @param skipped スキップしたステップの数
     * @param compensating 補償処理中かどうか
     */
    public AbstractStepToExecute(SagaStep<Data> step, int skipped, boolean compensating) {
        this.step = step;
        this.skipped = skipped;
        this.compensating = compensating;
    }

    /**
     * ステップのサイズを取得します。
     *
     * @return スキップしたステップを含むサイズ
     */
    protected int size() {
        return 1 + skipped;
    }

    /**
     * サガアクションを生成します。
     *
     * @param builder サガアクションビルダー
     * @param data サガデータ
     * @param newState 新しいサガの実行状態
     * @param compensating 補償処理中かどうか
     * @return 生成されたサガアクション
     */
    protected SagaActions<Data> makeSagaActions(SagaActions.Builder<Data> builder, Data data, SagaExecutionState newState, boolean compensating) {
        String state = encodeState(newState);
        return builder.buildActions(data, compensating, state, newState.isEndState());
    }
}
