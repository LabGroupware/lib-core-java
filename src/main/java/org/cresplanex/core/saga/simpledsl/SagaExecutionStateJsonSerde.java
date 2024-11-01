package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.common.json.mapper.JSonMapper;

/**
 * サガの実行状態をJSON形式でシリアライズ・デシリアライズするためのユーティリティクラスです。
 */
public class SagaExecutionStateJsonSerde {

    /**
     * JSON文字列からサガ実行状態をデコードします。
     *
     * @param currentState JSON形式の現在の状態
     * @return デコードされたサガ実行状態
     */
    public static SagaExecutionState decodeState(String currentState) {
        return JSonMapper.fromJson(currentState, SagaExecutionState.class);
    }

    /**
     * サガ実行状態をJSON形式にエンコードします。
     *
     * @param state エンコードするサガ実行状態
     * @return JSON形式のサガ実行状態
     */
    public static String encodeState(SagaExecutionState state) {
        return JSonMapper.toJson(state);
    }
}
