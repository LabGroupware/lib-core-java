/**
 * コンシューマのデコレータを提供.
 *
 * 処理の前後に処理を追加するためのデコレータを提供.
 *
 * {@link DecoratedMessageHandlerFactory}では{@link MessageHandlerDecorator}をgetOrderの順にソートし,保持しておき,
 * {@link org.cresplanex.core.messaging.consumer.MessageHandler}を受け取るdecorateメソッドを提供する.
 *
 * このメソッド内では, {@link MessageHandlerDecoratorChainBuilder}を使用しており,
 * 指定されたデコレータリストと最終的な処理を元に, デコレータチェーンを再帰的に構築することで, orderの小さい順(昇順)にA, B,
 * Cのデコレータがある場合, Aの前処理 -> Bの前処理 -> Cの前処理 -> メッセージハンドラの処理 -> Cの後処理 -> Bの後処理 ->
 * Aの後処理となる.
 *
 * 他に以下のようなデフォルトのデコレータが提供されている. 受取, 処理の前処理, 後処理を行う{@link PrePostReceiveMessageHandlerDecorator}, {@link PrePostProcessMessageHandlerDecorator}
 * {@link org.cresplanex.core.messaging.consumer.duplicate.DuplicateMessageDetector}を使用して,
 * 重複メッセージを加味した処理の実行を行う{@link DuplicateDetectingMessageHandlerDecorator}
 *
 * また, 任意で楽観的ロックの失敗時にリトライを行い,
 * メッセージの処理チェーンを実行する{@link OptimisticLockingDecorator}も提供されている.
 */
package org.cresplanex.core.messaging.consumer.decorator;
