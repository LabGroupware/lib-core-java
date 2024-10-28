package org.cresplanex.core.common.jdbc;

/**
 * SQL列をJSON形式に変換するためのインターフェース。
 * <p>
 * スキーマとカラム名を基に、指定された列をJSON文字列として取得するためのメソッドを提供します。
 * </p>
 */
public interface SqlJsonConverter {

    /**
     * 指定されたスキーマとカラム名からJSON形式に変換します。
     *
     * @param schema 変換対象のスキーマ
     * @param column 変換対象のカラム名
     * @return JSON形式の文字列
     */
    String convert(CoreSchema schema, String column);
}
