/**
 * Provides classes for sending messages to a JDBC destination.
 *
 * {@link org.cresplanex.core.common.jdbc.CoreCommonJdbcOperations}での実装を基に,
 * JDBCにメッセージを保存するためのクラスを提供.
 * 生成されたIDを{@link org.cresplanex.core.messaging.common.Message}のHeader.IDに設定する.
 * 
 * {@link org.cresplanex.core.messaging.producer.MessageProducerImplementation}を実装するように設計.
 */
package org.cresplanex.core.messaging.producer.jdbc;
