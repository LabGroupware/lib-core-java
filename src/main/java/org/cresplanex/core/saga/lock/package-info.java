/**
 * Sagaのロック機構を提供.
 *
 * 基本クラスと成る{@link LockTarget}では, classとIDを基にロック対象を特定.
 *
 * {@link StashedMessage}は, {@link SagaLockManager}によって処理されるメッセージを表し,
 * {@link StashMessageRequiredException}は,
 * {@link StashedMessage}が必要な場合にスローされる例外を表す.
 *
 * {@link SagaLockManagerSql}では,
 * {@link SagaLockManager}の実装である{@link SagaLockManagerJdbc}のためのSQLクエリステートメントを生成.
 *
 * 前提として, これらの処理は, トランザクションの中で行われることを想定している.
 * 
 * このパッケージで約束されることは.
 * 1. 同一ターゲットの操作は, 同一sagaからのみ行われる.
 * 2. 同一sageでも, 同時に実行されることはない.
 *
 * {@link #claimLock(String, String, String)}は, ロックを取得するために使用され, lockテーブルを使用して,
 * 異なるsagaからの同一ターゲットへのアクセスを防ぐ.
 * 
 * {@link #stashMessage(String, String, String, Message)}は, メッセージをスタッシュするために使用される.
 * 
 * {@link #unlock(String sagaId, String target)}は, ロックを解放するために使用される.
 * 該当targetのstashされたメッセージがある場合は, 該当sagaがそのtargetのロックを取得できるように更新を行い,
 * そのメッセージを返す.
 * stashされたメッセージがない場合は, そのtargetのロックを開放(lock recordの削除)をして, 空のOptionalを返す.
 */
package org.cresplanex.core.saga.lock;
