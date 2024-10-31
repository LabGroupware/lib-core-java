/**
 * 負荷に伴ったパーティションの再割り当てを行うためのクラスを提供します。
 *
 * バックプレッシャー管理状態を表現する{@link BackPressureManagerState}インターフェース,
 * その実装クラスである{@link BackPressureManagerNormalState}クラス,
 * {@link BackPressureManagerPausedState}クラスを提供し,
 * それぞれの通常状態と一時停止状態におけるバックログ(未処理状況)と{@link BackPressureConfig}に応じた振る舞い({@link BackPressureActions})と,
 * それに伴う状態遷移を管理する{@link BackPressureManagerStateAndActions}クラスを提供する.
 *
 * これを統合して管理するのが{@link BackPressureManager}クラスとなり, このパッケージの主要なクラスとなる.
 * {@link org.cresplanex.core.common.kafka.base.CoreKafkaConsumer}クラスでの負荷制御に使用される.
 */
package org.cresplanex.core.common.kafka.baskpressure;
