package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.SagaDefinition;

import java.util.LinkedList;
import java.util.List;

/**
 * シンプルなサガの定義を構築するビルダークラスです。
 *
 * @param <Data> サガで処理されるデータの型
 */
public class SimpleSagaDefinitionBuilder<Data> {

    private final List<SagaStep<Data>> sagaSteps = new LinkedList<>();

    /**
     * サガステップを追加します。
     *
     * @param sagaStep 追加するサガステップ
     */
    public void addStep(SagaStep<Data> sagaStep) {
        sagaSteps.add(sagaStep);
    }

    /**
     * サガ定義をビルドします。
     *
     * @return サガ定義
     */
    public SagaDefinition<Data> build() {
        return new SimpleSagaDefinition<>(sagaSteps);
    }
}
