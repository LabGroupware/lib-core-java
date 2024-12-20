package org.cresplanex.core.common.kafka.base;

/**
 * Kafkaコンシューマの状態を表す列挙型です。
 * <p>
 * Kafkaメッセージコンシューマが現在どの状態にあるかを追跡するために使用されます。
 * </p>
 */
public enum CoreKafkaConsumerState {

    /**
     * メッセージ処理に失敗した状態。
     * <p>
     * メッセージ処理中にエラーが発生し、メッセージの処理が失敗したことを示します。</p>
     */
    MESSAGE_HANDLING_FAILED,
    /**
     * コンシューマが開始された状態。
     * <p>
     * Kafkaコンシューマが正常に開始され、メッセージの消費が可能であることを示します。</p>
     */
    STARTED,
    /**
     * コンシューマの起動に失敗した状態。
     * <p>
     * コンシューマの起動プロセスでエラーが発生し、起動に失敗したことを示します。</p>
     */
    FAILED_TO_START,
    /**
     * コンシューマが停止した状態。
     * <p>
     * Kafkaコンシューマが正常に停止されたことを示します。</p>
     */
    STOPPED,
    /**
     * コンシューマが失敗した状態。
     * <p>
     * コンシューマが動作中にエラーが発生し、停止したことを示します。</p>
     */
    FAILED,
    /**
     * コンシューマが作成された直後の状態。
     * <p>
     * Kafkaコンシューマがインスタンス化されたが、まだ開始されていない状態を示します。</p>
     */
    CREATED
}
