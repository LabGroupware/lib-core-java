package org.cresplanex.core.saga.participant;

import org.cresplanex.core.saga.common.LockTarget;

import java.util.Map;
import java.util.Optional;

import org.cresplanex.core.messaging.common.MessageImpl;

public class SagaReplyMessage extends MessageImpl {
  private final Optional<LockTarget> lockTarget;

  public SagaReplyMessage(String body, Map<String, String> headers, Optional<LockTarget> lockTarget) {
    super(body, headers);
    this.lockTarget = lockTarget;
  }

  public Optional<LockTarget> getLockTarget() {
    return lockTarget;
  }

  public boolean hasLockTarget() {
    return lockTarget.isPresent();
  }
}
