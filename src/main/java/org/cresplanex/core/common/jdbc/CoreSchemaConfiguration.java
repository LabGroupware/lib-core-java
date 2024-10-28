package org.cresplanex.core.common.jdbc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * データベーススキーマの設定を行うクラス。
 * <p>
 * アプリケーション設定からスキーマ名を読み込み、{@link CoreSchema} のインスタンスを生成します。
 * </p>
 */
@Configuration
public class CoreSchemaConfiguration {

    /**
     * CoreSchemaのBeanを作成します。
     * <p>
     * プロパティ `core.database.schema` からデータベーススキーマ名を取得し、スキーマ名を設定した
     * {@link CoreSchema} オブジェクトを返します。 スキーマ名が設定されていない場合、デフォルトスキーマが使用されます。
     * </p>
     *
     * @param coreDatabaseSchema データベーススキーマ名（アプリケーション設定から取得）
     * @return CoreSchemaのインスタンス
     */
    @Bean
    public CoreSchema coreSchema(@Value("${core.database.schema:#{null}}") String coreDatabaseSchema) {
        return new CoreSchema(coreDatabaseSchema);
    }
}
