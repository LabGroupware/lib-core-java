/**
 * This package contains classes for consuming messages from Kafka.
 *
 * <p>
 * {@link CoreKafkaMessageConsumer}がこのパッケージの主要なクラスであり, Kafkaからメッセージを受信するためのクラス.
 *
 * {@link KafkaMessageHandler}や{@link ReactiveKafkaMessageHandler}はメッセージを処理するためのインターフェースで,
 * 外部で実装する必要がある.
 * </p>
 *
 * <h3>MessageConsumerKafkaImplの特徴</h3>
 * <ul>
 * <li>ユニークなID(UUID)を持ち, getIDではそれを返す</li>
 * <li>{@link org.cresplanex.core.common.kafka.base.CoreKafkaConsumer}を配列で格納しており,
 * 全てのconsumerをまとめて管理している (ただし,
 * propertyやAPI操作の低レイヤconsumerなどはどのconsumerも統一のものを使う)</li>
 * <li>closeの際はm 全てのconsumerに対してstop(ループ終了)を呼び出す.</li>
 * </ul>
 *
 * <h2>subscribeの処理の流れ</h2>
 * <>
 * <
 * li>subscriberIDごとに{@link SwimlaneBasedDispatcher}を生成.</>
 * <li>以下のようなハンドラ({@link org.cresplanex.core.common.kafka.base.CoreKafkaConsumerMessageHandler})を生成.
 * <ol>
 * <li>{@link org.cresplanex.core.common.kafka.multi.CoreKafkaMultiMessageConverter}により,
 * multiMessageか判別後,
 * multiMessageの場合は複数の{@link org.cresplanex.core.common.kafka.common.KafkaMessage}をバッチ的に,
 * 異なる場合は一つだけを先程のdispatcherでdispatchを呼び出す(呼び出しの際はいずれの場合でも一つの
 * {@link org.cresplanex.core.common.kafka.common.RawKafkaMessage}となっており,
 * 第三引数のconsumerコールバックは後に解説.).内部ではパーティション, メッセージキーによってスイムレーンIDをmapping取得.
 * {@link SwimlaneDispatcher}が存在しない場合は新規作成したあと, dispatch. 更に内部では, キューにメッセージを追加し,
 * {@link SwimlaneDispatcherBacklog}(バックログ(未処理キュー)の状態)を返却する.
 * {@link SwimlaneDispatcher}では非同期に順次処理を行う.ここでの処理は完全に先程第三引数で渡されたconsumerコールバックが実行されることになる.
 * </li>
 * <li>第三引数に渡されるconsumerコールバックでは,
 * {@link org.cresplanex.core.common.kafka.common.RawKafkaMessage}を{@link org.cresplanex.core.common.kafka.common.KafkaMessage}に変換後,
 * {@link org.cresplanex.core.common.kafka.base.CoreKafkaConsumerMessageHandler}のapplyメソッドを呼び出す.要するに,
 * subscribe呼び出し元の{@link KafkaMessageHandler}もしくは{@link ReactiveKafkaMessageHandler}に依存することになる.また,
 * ここではcallbackが呼び出されているが,{@link org.cresplanex.core.common.kafka.base.KafkaMessageProcessor}の実装では,処理済みオフセットへの追加が行われている.
 * </li>
 * </ol>
 * </li>
 * <li>生成したハンドラをもとに,
 * {@link org.cresplanex.core.common.kafka.base.CoreKafkaConsumer}を生成し,
 * customerに追加後, start</li>
 * <li>終了処理(consumerのstopとconsumerの削除)をcloseとして登録した{@link KafkaSubscription}を返す.</li>
 * </ol>
 */
package org.cresplanex.core.common.kafka.consumer;
