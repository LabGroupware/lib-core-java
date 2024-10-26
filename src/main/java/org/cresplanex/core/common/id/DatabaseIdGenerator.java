package org.cresplanex.core.common.id;

import java.util.Optional;

/**
 * データベースIDを基に128ビットの一意なIDを生成するクラス。
 * <p>
 * サービスIDとカウンターを使用し、データベースIDを必須とするID生成方式を提供します。
 * </p>
 */
public class DatabaseIdGenerator implements IdGenerator {

    /**
     * サービスIDの最大値
     */
    public static final long SERVICE_ID_MAX_VALUE = 0x0000ffffffffffffL;

    /**
     * カウンターの最大値
     */
    public static final long COUNTER_MAX_VALUE = 0xffffL;

    private final long serviceId;

    /**
     * データベースIDを必須とすることを示します。
     *
     * @return 常にtrue（データベースIDが必要）
     */
    @Override
    public boolean databaseIdRequired() {
        return true;
    }

    /**
     * 指定されたサービスIDで新しいDatabaseIdGeneratorを初期化します。
     * サービスIDは0から{@link #SERVICE_ID_MAX_VALUE}の範囲内である必要があります。
     *
     * @param serviceId 使用するサービスID
     * @throws IllegalArgumentException サービスIDが範囲外の場合
     */
    public DatabaseIdGenerator(long serviceId) {
        this.serviceId = serviceId;

        if (serviceId < 0 || serviceId > SERVICE_ID_MAX_VALUE) {
            throw new IllegalArgumentException(String.format("service id should be between 0 and %s", SERVICE_ID_MAX_VALUE));
        }
    }

    /**
     * データベースIDとパーティションオフセットを使用して、128ビットの一意なIDを生成します。
     * <p>
     * データベースIDは必須であり、指定されていない場合は例外がスローされます。
     * </p>
     *
     * @param databaseId データベースID（必須）
     * @param partitionOffset パーティションオフセット（任意）
     * @return 生成された128ビットの一意なID
     * @throws IllegalArgumentException データベースIDがnullの場合
     */
    @Override
    public Int128 genId(Long databaseId, Integer partitionOffset) {

        if (databaseId == null) {
            throw new IllegalArgumentException("database id is required");
        }

        return new Int128(databaseId, serviceId + (partitionOffset == null ? 0 : partitionOffset));
    }

    /**
     * 指定されたアンカーIDを基に、インクリメントされたIDを生成します。
     * <p>
     * カウンターが最大値に達した場合は、インクリメントが不可能とみなし、空のOptionalを返します。
     * それ以外の場合は、カウンターをインクリメントした新しいIDを生成します。
     * </p>
     *
     * @param anchorId 基準となるID
     * @return インクリメントされたIDが生成可能な場合はそれを含むOptional、生成不可能な場合は空のOptional
     */
    @Override
    public Optional<Int128> incrementIdIfPossible(Int128 anchorId) {
        long counter = anchorId.getLo() >>> 48;

        if (counter == COUNTER_MAX_VALUE) {
            return Optional.empty();
        }

        counter = (++counter) << 48;

        long lo = anchorId.getLo() & 0x0000ffffffffffffL | counter;

        return Optional.of(new Int128(anchorId.getHi(), lo));
    }
}
