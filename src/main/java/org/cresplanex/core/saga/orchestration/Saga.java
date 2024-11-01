package org.cresplanex.core.saga.orchestration;

/**
 * サガの操作を定義するインターフェース。サガが処理するデータ型を定義し、サガの開始や終了、失敗時の処理を指定します。
 *
 * @param <Data> サガが処理するデータの型
 */
public interface Saga<Data> {

    /**
     * サガの処理フローを定義する {@link SagaDefinition} を返します。
     *
     * @return サガの処理フロー定義
     */
    SagaDefinition<Data> getSagaDefinition();

    /**
     * サガのタイプを取得します。デフォルトでクラス名を基にタイプを生成します。
     *
     * @return サガのタイプ
     */
    default String getSagaType() {
        return getClass().getName().replace("$", "_DLR_");
    }

    /**
     * サガ開始時に実行される処理。サガIDとデータを受け取ります。
     *
     * @param sagaId サガID
     * @param data サガのデータ
     */
    default void onStarting(String sagaId, Data data) {
    }

    /**
     * サガが正常に完了した際に実行される処理。
     *
     * @param sagaId サガID
     * @param data サガのデータ
     */
    default void onSagaCompletedSuccessfully(String sagaId, Data data) {
    }

    /**
     * サガがロールバックされた際に実行される処理。
     *
     * @param sagaId サガID
     * @param data サガのデータ
     */
    default void onSagaRolledBack(String sagaId, Data data) {
    }

    /**
     * サガが失敗した際に実行される処理。
     *
     * @param sagaId サガID
     * @param data サガのデータ
     */
    default void onSagaFailed(String sagaId, Data data) {
    }
;
}
