package org.cresplanex.core.messaging.consumer.duplicate;

import org.cresplanex.core.messaging.common.SubscriberIdAndMessage;

/**
 * メッセージの重複を検出するためのインターフェース。
 * 
 * <p>
 * このインターフェースは、メッセージの重複を検出するためのメソッドを提供します。
 * </p>
 */
public interface DuplicateMessageDetector {
  /**
   * 指定されたコンシューマーIDとメッセージIDに対して、重複しているかどうかを判定します。
   * @param consumerId
   * @param messageId
   * @return
   */
  boolean isDuplicate(String consumerId, String messageId);
  /**
   * サブスクライバーIDとメッセージ, その処理を行うハンドラを受け取った上で, メッセージの重複のない場合に指定されたコールバックを実行します。
   */
  void doWithMessage(SubscriberIdAndMessage subscriberIdAndMessage, Runnable callback);
}
