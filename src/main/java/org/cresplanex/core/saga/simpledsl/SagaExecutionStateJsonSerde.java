package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.common.json.mapper.JSonMapper;

public class SagaExecutionStateJsonSerde {
  public static SagaExecutionState decodeState(String currentState) {
    return JSonMapper.fromJson(currentState, SagaExecutionState.class);
  }

  public static String encodeState(SagaExecutionState state) {
    return JSonMapper.toJson(state);
  }
}
