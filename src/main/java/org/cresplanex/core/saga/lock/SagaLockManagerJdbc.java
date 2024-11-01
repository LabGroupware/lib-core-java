package org.cresplanex.core.saga.lock;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.common.jdbc.CoreDuplicateKeyException;
import org.cresplanex.core.common.jdbc.CoreJdbcStatementExecutor;
import org.cresplanex.core.common.jdbc.CoreSchema;
import org.cresplanex.core.common.json.mapper.JSonMapper;
import org.cresplanex.core.messaging.common.Message;
import org.cresplanex.core.messaging.common.MessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC を利用して Saga のロックを管理するクラスです。 ロックの取得、メッセージのスタッシュ、およびロックの解放を行います。
 */
public class SagaLockManagerJdbc implements SagaLockManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreJdbcStatementExecutor coreJdbcStatementExecutor;
    private final SagaLockManagerSql sagaLockManagerSql;

    /**
     * コンストラクタ。指定された JDBC ステートメントエグゼキューターとスキーマを用いて初期化します。
     *
     * @param coreJdbcStatementExecutor JDBC ステートメントエグゼキューター
     * @param coreSchema JDBC スキーマ
     */
    public SagaLockManagerJdbc(CoreJdbcStatementExecutor coreJdbcStatementExecutor, CoreSchema coreSchema) {
        this.coreJdbcStatementExecutor = coreJdbcStatementExecutor;
        sagaLockManagerSql = new SagaLockManagerSql(coreSchema);
    }

    /**
     * 指定された Saga に対してロックを取得します。 ロックがすでに取得されている場合、ブロックされているかどうかを確認します。
     *
     * @param sagaType Saga の種類
     * @param sagaId Saga の ID
     * @param target ロック対象
     * @return ロックが成功した場合は {@code true}、失敗した場合は {@code false}
     */
    @Override
    public boolean claimLock(String sagaType, String sagaId, String target) {
        while (true) {
            try {
                // Lock Tableに対してレコードを挿入するSQL(target, saga_type, saga_id)の実行
                // targetが別スレッド, インスタンスなどでロックされている場合は例外がスローされる
                // targetがprimary keyなので, 同一targetに対してレコードを挿入することはできない
                coreJdbcStatementExecutor.update(sagaLockManagerSql.getInsertIntoSagaLockTableSql(), target, sagaType, sagaId);
                logger.debug("Saga {} {} has locked {}", sagaType, sagaId, target);
                // 成功すれば, ok
                return true;
            } catch (CoreDuplicateKeyException e) {
                // Lock tableからtargetを基に行レベルのロックを取得して, その行のsaga_idを取得する
                Optional<String> owningSagaId = selectForUpdate(target);
                if (owningSagaId.isPresent()) {
                    if (owningSagaId.get().equals(sagaId)) { // 別スレッド, インスタンスなどでロックされている場合でも, 同じsaga_idであればそのロックを取得してok
                        return true;
                    } else {
                        // targetが別のsagaにロックされている場合
                        // このときの対処はそれぞれで異なる.
                        logger.debug("Saga {} {} is blocked by {} which has locked {}", sagaType, sagaId, owningSagaId, target);
                        return false;
                    }
                }
                logger.debug("{} is repeating attempt to lock {}", sagaId, target);
            }
        }
    }

    /**
     * 指定されたターゲットに対するロックの所有者を取得します。
     *
     * @param target ロック対象
     * @return 所有者の Saga ID を持つ {@code Optional} オブジェクト
     */
    private Optional<String> selectForUpdate(String target) {
        // Lock Tableからtargetを基に行レベルのロックを取得して, その行のsaga_idを取得する
        return coreJdbcStatementExecutor
                .query(sagaLockManagerSql.getSelectFromSagaLockTableSql(), (rs, rowNum) -> rs.getString("saga_id"), target).stream().findFirst();
    }

    /**
     * メッセージをスタッシュします。指定されたターゲットがロックされている場合に使用します。
     *
     * @param sagaType Saga の種類
     * @param sagaId Saga の ID
     * @param target ロック対象
     * @param message スタッシュするメッセージ
     */
    @Override
    public void stashMessage(String sagaType, String sagaId, String target, Message message) {
        logger.debug("Stashing message from {} for {} : {}", sagaId, target, message);
        // 単にStash Tableにメッセージを挿入するSQLの実行
        coreJdbcStatementExecutor.update(sagaLockManagerSql.getInsertIntoSagaStashTableSql(),
                message.getRequiredHeader(Message.ID),
                target,
                sagaType,
                sagaId,
                JSonMapper.toJson(message.getHeaders()),
                message.getPayload());
    }

    /**
     * 指定された Saga ID に対してロックを解除し、スタッシュされたメッセージを取り出します。
     *
     * @param sagaId Saga の ID
     * @param target ロック対象
     * @return スタッシュされていたメッセージがある場合は {@code Optional<Message>}、ない場合は
     * {@code Optional.empty()}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Optional<Message> unlock(String sagaId, String target) {
        // Lock tableからtargetを基に行レベルのロックを取得して, その行のsaga_idを取得する
        Optional<String> owningSagaId = selectForUpdate(target);

        if (!owningSagaId.isPresent()) {
            throw new RuntimeException("owningSagaId is not present");
        }

        // owningSagaIdがsagaIdと一致しない場合は例外をスロー
        if (!owningSagaId.get().equals(sagaId)) {
            throw new RuntimeException(String.format("Expected owner to be %s but is %s", sagaId, owningSagaId.get()));
        }

        logger.debug("Saga {} has unlocked {}", sagaId, target);

        // targetを基に最も古いスタッシュされたメッセージを取得し,
        // Stash Messageへのインスタンス化を行う
        List<StashedMessage> stashedMessages = coreJdbcStatementExecutor.query(sagaLockManagerSql.getSelectFromSagaStashTableSql(), (rs, rowNum) -> {
            return new StashedMessage(
                    rs.getString("saga_type"),
                    rs.getString("saga_id"),
                    MessageBuilder.withPayload(rs.getString("message_payload"))
                            .withExtraHeaders(
                                    "",
                                    JSonMapper.fromJson(rs.getString("message_headers"), Map.class)
                            )
                            .build()
            );
        }, target);

        // スタッシュされたメッセージがない場合は、Lock Tableからtargetを基にレコードを削除し、Optional.empty()を返す
        // このsagaがtargetに関するロックを解放したことを示す
        if (stashedMessages.isEmpty()) {
            assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getDeleteFromSagaLockTableSql(), target));
            return Optional.empty();
        }

        StashedMessage stashedMessage = stashedMessages.get(0);

        logger.debug("unstashed from {} for {} : {}", sagaId, target, stashedMessage.getMessage());

        // targetを基にlockテーブルのsage_id, saga_typeをスタッシュから取り出したものに更新し、massage_idを基にStash Tableからメッセージを削除する
        assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getUpdateSagaLockTableSql(), stashedMessage.getSagaType(),
                stashedMessage.getSagaId(), target));
        assertEqualToOne(coreJdbcStatementExecutor.update(sagaLockManagerSql.getDeleteFromSagaStashTableSql(), stashedMessage.getMessage().getId()));

        // スタッシュから取り出したメッセージを返す
        return Optional.of(stashedMessage.getMessage());
    }

    /**
     * 更新件数が1件であることを確認します。 1件でない場合、例外をスローします。
     *
     * @param n 更新された行数
     */
    private void assertEqualToOne(int n) {
        if (n != 1) {
            throw new RuntimeException("Expected to update one row but updated: " + n);
        }
    }
}
