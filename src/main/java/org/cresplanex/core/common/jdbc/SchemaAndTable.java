package org.cresplanex.core.common.jdbc;

import java.util.Objects;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * データベースのスキーマとテーブル名を保持するクラス。
 * <p>
 * スキーマ名とテーブル名を管理し、テーブル名は小文字で統一されます。
 * </p>
 */
public class SchemaAndTable {

    /**
     * スキーマ名
     */
    private final String schema;

    /**
     * テーブル名（小文字）
     */
    private final String tableName;

    /**
     * 指定されたスキーマ名とテーブル名で {@link SchemaAndTable} オブジェクトを構築します。
     * テーブル名は小文字に変換されて保持されます。
     *
     * @param schema スキーマ名
     * @param tableName テーブル名
     */
    public SchemaAndTable(String schema, String tableName) {
        this.schema = schema;
        this.tableName = tableName.toLowerCase();
    }

    /**
     * このオブジェクトの文字列表現を返します。
     *
     * @return このオブジェクトの文字列形式
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
     * テーブル名を取得します。
     *
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * このオブジェクトのハッシュコードを返します。
     *
     * @return `schema` と `tableName` に基づくハッシュコード
     */
    @Override
    public int hashCode() {
        return Objects.hash(schema, tableName);
    }

    /**
     * このオブジェクトと指定されたオブジェクトが等しいかを判定します。
     * <p>
     * 同じクラスで、かつ `schema` と `tableName` が等しい場合に true を返します。
     * </p>
     *
     * @param obj 比較対象のオブジェクト
     * @return オブジェクトが等しい場合は true、そうでない場合は false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SchemaAndTable that = (SchemaAndTable) obj;
        return EqualsBuilder.reflectionEquals(this, that);
    }
}
