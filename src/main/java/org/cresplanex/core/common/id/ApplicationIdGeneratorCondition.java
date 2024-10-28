package org.cresplanex.core.common.id;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * アプリケーションID生成条件クラス。
 * <p>
 * "core.outbox.id" プロパティが設定されていない場合に、条件が一致し、アプリケーションID生成器が有効になります。
 * </p>
 */
public class ApplicationIdGeneratorCondition extends SpringBootCondition {

    /**
     * 条件の一致結果を取得します。
     * <p>
     * プロパティ "core.outbox.id" がnullの場合に一致し、アプリケーションID生成器の条件が成立します。
     * </p>
     *
     * @param context   条件が評価されるコンテキスト情報
     * @param metadata  注釈付きメタデータ
     * @return 条件の結果を表す {@link ConditionOutcome} オブジェクト
     */
    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

        boolean match = context.getEnvironment().getProperty("core.outbox.id") == null;

        return new ConditionOutcome(match, match ? "application id generator condition matched" : "application id generator condition failed");
    }
}
