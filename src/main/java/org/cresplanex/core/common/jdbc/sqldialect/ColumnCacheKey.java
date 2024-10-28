package org.cresplanex.core.common.jdbc.sqldialect;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * カラムキャッシュキーを表すクラス。
 * <p>
 * スキーマ名、テーブル名、およびカラム名を組み合わせて、キャッシュキーとして使用します。
 * カラムのキャッシュされたデータを取得する際に一意に識別できるように設計されています。
 * </p>
 */
public class ColumnCacheKey {

    /**
     * スキーマ名
     */
    private String schema;

    /**
     * テーブル名
     */
    private String table;

    /**
     * カラム名
     */
    private String column;

    /**
     * 指定されたスキーマ、テーブル、カラム名でキャッシュキーを初期化します。
     *
     * @param schema スキーマ名
     * @param table テーブル名
     * @param column カラム名
     */
    public ColumnCacheKey(String schema, String table, String column) {
        this.schema = schema;
        this.table = table;
        this.column = column;
    }

    /**
     * スキーマ名を取得します。
     *
     * @return スキーマ名
     */
    public String getSchema() {
        return schema;
    }

    /**
     * スキーマ名を設定します。
     *
     * @param schema 新しいスキーマ名
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * テーブル名を取得します。
     *
     * @return テーブル名
     */
    public String getTable() {
        return table;
    }

    /**
     * テーブル名を設定します。
     *
     * @param table 新しいテーブル名
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * カラム名を取得します。
     *
     * @return カラム名
     */
    public String getColumn() {
        return column;
    }

    /**
     * カラム名を設定します。
     *
     * @param column 新しいカラム名
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * 指定されたオブジェクトとこのキャッシュキーが等しいかを判定します。
     *
     * @param o 比較対象のオブジェクト
     * @return オブジェクトが等しい場合はtrue、そうでない場合はfalse
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * このキャッシュキーのハッシュコードを返します。
     *
     * @return ハッシュコード
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
