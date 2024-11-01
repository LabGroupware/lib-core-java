package org.cresplanex.core.saga.orchestration.repository;

/**
 * SQLクエリの行データを取得するインターフェース。 SQLクエリの結果を文字列やブール値として取得します。
 */
public interface SqlQueryRow {

    /**
     * 指定された名前の文字列データを取得します。
     *
     * @param name データの名前
     * @return 指定された名前に対応する文字列データ
     */
    String getString(String name);

    /**
     * 指定された名前のブール値データを取得します。
     *
     * @param name データの名前
     * @return 指定された名前に対応するブール値データ
     */
    boolean getBoolean(String name);
}
