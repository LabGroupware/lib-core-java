package org.cresplanex.core.common.jdbc;

import org.apache.commons.lang3.StringUtils;

/**
 * データベーススキーマを管理するクラス。
 * <p>
 * デフォルトスキーマ、空スキーマの管理と、テーブルの完全修飾名を生成するメソッドを提供します。
 * </p>
 */
public class CoreSchema {

    /**
     * デフォルトのデータベーススキーマ名
     */
    public static final String DEFAULT_SCHEMA = "core";

    /**
     * スキーマが空であることを示す文字列
     */
    public static final String EMPTY_SCHEMA = "none";

    /**
     * 使用するデータベーススキーマ名
     */
    private final String coreDatabaseSchema;

    /**
     * {@link CoreSchema} のデフォルトコンストラクタ。 デフォルトスキーマ名を "core" として設定します。
     */
    public CoreSchema() {
        coreDatabaseSchema = DEFAULT_SCHEMA;
    }

    /**
     * 指定されたスキーマ名で {@link CoreSchema} オブジェクトを構築します。
     * <p>
     * スキーマ名が空の場合はデフォルトスキーマ名 "core" を使用します。
     * </p>
     *
     * @param coreDatabaseSchema 設定するスキーマ名
     */
    public CoreSchema(String coreDatabaseSchema) {
        this.coreDatabaseSchema = StringUtils.isEmpty(coreDatabaseSchema) ? DEFAULT_SCHEMA : coreDatabaseSchema;
    }

    /**
     * 現在のデータベーススキーマ名を取得します。
     *
     * @return データベーススキーマ名
     */
    public String getCoreDatabaseSchema() {
        return coreDatabaseSchema;
    }

    /**
     * スキーマが空であるかを判定します。
     *
     * @return スキーマが "none" であれば true
     */
    public boolean isEmpty() {
        return EMPTY_SCHEMA.equals(coreDatabaseSchema);
    }

    /**
     * デフォルトスキーマが設定されているかを判定します。
     *
     * @return スキーマが "core" であれば true
     */
    public boolean isDefault() {
        return DEFAULT_SCHEMA.equals(coreDatabaseSchema);
    }

    /**
     * テーブル名を完全修飾名にして返します。
     * <p>
     * スキーマが空でない場合、テーブル名の前にスキーマ名を付与します。
     * </p>
     *
     * @param table 修飾するテーブル名
     * @return 完全修飾名またはスキーマなしのテーブル名
     */
    public String qualifyTable(String table) {
        if (isEmpty()) {
            return table;
        }

        String schema = isDefault() ? DEFAULT_SCHEMA : coreDatabaseSchema;

        return String.format("%s.%s", schema, table);
    }
}
