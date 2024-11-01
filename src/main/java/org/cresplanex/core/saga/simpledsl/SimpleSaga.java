package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.Saga;

/**
 * シンプルなDSLスタイルで記述されたサガのインターフェースです。
 *
 * @param <Data> サガで処理されるデータの型
 */
public interface SimpleSaga<Data> extends Saga<Data>, SimpleSagaDsl<Data> {
}
