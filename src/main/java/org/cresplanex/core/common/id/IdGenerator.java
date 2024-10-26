package org.cresplanex.core.common.id;

import java.util.Optional;

/**
 * 一意のIDを生成するためのインターフェース。
 * <p>
 * IDの生成にはデータベースIDやパーティションオフセットなどのオプションのパラメータが使用されます。
 * また、生成されたIDを文字列形式で取得する機能も提供します。
 * </p>
 */
public interface IdGenerator {

    /**
     * データベースIDが必要かどうかを返します。
     *
     * @return データベースIDが必要な場合はtrue、不要な場合はfalse
     */
    boolean databaseIdRequired();

    /**
     * 指定されたデータベースIDとパーティションオフセットを使用して、128ビットの一意なIDを生成します。
     *
     * @param databaseId オプションのデータベースID。IDの一意性に寄与します。
     * @param partitionOffset オプションのパーティションオフセット。IDの一意性に寄与します。
     * @return 生成された128ビットの一意なID
     */
    Int128 genId(Long databaseId, Integer partitionOffset);

    /**
     * 指定されたデータベースIDとパーティションオフセットを使用して、文字列形式の128ビットIDを生成します。
     *
     * @param databaseId オプションのデータベースID。IDの一意性に寄与します。
     * @param partitionOffset オプションのパーティションオフセット。IDの一意性に寄与します。
     * @return 文字列形式の128ビット一意なID
     */
    default String genIdAsString(Long databaseId, Integer partitionOffset) {
        return genId(databaseId, partitionOffset).asString();
    }

    /**
     * データベースIDとパーティションオフセットなしで、128ビットの一意なIDを生成します。
     *
     * @return 生成された128ビットの一意なID
     */
    default Int128 genId() {
        return genId(null, null);
    }

    /**
     * データベースIDとパーティションオフセットなしで、文字列形式の128ビットIDを生成します。
     *
     * @return 文字列形式の128ビット一意なID
     */
    default String genIdAsString() {
        return genId().asString();
    }

    /**
     * 指定されたIDを基準にして、可能であればインクリメントされたIDを生成します。 インクリメントが不可能な場合は空のOptionalを返します。
     *
     * @param anchorId 基準となるID
     * @return インクリメントされたIDが存在する場合はそれを含むOptional、インクリメントが不可能な場合は空のOptional
     */
    Optional<Int128> incrementIdIfPossible(Int128 anchorId);
}
