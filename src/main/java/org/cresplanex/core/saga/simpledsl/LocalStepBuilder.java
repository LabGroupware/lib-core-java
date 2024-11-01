package org.cresplanex.core.saga.simpledsl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.cresplanex.core.saga.orchestration.SagaDefinition;

/**
 * ローカルステップのビルダー。 サガ内でローカルに実行する処理や、補償処理、例外ハンドリングを設定します。
 *
 * @param <Data> サガのデータ型
 */
public class LocalStepBuilder<Data> {

    private final SimpleSagaDefinitionBuilder<Data> parent;
    private final Consumer<Data> localFunction;
    private Optional<Consumer<Data>> compensation = Optional.empty();

    private final List<LocalExceptionSaver<Data>> localExceptionSavers = new LinkedList<>();
    private final List<Class<RuntimeException>> rollbackExceptions = new LinkedList<>();

    /**
     * コンストラクタ
     *
     * @param parent 親のSagaDefinitionBuilder
     * @param localFunction ローカルステップで実行する関数
     */
    public LocalStepBuilder(SimpleSagaDefinitionBuilder<Data> parent, Consumer<Data> localFunction) {
        this.parent = parent;
        this.localFunction = localFunction;
    }

    /**
     * 補償処理を設定します。
     *
     * @param localCompensation 補償処理のコンシューマ
     * @return LocalStepBuilder インスタンス
     */
    public LocalStepBuilder<Data> withCompensation(Consumer<Data> localCompensation) {
        this.compensation = Optional.of(localCompensation);
        return this;
    }

    /**
     * 新しいステップビルダーを作成して返します。
     *
     * @return 新しいStepBuilder
     */
    public StepBuilder<Data> step() {
        parent.addStep(makeLocalStep());
        return new StepBuilder<>(parent);
    }

    /**
     * 現在の設定を基にローカルステップを作成します。
     *
     * @return 作成したLocalStep
     */
    private LocalStep<Data> makeLocalStep() {
        return new LocalStep<>(localFunction, compensation, localExceptionSavers, rollbackExceptions);
    }

    /**
     * サガ定義をビルドします。
     *
     * @return 完成したSagaDefinition
     */
    public SagaDefinition<Data> build() {
        parent.addStep(makeLocalStep());
        return parent.build();
    }

    /**
     * 指定した例外タイプに対する例外ハンドラーを追加します。
     *
     * @param exceptionType 例外クラスの型
     * @param exceptionConsumer 例外発生時に実行する処理
     * @param <E> 例外クラスの型
     * @return LocalStepBuilder インスタンス
     */
    @SuppressWarnings("unchecked")
    public <E extends RuntimeException> LocalStepBuilder<Data> onException(Class<E> exceptionType, BiConsumer<Data, E> exceptionConsumer) {
        rollbackExceptions.add((Class<RuntimeException>) exceptionType);
        localExceptionSavers.add(new LocalExceptionSaver<>(exceptionType, (BiConsumer<Data, RuntimeException>) exceptionConsumer));
        return this;
    }

    /**
     * 指定した例外タイプが発生した際にロールバックを行う設定を追加します。
     *
     * @param exceptionType 例外クラスの型
     * @param <E> 例外クラスの型
     * @return LocalStepBuilder インスタンス
     */
    @SuppressWarnings("unchecked")
    public <E extends RuntimeException> LocalStepBuilder<Data> onExceptionRollback(Class<E> exceptionType) {
        rollbackExceptions.add((Class<RuntimeException>) exceptionType);
        return this;
    }
}
