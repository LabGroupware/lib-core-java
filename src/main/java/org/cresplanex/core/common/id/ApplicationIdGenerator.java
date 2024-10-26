package org.cresplanex.core.common.id;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * アプリケーションの一意なIDを生成するためのクラス。
 * <p>
 * MACアドレスとタイムスタンプを組み合わせた128ビットのIDを生成します。
 * データベースIDやパーティションオフセットを必要とせず、内部的にカウンターで管理されます。
 * </p>
 */
public class ApplicationIdGenerator implements IdGenerator {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static final long MAX_COUNTER = 1 << 16;

    private Long macAddress;
    private long currentPeriod = timeNow();
    private long counter = 0;

    /**
     * デフォルトコンストラクタ。
     * <p>
     * MACアドレスを取得し、ID生成に使用する準備を行います。 MACアドレスが取得できない場合、例外がスローされます。
     * </p>
     *
     * @throws RuntimeException MACアドレスが取得できない場合、またはSocketExceptionが発生した場合
     */
    public ApplicationIdGenerator() {
        try {
            macAddress = getMacAddress();
            logger.debug("Mac address {}", macAddress);
            if (macAddress == null) {
                throw new RuntimeException("Cannot find mac address");
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * データベースIDが必要かどうかを示します。このクラスではデータベースIDは必要ありません。
     *
     * @return 常にfalse（データベースIDは不要）
     */
    @Override
    public boolean databaseIdRequired() {
        return false;
    }

    /**
     * システムのMACアドレスを取得します。
     * <p>
     * 環境変数「CUSTOM_MAC_ADDRESS」が設定されている場合、その値を使用し、
     * 設定されていない場合はネットワークインタフェースからMACアドレスを取得します。
     * </p>
     *
     * @return MACアドレスのlong値（取得できなかった場合はnull）
     * @throws SocketException ネットワークインタフェースの取得時にエラーが発生した場合
     */
    private Long getMacAddress() throws SocketException {
        String ma = System.getenv("CUSTOM_MAC_ADDRESS");
        if (ma != null) {
            return Long.valueOf(ma);
        }
        Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            if (iface.getHardwareAddress() != null) {
                return toLong(iface.getHardwareAddress());
            }
        }
        return null;
    }

    /**
     * バイト配列をlong値に変換します。
     *
     * @param bytes MACアドレスのバイト配列
     * @return バイト配列から変換されたlong値
     */
    private Long toLong(byte[] bytes) {
        long result = 0L;
        for (byte b : bytes) {
            result = (result << 8) + (b & 0xff);
        }
        return result;
    }

    /**
     * 現在のタイムスタンプをミリ秒単位で取得します。
     *
     * @return 現在のミリ秒単位のタイムスタンプ
     */
    private long timeNow() {
        return System.currentTimeMillis();
    }

    /**
     * 現在の期間（タイムスタンプ）とMACアドレス、カウンターを使用して128ビットのIDを生成します。
     *
     * @return 生成された128ビットの一意なID
     */
    private Int128 makeId() {
        return new Int128(currentPeriod, (macAddress << 16) + counter);
    }

    /**
     * 内部カウンターとタイムスタンプを使用して、新しい128ビットのIDを生成します。
     * カウンターが最大値に達した場合、次の期間に進むことで一意性を確保します。
     *
     * @return 生成された128ビットの一意なID
     */
    public Int128 genIdInternal() {
        long now = timeNow();
        if (currentPeriod != now || counter == MAX_COUNTER) {
            long oldPeriod = this.currentPeriod;
            while ((this.currentPeriod = timeNow()) <= oldPeriod) {
                // Just do nothing
            }
            counter = 0;
        }
        Int128 id = makeId();
        counter = counter + 1;
        return id;
    }

    /**
     * データベースIDやパーティションオフセットなしで、128ビットのIDを生成します。
     *
     * @param databaseId データベースID（この実装では無視されます）
     * @param partitionOffset パーティションオフセット（この実装では無視されます）
     * @return 生成された128ビットの一意なID
     */
    @Override
    public synchronized Int128 genId(Long databaseId, Integer partitionOffset) {
        return genIdInternal();
    }

    /**
     * 基準IDをもとに、インクリメントされたIDを生成します。 インクリメントが不可能な場合でも、この実装では新しいIDを生成します。
     *
     * @param anchorId 基準となるID
     * @return 新しく生成された128ビットの一意なIDを含むOptional
     */
    @Override
    public Optional<Int128> incrementIdIfPossible(Int128 anchorId) {
        return Optional.of(genId(null, null));
    }
}
