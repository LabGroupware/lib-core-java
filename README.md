# Core For Microservice Java

## Quick Start
``` sh
./gradlew publish
```

## UseCase

---
### Common
---

### IdGenerator
> IDの生成のみの利用.Common/IDの成果物.

#### Configuration

``` java
@Configuration
@Import(CoreIdGeneratorConfiguration.class)
public class IdGeneratorConfiguration {
}
```

#### Application Property

``` yaml
core:
    outbox:
        # id: 1 # database id generatorを使用する場合に指定
    database:
        schema: core # 使用するデータベーススキーマ名
```

#### Environment

``` sh
CUSTOM_MAC_ADDRESS=73588229205 # application id generatorで使用されるmacアドレスを手動設定したい場合.
```

### CoreCommonJdbcOperations
> JDBC操作の利用.Common/JDBCの成果物

#### Configuration
``` java
@Configuration
@Import({CoreSchemaConfiguration.class, // 用意されたCoreSchema名の解決クラスを使用.
    SqlDialectConfiguration.class, // 用意されたMsSQL, MySQL, Postgres, DefaultのSqlDialectを使用.
    OutboxPartitioningSpecConfiguration.class}) // SpringProperty設定のアウトボックステーブル数, パーティション数設定クラスを使用.
public class CoreCommonJdbcOperationsConfiguration {
    /**
     * CoreSqlDialectを実装した独自実装クラス.
     */
    @Bean("customDialect")
    public CustomDialect customDialect() {
        return new CustomDialect();
    }

    /**
     * SQL方言セレクターBeanを作成します。
     * 必要に応じて, 自身で作成したCoreSqlDialect実装クラスにdependsOnさせる.
     *
     * @param dialects SQL方言のコレクション
     * @return SQL方言セレクターBean
     */
    @Bean("sqlDialectSelector")
    @DependsOn({"customDialect"})
    public SqlDialectSelector sqlDialectSelector(Collection<CoreSqlDialect> dialects) {
        return new SqlDialectSelector(dialects);
    }

    /**
     * Spring JDBCのSQL生成テンプレートを利用.
     */
    @Bean
    public CoreTransactionTemplate coreTransactionTemplate(TransactionTemplate transactionTemplate) {
        return new CoreSpringTransactionTemplate(transactionTemplate);
    }

    /**
     * Spring JDBCをExecutorに使用.
     */
    @Bean
    public CoreJdbcStatementExecutor coreJdbcStatementExecutor(JdbcTemplate jdbcTemplate) {
        return new CoreSpringJdbcStatementExecutor(jdbcTemplate);
    }

    /**
     * {@link CoreCommonJdbcOperations} のBeanを作成します。
     *
     * @param coreJdbcStatementExecutor ステートメント実行オブジェクト
     * @param sqlDialectSelector SQL方言のセレクター
     * @param driver データソースのドライバクラス名（プロパティから取得）
     * @return CoreCommonJdbcOperationsインスタンス
     */
    @Bean
    public CoreCommonJdbcOperations coreCommonJdbcOperations(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            SqlDialectSelector sqlDialectSelector,
            @Value("${spring.datasource.driver-class-name}") String driver,
            OutboxPartitioningSpec outboxPartitioningSpec) {
        CoreSqlDialect coreSqlDialect = sqlDialectSelector.getDialect(driver);
        return new CoreCommonJdbcOperations(new CoreJdbcOperationsUtils(coreSqlDialect),
                coreJdbcStatementExecutor, coreSqlDialect, outboxPartitioningSpec);
    }
}
```

#### Application Property

``` yaml
core:
    current:
        time:
            in:
                milliseconds:
                    sql: #{null} # DefaultCoreSqlDialect使用時の現在時刻の設定.
    outbox:
        partitioning:
            outbox:
                tables: #{null}
            message:
                partitions: #{null}
    database:
        schema: core # 使用するデータベーススキーマ名

spring:
    datasource:
        driver-class-name: org.postgresql.Driver # 使用するドライバーの指定
```

### CoreKafkaMessageConsumer
> Kafka Consumer操作の利用.Common/Kafkaの成果物.

#### Configuration
``` java
@Configuration
@Import({
    KafkaConsumerFactoryConfiguration.class, // Kafkaの低レイヤAPI呼び出しにSDKのラッパーを利用.
    CoreKafkaConsumerSpringPropertiesConfiguration.class, // Kafkaコンシューマのプロパティ設定にSpringプロパティの注入を行う.
    CoreKafkaConnectPropertiesConfiguration.class // Kafka接続のプロパティ設定にSpringプロパティの注入を行う.
})
public class CoreKafkaMessageConsumerConfiguration {
    // パーティションからスイムレーンへのデフォルトのマッピング
    // ここではトピックパーティションのパーティションIDをそのままスイムレーンとして使用
    // 他にもトピックパーティションごとに複数のスイムレーンをマッピングするMultipleSwimlanesPerTopicPartitionMapping(メッセージキーのハッシュ値を使用),
    // Kafkaのトピックパーティションごとに個別のスイムレーンを割り当てるSwimlanePerTopicPartitionなどがデフォルトである.
    private final TopicPartitionToSwimlaneMapping partitionToSwimLaneMapping = new OriginalTopicPartitionToSwimlaneMapping();

    /**
     * Kafkaメッセージを消費するCoreKafkaMessageConsumerビーンを作成
     *
     * @param props Kafkaの基本プロパティ
     * @param coreKafkaConsumerProperties Kafkaコンシューマの設定
     * @param kafkaConsumerFactory Kafkaコンシューマを生成するファクトリ
     * @return 設定済みのCoreKafkaMessageConsumerインスタンス
     */
    @Bean
    public CoreKafkaMessageConsumer messageConsumerKafka(CoreKafkaConnectProperties props,
            CoreKafkaConsumerProperties coreKafkaConsumerProperties,
            KafkaConsumerFactory kafkaConsumerFactory) {
        return new CoreKafkaMessageConsumer(props.getBootstrapServers(), coreKafkaConsumerProperties, kafkaConsumerFactory, partitionToSwimLaneMapping);
    }
}
```

#### Application Property

``` yaml
core:
    kafka: # kafkaプロパティ
        bootstrap:
            servers: "localhost:9092" # kafkaサーバーアドレス
        connection:
            validation:
                timeout: 1000 # 接続検証のタイムアウト時間(Default: 1000, 現在未使用)
        consumer:
            properties: {} # Kafkaにそのまま渡されるプロパティ. 下記は既に定義されており, 他に設定したい場合や上書きしたい場合に利用.
                # "bootstrap.servers", bootstrapServers)
                # "group.id", subscriberId)
                # "enable.auto.commit", "false")
                # "key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
                # "value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer")
                # "auto.offset.reset", "earliest")
            back-pressure:
                # low: 0 # バックログ(未処理)の下限で, これを下回ったconsumerは, 一時停止されている場合に再開する.(default: 0)
                # high: 100 # バックログの上限で, これを上回ったconsumerは, 受け付けを一時停止して, 未処理のキューがこれを下回るまで処理のハンドルのみ続ける.(default: maxInteger)
            poll-timeout: 100 # Kafkaコンシューマーのポーリングタイムアウトを指定, default: 100msだが, 現在未使用.
```

---
### Messaging
---

### MessageProducer
> メッセージプロデューサーの実装.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreCommonJdbcOperationsConfiguration.class, // JDBCの実装
    // Util
    CoreIdGeneratorConfiguration.class, // IDGenerator
    // マッピング
    ChannelMappingDefaultConfiguration.class, // デフォルトのチャネルマッピング利用
})
public class MessageProducerConfiguration {

    // JDBCを利用
    @Bean
    public MessageProducerImplementation messageProducerImplementation(CoreCommonJdbcOperations coreCommonJdbcOperations,
            IdGenerator idGenerator,
            CoreSchema coreSchema) {
        return new MessageProducerJdbcImplementation(coreCommonJdbcOperations,
                idGenerator,
                coreSchema);
    }

    @Autowired(required = false)
    private final MessageInterceptor[] messageInterceptors = new MessageInterceptor[0];

    @Bean
    public MessageProducer messageProducer(ChannelMapping channelMapping, MessageProducerImplementation implementation) {
        return new MessageProducerImpl(messageInterceptors, channelMapping, implementation);
    }
}
```

#### Dependency
- JDBC利用の場合
  - [CoreCommonJdbcOperationsConfiguration](#corecommonjdbcoperations)

### MessageConsumer
> メッセージコンシューマの実装.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreKafkaMessageConsumerConfiguration.class, // Kafkaの実装
    // Duplicate実装
    CoreCommonJdbcOperationsConfiguration.class,
    // マッピング
    ChannelMappingDefaultConfiguration.class, // デフォルトのチャネルマッピング利用
    SubscriberMappingDefaultConfiguration.class, // デフォルトのサブスクライバーIDマッピング利用
    // デコレータ
    BuiltInMessageHandlerDecoratorConfiguration.class, // decorator Factory + pre/post handle, pre/post receive interceptor + duplicate handling
    OptimisticLockingDecoratorConfiguration.class // 楽観的ロックの失敗時のリトライ
})
public class MessageConsumerConfiguration {

    // Kafkaを利用
    @Bean
    public MessageConsumerImplementation messageConsumerImplementation(CoreKafkaMessageConsumer coreKafkaMessageConsumer) {
        return new MessageConsumerKafkaImplementation(coreKafkaMessageConsumer);
    }

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    // Duplicate実装
    @Bean
    public DuplicateMessageDetector duplicateMessageDetector(CoreSchema coreSchema,
            SqlDialectSelector sqlDialectSelector,
            CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreTransactionTemplate coreTransactionTemplate) {
        return new SqlTableBasedDuplicateMessageDetector(coreSchema,
                sqlDialectSelector.getDialect(driver).getCurrentTimeInMillisecondsExpression(),
                coreJdbcStatementExecutor,
                coreTransactionTemplate);
    }

    // Consumerの実装
    @Bean
    public MessageConsumer messageConsumer(MessageConsumerImplementation messageConsumerImplementation,
            ChannelMapping channelMapping,
            DecoratedMessageHandlerFactory decoratedMessageHandlerFactory, SubscriberMapping subscriberMapping) {
        return new MessageConsumerImpl(channelMapping, messageConsumerImplementation, decoratedMessageHandlerFactory, subscriberMapping);
    }
}
```

#### Dependency
- Kafka利用の場合
  - [CoreKafkaMessageConsumerConfiguration](#corekafkamessageconsumer)
- DuplicateでのJDBC利用の場合
  - [CoreCommonJdbcOperationsConfiguration](#corecommonjdbcoperations)

### MessageInterceptor
> インタセプタの利用.なお, LoggingMessageInterceptorはデフォルトで定義済み.

#### Configuration
``` java
@Configuration
public class MessageInterceptorConfiguration {

    @Bean
    public MessageInterceptor messageLoggingInterceptor() {
        return new LoggingMessageInterceptor();
    }
}
```

---
### Command
---

### CommandProducer
> コマンドのプロデューサー.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreCommandProducerConfiguration.class
})
public class CommandProducerConfiguration {
}
```

#### Dependency
- [MessageProducerConfiguration](#messageproducer)

### CommandConsumer
> コマンドのコンシューマ.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreCommandConsumerConfiguration.class
})
public class CommandConsumerConfiguration {
}
```

#### Dependency
- [MessageProducerConfiguration](#messageproducer): リプライのプロデューサーとなるため.
- [MessageConsumerConfiguration](#messageconsumer)

---
### Event
---

### EventPublisher
> イベントパブリッシャー.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreEventPublisherConfiguration.class
})
public class EventPublisherConfiguration {
}
```

#### Dependency
- [MessageProducerConfiguration](#messageproducer)

### EventSubscriber
> コマンドのサブスクライバー.

#### Configuration
``` java
@Configuration
@Import({
    // 実装
    CoreEventSubscriberConfiguration.class
})
public class EventSubscriberConfiguration {
}
```

#### Dependency
- [MessageProducerConfiguration](#messageproducer): リプライのプロデューサーとなるため.
- [MessageConsumerConfiguration](#messageconsumer)

---
### Sage
---

### SagaLockManager
> Sagaロックマネージャ

#### Configuration
``` java
@Configuration
public class SagaLockManagerConfiguration {

    @Bean
    public SagaLockManager sagaLockManager(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSchema coreSchema) {
        return new SagaLockManagerJdbc(coreJdbcStatementExecutor, coreSchema);
    }
}
```

#### Dependency
- JDBC利用の場合
  - [CoreCommonJdbcOperationsConfiguration](#corecommonjdbcoperations)

### SagaOrchestrator
> Sagaオーケストラ

#### Configuration
``` java
@Configuration
@Import({
    SagaCommandProducerConfiguration.class
})
public class SagaOrchestratorConfiguration {

    @Bean
    public SagaInstanceRepository sagaInstanceRepository(CoreJdbcStatementExecutor coreJdbcStatementExecutor,
            CoreSchema coreSchema) {
        return new SagaInstanceRepositoryJdbc(coreJdbcStatementExecutor, new ApplicationIdGenerator(), coreSchema);
    }

    @Bean
    public SagaInstanceFactory sagaInstanceFactory(SagaInstanceRepository sagaInstanceRepository, CommandProducer commandProducer, MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager, SagaCommandProducer sagaCommandProducer, Collection<Saga<?>> sagas) {
        SagaManagerFactory smf = new SagaManagerFactory(sagaInstanceRepository, commandProducer, messageConsumer,
                sagaLockManager, sagaCommandProducer);
        return new SagaInstanceFactory(smf, sagas);
    }
}
```

#### Dependency
- [SagaLockManagerConfigurationn](#sagalockmanager)
- JDBC利用の場合
  - [CoreCommonJdbcOperationsConfiguration](#corecommonjdbcoperations)
- [MessageConsumerConfiguration](#messageconsumer)
- [CommandProducerConfiguration](#commandproducer)

### SagaParticipant
> Sagaパーティシパント

#### Configuration
``` java
@Configuration
@Import({
    CommandReplyProducerConfiguration.class,
    CommandNameMappingDefaultConfiguration.class
})
public class SagaParticipantConfiguration {

    @Bean
    public SagaCommandDispatcherFactory sagaCommandDispatcherFactory(MessageConsumer messageConsumer,
            SagaLockManager sagaLockManager,
            CommandNameMapping commandNameMapping,
            CommandReplyProducer commandReplyProducer) {
        return new SagaCommandDispatcherFactory(messageConsumer, sagaLockManager, commandNameMapping, commandReplyProducer);
    }
}

```

#### Dependency
- [SagaLockManagerConfigurationn](#sagalockmanager)
- [MessageConsumerConfiguration](#messageconsumer)

## 詳細

### IdGenerator
> パッケージ内では, `IdGenerator`インタフェースを用いてIDの生成を行う.
> デフォルトではこの実装クラスとして, `ApplicationIdGenerator`か`DatabaseIdGenerator`が存在している.

#### ApplicationIdGenerator

前半64ビットはcurrentPeriod, 後半64ビットはMacアドレス(48bit)とカウンタ(16bit)を用いた値を使用する.

##### currentPeriodとcounter

currentPeriodには, 現時刻(millisecond)を使用し, counterの値は1ずつ増減させる.<br>
以前の時刻から変更があった場合とcounterがmax(16bit)に至った際に, counterを0に戻す.ただし, counterの値がmaxになった場合は, 時刻が変更されることを待ってから, counterを0に戻す.

##### MACアドレス

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

##### incrementIdIfPossible
incrementの際は, 何もせず, generateを行うだけで, IDの枯渇にはならない.

#### DatabaseIdGenerator

前半64ビットはdatabaseID, 後半64ビットは`serviceId + (partitionOffset == null ? 0 : partitionOffset)`を使用する.

##### databaseID

データベースが生成するIDであり, `autoIncrement`で生成した値などが入ることになる.

##### serviceID

serviceIDは以下のプロパティで指定可能である.
``` yaml
core:
    outbox:
        id: 1
```

##### incrementIdIfPossible
counterは後半64bitのうち, 上位16bitとなり, これがmax値(65535)となれば, 空を返す(枯渇).
カウンターは毎回インクリメントする.

#### 適用方法

以下の用意されているConfigurationをimportすることで, [serviceIDのプロパティ](#serviceid)を設定することで, `DatabaseIdGenerator`が適用される.設定しない場合は, `ApplicationIdGenerator`が適用され, 自動生成される.
``` java
@Configuration
@Import(IdGeneratorConfiguration.class) // ID Generatorの登録
public class CoreConfiguration {
}
```

### Schema

Core内で使用されるデータベースのスキーマはデフォルトで`core`が指定されている.異なるものを使用したい場合は以下のように指定する.
スキーマがない場合は, `none`を指定する.

``` yaml
core:
    database:
        schema: none
```

## Logging

ログのレベル設定
``` yaml
logging:
    level:
        org.cresplanex.core: DEBUG
```