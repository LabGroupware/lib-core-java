package org.cresplanex.core.common.id;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * ID生成器の設定クラス。
 * <p>
 * このクラスでは、条件に応じたID生成器のインスタンスを提供します。
 * 例えば、アプリケーション固有のID生成器やデータベースID生成器を構成することができます。
 * </p>
 */
@Configuration
public class CoreIdGeneratorConfiguration {

    /**
     * アプリケーションID生成器のBeanを作成します。
     * <p>
     * {@link ApplicationIdGeneratorCondition}が適用されている場合にのみ
     * {@link ApplicationIdGenerator}のインスタンスを提供します。
     * core.outbox.id プロパティが設定されていない場合にアプリケーションで自動生成するIDGeneratorを提供します。
     * </p>
     *
     * @return アプリケーションID生成器のインスタンス
     */
    @Bean("org.cresplanex.core.common.id.IdGenerator")
    @Conditional(ApplicationIdGeneratorCondition.class)
    public IdGenerator applicationIdGenerator() {
        return new ApplicationIdGenerator();
    }

    /**
     * データベースID生成器のBeanを作成します。
     * <p>
     * プロパティ "core.outbox.id" が設定されている場合に
     * {@link DatabaseIdGenerator}のインスタンスを提供します。
     * プロパティが設定されていない場合、この生成器はインスタンス化されません。
     * </p>
     *
     * @param id データベース用のユニークID（"core.outbox.id" プロパティから取得）
     * @return データベースID生成器のインスタンス
     */
    @Bean("org.cresplanex.core.common.id.IdGenerator")
    @ConditionalOnProperty(name = "core.outbox.id")
    public IdGenerator databaseIdGenerator(@Value("${core.outbox.id:#{null}}") long id) {
        return new DatabaseIdGenerator(id);
    }
}
