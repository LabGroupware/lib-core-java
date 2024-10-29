# lib-core-java

## IdGenerator
> パッケージ内では, `IdGenerator`インタフェースを用いてIDの生成を行う.
> デフォルトではこの実装クラスとして, `ApplicationIdGenerator`か`DatabaseIdGenerator`が存在している.

### ApplicationIdGenerator

前半64ビットはcurrentPeriod, 後半64ビットはMacアドレス(48bit)とカウンタ(16bit)を用いた値を使用する.

#### currentPeriodとcounter

currentPeriodには, 現時刻(millisecond)を使用し, counterの値は1ずつ増減させる.<br>
以前の時刻から変更があった場合とcounterがmax(16bit)に至った際に, counterを0に戻す.ただし, counterの値がmaxになった場合は, 時刻が変更されることを待ってから, counterを0に戻す.

#### MACアドレス

MAC_ADDRESSはネットワークインタフェースから取得されるが, 自分で指定したい場合は, `CUSTOM_MAC_ADDRESS`を指定する.
指定したい場合は, `00:11:22:33:44:55`などのMAC_ADDRESSをLong 値に変換し, その10進数値を設定.
```sh
# 00:11:22:33:44:55を使用したい場合
CUSTOM_MAC_ADDRESS=73588229205
```

参考として, 内部ではこのように変換されている.
``` java
    private Long toLong(byte[] bytes) {
        long result = 0L;
        for (byte b : bytes) {
            result = (result << 8) + (b & 0xff);
        }
        return result;
    }
```

#### incrementIdIfPossible
incrementの際は, 何もせず, generateを行うだけで, IDの枯渇にはならない.

### DatabaseIdGenerator

前半64ビットはdatabaseID, 後半64ビットは`serviceId + (partitionOffset == null ? 0 : partitionOffset)`を使用する.

#### databaseID

データベースが生成するIDであり, `autoIncrement`で生成した値などが入ることになる.

#### serviceID

serviceIDは以下のプロパティで指定可能である.
``` yaml
core:
    outbox:
        id: 1
```

#### incrementIdIfPossible
counterは後半64bitのうち, 上位16bitとなり, これがmax値(65535)となれば, 空を返す(枯渇).
カウンターは毎回インクリメントする.

### 適用方法

[serviceIDのプロパティ](#serviceid)を設定することで, `DatabaseIdGenerator`が適用される.設定しない場合は, `ApplicationIdGenerator`が適用され, 自動生成される.

## Schema

Core内で使用されるデータベースのスキーマはデフォルトで`core`が指定されている.異なるものを使用したい場合は以下のように指定する.
スキーマがない場合は, `none`を指定する.

``` yaml
core:
    database:
        schema: none
```

## 呼び出し関係

### Common/JDBC

``` txt
CoreCommonJdbcOperations: このパッケージのアーティファクト.
-> CoreJdbcOperationsUtils: イベントやメッセージテーブルへのコマンドなどのSQLを発行する.
    -> CoreSqlDialect: 各DBMSの挙動誤差を隠すインタフェース.
-> CoreJdbcStatementExecutor: SQLの実行を行うインタフェース. -> CoreCommonJdbcStatementExecutor(java.sqlによる実装), CoreSpringJdbcStatementExecutor(spring.jdbcによる実装)
-> CoreSqlDialect: 各DBMSの挙動誤差を隠すインタフェース
-> OutboxPartitioningSpec: アウトボックステーブル数とパーティション数を定義したクラス.

CoreSqlDialectOrder: 優先する順序を取得するメソッドを持つインタフェース.
-> CoreSqlDialect: 各DBMSの挙動誤差を隠すインタフェース. -> AbstractCoreSqlDialect: 共通アブストラクト -> DefaultCoreSqlDialect, MsSqlDialect, MySqlDialect, PostgresDialect

SqlDialectSelector: 利用する実装クラスを選択するためのクラス.

CoreTransactionTemplate: このパッケージのアーティファクト, トランザクション内での処理実行を定義するテンプレートインターフェース. -> CoreSpringTransactionTemplate(springによる実装)

CoreSqlException: SQLに関するエラー全般のラッパー -> CoreDuplicateKeyException: 重複キーエラー
```

### messaging/kafka

``` txt
CoreKafkaConsumer: コンシューマーの実装クラス
-> CoreKafkaConsumerMessageHandler: メッセージハンドラインタフェース -> MessageConsumerKafkaImplの中で定義されている.
-> KafkaConsumerFactory: Kafkaコンシューマー(原子操作)を生成するファクトリ -> DefaultKafkaConsumerFactory
    -> KafkaMessageConsumer: Kafkaメッセージコンシューマ(原子操作) -> DefaultKafkaMessageConsumer
-> BackPressureConfig: Kafkaコンシューマの負荷制御のためのしきい値(低・高)を設定
-> CoreKafkaConsumerState: コンシューマの状態.
-> ConsumerCallbacks: Kafkaコンシューマのコミットに関連するコールバックを定義するインターフェース.
-> ConsumerPropertiesFactory: Kafkaの接続用デフォルトプロパティ + CoreKafkaConsumerConfigurationProperties: Kafkaプロパティ(上書き)
-> KafkaMessageProcessor: Kafkaメッセージの処理と, 成功したメッセージオフセットのトラッキングを行う.
    -> CoreKafkaConsumerMessageHandler: メッセージハンドラインタフェース
    -> OffsetTracker: パーティションとオフセット情報のマッピング.
        -> TopicPartitionOffsets: オフセット情報.
    -> BackPressureManager: 負荷状況に応じて, トピックパーティションの一時停止や再開を制御.
        -> BackPressureManagerStateAndActions: 負荷制御におけるマネージャステートとアクション.
            -> BackPressureActions: 負荷制御に対して, どんなアクションをとるか.
        -> BackPressureConfig
        -> BackPressureManagerState: バックプレッシャーのステートインタフェース. -> BackPressureManagerNormalState, BackPressureManagerPausedState
    -> MessageConsumerBacklog: バックログを表す. -> SwimlaneDispatcherBacklog

KafkaMessage: Kafkaで扱うメッセージ
KafkaMessageHandler, ReactiveKafkaMessageHandler: Kafkaメッセージハンドラのインタフェース.
KafkaSubscription: サブスクリプションを表し, closeで呼び出されるコールバックを登録できる.

MessageConsumerKafkaImpl: 複数のKafkaメッセージを管理し, 指定されたハンドラーでメッセージを処理.(スイムレーン以外はCoreKafkaConsumerのラッパー)
-> CoreKafkaConsumer: コンシューマの実装クラス.
-> SwimlaneBasedDispatcher: スイムレーンごとに振り分けて処理するためのディスパッチャ, スイムレーンごとに別々の処理キューを持ち、メッセージの分散処理を行う.これを利用したCoreKafkaConsumerMessageHandlerを使って, CoreKafkaConsumerの生成を行う.
    -> RawKafkaMessage: 生のKafkaMessageデータを使用.
    -> TopicPartitionToSwimlaneMapping: パーティションを基に, メッセージ処理のスイムレーンIDを割り当てるインターフェース. -> SwimlanePerTopicPartition(パーティションごとに個別のスイムレーン), OriginalTopicPartitionToSwimlaneMapping(パーティションIDをそのままスイムレーンとして使用(Default)), MultipleSwimlanesPerTopicPartitionMapping(パーティションごとに複数のスイムレーンをマッピング)
    -> SwimlaneDispatcher: スイムレーンに基づいてメッセージをキューに蓄積し, 順次処理するディスパッチャ.ただし, 実際の処理内容は, MessageConsumerKafkaImpl内のhandleで定義している.また, この中でもRawKafkaMessageからKafkaMessageに変換されたあと, KafkaMessageHandler, ReactiveKafkaMessageHandlerが呼ばれている.
-> KafkaSubscription: CoreKafkaConsumerの生成後, これのサブスクリプションを解除できるように, コールバックを設定したKafkaSubscriptionを返す.

CommonMessageConsumer: 複数のKafkaメッセージを管理し、 指定されたハンドラーでメッセージを処理. -> MessageConsumerKafkaImpl
```

## Logging

``` yaml
logging:
    level:
        org.cresplanex.core: DEBUG
```

## 統合した設定

application.yaml
``` yaml
core:
    outbox:
        id: 1 # database id generatorを使用する場合に指定
    database:
        schema: core # 使用するデータベーススキーマ名
    kafka: # kafkaプロパティ
        bootstrap:
            servers: "localhost:9092" # kafkaサーバーアドレス
        connection:
            validation:
                timeout: 1000 # 接続検証のタイムアウト時間(Default: 1000, 現在未使用)

spring:
    datasource:
        driver-class-name: org.postgresql.Driver # 使用するドライバーの指定
```

environment
``` sh
CUSTOM_MAC_ADDRESS=73588229205 # application id generatorで使用されるmacアドレスを手動設定
```