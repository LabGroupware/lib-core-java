package org.cresplanex.core.saga.orchestration;

import java.util.function.Function;

/**
 * 開始ハンドラインターフェース。 Sagaの開始時に適用される処理を定義します。
 *
 * @param <Data> Sagaデータの型
 */
@SuppressWarnings("rawtypes")
public interface StartingHandler<Data> extends Function<Data, SagaActions> {
}
