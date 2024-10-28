package org.cresplanex.core.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBCクエリ結果の行をオブジェクトにマッピングするためのインターフェース。
 * <p>
 * このインターフェースを実装することで、SQLクエリの結果で取得した {@link ResultSet} の各行を 特定の型のオブジェクトに変換できます。
 * </p>
 *
 * @param <T> マッピング対象のオブジェクトの型
 */
public interface CoreRowMapper<T> {

    /**
     * {@link ResultSet} の1行を指定された型のオブジェクトにマッピングします。
     *
     * @param rs 結果セット（SQLクエリの実行結果の行）, 各カラムの取得が可能
     * @param rowNum 行番号（0から始まるインデックス）
     * @return マッピングされたオブジェクト
     * @throws SQLException データベースアクセスエラーが発生した場合
     */
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
