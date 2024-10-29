package org.cresplanex.core.messaging.kafka.consumer;

import java.util.concurrent.LinkedBlockingQueue;

import org.cresplanex.core.messaging.kafka.basic.consumer.MessageConsumerBacklog;

/**
 * Kafkaメッセージのバックログを管理するクラスです。 指定されたキューのサイズを返し、処理待ちメッセージの数を提供します。
 */
public class SwimlaneDispatcherBacklog implements MessageConsumerBacklog {

    private final LinkedBlockingQueue<?> queue;

    /**
     * キューを使用してSwimlaneDispatcherBacklogを初期化します。
     *
     * @param queue 処理待ちメッセージを保持するキュー
     */
    public SwimlaneDispatcherBacklog(LinkedBlockingQueue<?> queue) {
        this.queue = queue;
    }

    /**
     * バックログ内の未処理メッセージの数を返します。
     *
     * @return 未処理メッセージの数
     */
    @Override
    public int size() {
        return queue.size();
    }
}
