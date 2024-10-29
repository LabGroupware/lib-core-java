package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * スイムレーンに基づいてメッセージをキューに蓄積し、順次処理するディスパッチャです。
 * スイムレーンごとに個別のキューでメッセージを処理し、並行処理を行います。
 */
public class SwimlaneDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(SwimlaneDispatcher.class);

    private final String subscriberId;
    private final Integer swimlane;
    private final Executor executor;

    private final LinkedBlockingQueue<QueuedMessage> queue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final SwimlaneDispatcherBacklog consumerStatus = new SwimlaneDispatcherBacklog(queue);

    /**
     * 新しいSwimlaneDispatcherを作成します。
     *
     * @param subscriberId サブスクライバID
     * @param swimlane スイムレーンのインデックス
     * @param executor メッセージ処理に使用するExecutor
     */
    public SwimlaneDispatcher(String subscriberId, Integer swimlane, Executor executor) {
        this.subscriberId = subscriberId;
        this.swimlane = swimlane;
        this.executor = executor;
    }

    /**
     * メッセージをキューに追加し、必要に応じて処理を開始します。
     *
     * @param message キューに追加するメッセージ
     * @param messageConsumer メッセージの処理を行うコンシューマ
     * @return キューのバックログ情報
     */
    public SwimlaneDispatcherBacklog dispatch(RawKafkaMessage message, Consumer<RawKafkaMessage> messageConsumer) {
        synchronized (queue) {
            QueuedMessage queuedMessage = new QueuedMessage(message, messageConsumer);
            queue.add(queuedMessage);
            logger.trace("added message to queue: {} {} {}", subscriberId, swimlane, message);
            if (running.compareAndSet(false, true)) {
                logger.trace("Stopped - attempting to process newly queued message: {} {}", subscriberId, swimlane);
                processNextQueuedMessage();
            } else {
                logger.trace("Running - Not attempting to process newly queued message: {} {}", subscriberId, swimlane);
            }
        }
        return consumerStatus;
    }

    /**
     * キュー内の次のメッセージを処理します。
     */
    private void processNextQueuedMessage() {
        executor.execute(this::processQueuedMessage);
    }

    /**
     * キュー内のメッセージを順次処理します。
     */
    public void processQueuedMessage() {
        while (true) {
            QueuedMessage queuedMessage = getNextMessage();
            if (queuedMessage == null) {
                logger.trace("No queued message for {} {}", subscriberId, swimlane);
                return;
            } else {
                logger.trace("Invoking handler for message for {} {} {}", subscriberId, swimlane, queuedMessage.message);
                try {
                    queuedMessage.messageConsumer.accept(queuedMessage.message);
                } catch (RuntimeException e) {
                    logger.error("Exception handling message - terminating", e);
                    return;
                }
            }
        }
    }

    /**
     * キューから次のメッセージを取得します。
     *
     * @return 次に処理するメッセージ、キューが空の場合はnull
     */
    private QueuedMessage getNextMessage() {
        QueuedMessage queuedMessage = queue.poll();
        if (queuedMessage != null) {
            return queuedMessage;
        }

        synchronized (queue) {
            queuedMessage = queue.poll();
            if (queuedMessage == null) {
                running.compareAndSet(true, false);
            }
            return queuedMessage;
        }
    }

    /**
     * キュー内のメッセージを保持する内部クラスです。
     */
    class QueuedMessage {

        RawKafkaMessage message;
        Consumer<RawKafkaMessage> messageConsumer;

        /**
         * メッセージとその処理コンシューマでキューされたメッセージを作成します。
         *
         * @param message キューされたKafkaメッセージ
         * @param messageConsumer メッセージの処理を行うコンシューマ
         */
        public QueuedMessage(RawKafkaMessage message, Consumer<RawKafkaMessage> messageConsumer) {
            this.message = message;
            this.messageConsumer = messageConsumer;
        }
    }
}
