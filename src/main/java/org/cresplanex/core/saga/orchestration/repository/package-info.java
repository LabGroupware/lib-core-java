/**
 * This package contains the repository interfaces for the orchestration of the saga.
 * 
 * Sagaインスタンスの永続化を行うリポジトリインターフェースを提供するパッケージ.
 * {@link SagaInstanceRepository}とその実装である{@link SagaInstanceRepositoryJdbc}
 * {@link SagaInstanceRepositoryJdbc}で使用されるSQLは{@link SagaInstanceRepositorySql}に定義されている.
 * 
 * サーガ情報+参加者情報の作成, 更新, 取得が可能.
 */
package org.cresplanex.core.saga.orchestration.repository;
