/**
 * kafkaのオフセット管理を伴う低レベルの処理を提供.
 *
 * <p>
 * 最終的な成果物は{@link CoreKafkaConsumer}であり, オフセット管理と低レイヤの制御フローを提供.
 *
 * {@link CoreKafkaConsumerState}でステート管理.
 * kafkaのAPI操作は{@link org.cresplanex.core.common.kafka.lower.KafkaConsumerFactory}から生成した{@link org.cresplanex.core.common.kafka.lower.KafkaMessageConsumer}を通じて行う
 *
 * {@link TopicPartitionOffsets}では一つのトピックパーティションに対する未処理と処理済みのオフセットを保持しており,
 * トピックパーティションに紐づけて, マップとして管理しているのが{@link OffsetTracker}となる.
 * {@link KafkaMessageProcessor}では, これを利用したレコードごとの処理を行う.
 * </p>
 *
 * <h2>処理の流れ(常時Stateの更新を行う)</h2>
 * <p>
 * interfaceのみ定義してある{@link ConsumerCallbacks}を実装してDIすることで,
 * 処理の流れに応じてコールバックを実行する.(ただし, 最大で1つまでの登録が可能)
 * </p>
 * <ol>
 * <li>トピック存在を確認.</li>
 * <li>コンシューマからトピックをサブスクライブ.</li>
 * <li>新規スレッドを作成して, 100msおきにpollingを行う.</li>
 * <li>取得したレコードが空でなければ, レコードごとにprocessorに処理(process)を委譡.
 * <ol>
 * <li>OffsetTrackerにレコードのオフセットを未処理として記録.</li>
 * <li>{@link CoreKafkaConsumerMessageHandler}を通じてレコードを処理.ただし,
 * この時に渡すコールバック(処理中に呼ばれる)では, 処理対象のレコードと処理中に発生したエラーを受け取り, 例外が発生した場合は,
 * {@link KafkaMessageProcessorFailedException}として保持し,
 * そうでない場合は処理済みのオフセットとしてそのレコードを追加.(この時, 未処理のオフセットからは削除されないことに注意)</li>
 * <li>上記のハンドラへの処理の移譲の際,
 * その段階でのバックログ(未処理のキュー)情報({@link MessageConsumerBacklog}の実装クラス)が返却されるため,
 * これをprocessorで保持している{@link MessageConsumerBacklog}のセットに追加する.</li>
 * </ol>
 * </li>
 * <li>kafkaに対して, 処理済みオフセットのコミットを行い, {@link TopicPartitionOffsets}の中の,
 * コミットした未処理, 処理済みのオフセットを削除.</li>
 * <li>processorからバックログの合計サイズを取得し, そのサイズに応じてバックプレッシャーの状態を更新.</li>
 * <li>pollingのループは例外発生もしくは, 外部からstopが呼ばれることで終了し,
 * 処理済みのコミットやその該当オフセットのトラッキングからの削除.必要に応じてconsumerのcloseを行う.</li>
 * </ol>
 */
package org.cresplanex.core.common.kafka.base;
