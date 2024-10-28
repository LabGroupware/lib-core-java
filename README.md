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

## Schema

Core内で使用されるデータベースのスキーマはデフォルトで`core`が指定されている.異なるものを使用したい場合は以下のように指定する.
スキーマがない場合は, `none`を指定する.

``` yaml
core:
    database:
        schema: none
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
        id: 1 # database id generatorを使用する
    database:
        schema: core # 使用するデータベーススキーマ名

spring:
    datasource:
        driver-class-name: org.postgresql.Driver # 使用するドライバーの指定
```

environment
``` sh
CUSTOM_MAC_ADDRESS=73588229205 # application id generatorで使用されるmacアドレスを手動設定
```