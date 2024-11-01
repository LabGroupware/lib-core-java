package org.cresplanex.core.saga.simpledsl;

import java.util.function.BiConsumer;

/**
 * ローカル例外を保存するクラス。 指定された例外タイプに一致する場合、データと例外を使用して処理を実行します。
 *
 * @param <Data> サガのデータの型
 */
public class LocalExceptionSaver<Data> {

    private final Class<?> exceptionType;
    private final BiConsumer<Data, RuntimeException> exceptionConsumer;

    /**
     * 指定された例外タイプと例外処理を使用してインスタンスを初期化します。
     *
     * @param exceptionType 保存対象の例外のクラス
     * @param exceptionConsumer 例外を処理するためのコンシューマー
     */
    public LocalExceptionSaver(Class<?> exceptionType, BiConsumer<Data, RuntimeException> exceptionConsumer) {
        this.exceptionType = exceptionType;
        this.exceptionConsumer = exceptionConsumer;
    }

    /**
     * 指定された例外が保存対象であるかどうかを判定します。
     *
     * @param e チェックする例外
     * @return 例外が保存対象のタイプである場合は true
     */
    public boolean shouldSave(Exception e) {
        return exceptionType.isInstance(e);
    }

    /**
     * 指定されたデータと例外を使用して保存処理を実行します。
     *
     * @param data サガデータ
     * @param e 保存する例外
     */
    public void save(Data data, RuntimeException e) {
        exceptionConsumer.accept(data, e);
    }
}
