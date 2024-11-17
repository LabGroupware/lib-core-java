package org.cresplanex.core.saga.simpledsl;

import java.util.List;
import static java.util.function.Function.identity;

import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.saga.orchestration.SagaActions;
import org.cresplanex.core.saga.orchestration.SagaDefinition;

/**
 * シンプルなサガの定義を提供するクラスです。
 *
 * @param <Data> サガで処理されるデータの型
 */
public class SimpleSagaDefinition<Data>
        extends AbstractSimpleSagaDefinition<Data, SagaStep<Data>, StepToExecute<Data>, SagaActionsProvider<Data>>
        implements SagaDefinition<Data> {

    /**
     * コンストラクタ。
     *
     * @param steps サガの各ステップのリスト
     */
    public SimpleSagaDefinition(List<SagaStep<Data>> steps) {
        super(steps);
    }

    @Override
    public SagaActions<Data> start(Data sagaData) {
        return toSagaActions(firstStepToExecute(sagaData));
    }

    @Override
    public SagaActions<Data> handleReply(String sagaType, String sagaId, String currentState, Data sagaData, Message message) {

        SagaExecutionState state = SagaExecutionStateJsonSerde.decodeState(currentState);
        SagaStep<Data> currentStep = steps.get(state.getCurrentlyExecuting());
        boolean compensating = state.isCompensating();

        // 以前のステップのリプライハンドラを呼び出す
        // Local Stepでは, ハンドラを登録不可
        currentStep.getReplyHandler(message, compensating).ifPresent(handlerAndClass -> invokeReplyHandler(message, sagaData, (d, m) -> {
            handlerAndClass.getHandler().accept(d, m);
            return null;
        }, handlerAndClass.getClazz()));

        SagaActionsProvider<Data> sap = sagaActionsForNextStep(sagaType, sagaId, sagaData, message, state, currentStep, compensating);
        return toSagaActions(sap);
    }

    private SagaActions<Data> toSagaActions(SagaActionsProvider<Data> sap) {
        // identityメソッドでは, 引数をそのまま返す高等関数を返す
        // これによるapplyとなるため, 継承元のAbstractSimpleSagaDefinitionのsagaActionsForNextStep内で生成される
        //  Providerに依存することに成るが, これは{@link sagaActionsForNextStep}に返される時点で, makeSagaActions(SagaActions)として
        // 以下の{@link makeSagaActionsProvider}が呼ばれる
        return sap.toSagaActions(identity(), identity());
    }

    @Override
    protected SagaActionsProvider<Data> makeSagaActionsProvider(SagaActions<Data> sagaActions) {
        return new SagaActionsProvider<>(sagaActions);
    }

    @Override
    protected SagaActionsProvider<Data> makeSagaActionsProvider(StepToExecute<Data> stepToExecute, Data data, SagaExecutionState state) {
        return new SagaActionsProvider<>(() -> stepToExecute.executeStep(data, state));
    }

    @Override
    protected StepToExecute<Data> makeStepToExecute(int skipped, boolean compensating, SagaStep<Data> step) {
        return new StepToExecute<>(step, skipped, compensating);
    }
}
