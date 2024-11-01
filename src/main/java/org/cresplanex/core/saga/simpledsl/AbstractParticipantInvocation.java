package org.cresplanex.core.saga.simpledsl;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * サガの参加者の呼び出しを抽象化するクラスです。 このクラスは、特定のデータに基づいて呼び出し可能かどうかを判断するための
 * 条件（Predicate）を保持します。
 *
 * @param <Data> サガのデータのタイプ
 */
public abstract class AbstractParticipantInvocation<Data> implements ParticipantInvocation<Data> {

    private final Optional<Predicate<Data>> invocablePredicate;

    /**
     * コンストラクタ。
     *
     * @param invocablePredicate 呼び出しが可能かどうかを判定する条件
     */
    protected AbstractParticipantInvocation(Optional<Predicate<Data>> invocablePredicate) {
        this.invocablePredicate = invocablePredicate;
    }

    /**
     * データに基づいて、この呼び出しが可能かどうかを判定します。
     *
     * @param data 呼び出し判定の対象データ
     * @return 呼び出し可能であれば true、そうでなければ false
     */
    @Override
    public boolean isInvocable(Data data) {
        return invocablePredicate.map(p -> p.test(data)).orElse(true);
    }
}
