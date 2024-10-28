package org.cresplanex.core.saga.simpledsl;

import static java.util.Collections.singletonMap;
import java.util.Map;

import org.cresplanex.core.commands.common.Command;

public class ParticipantInvocationBuilder {
  private final Map<String, String> params;


  public ParticipantInvocationBuilder(String key, String value) {
    this.params = singletonMap(key, value);
  }

  public <C extends Command>  ParticipantParamsAndCommand<C> withCommand(C command) {
    return new ParticipantParamsAndCommand<>(params, command);
  }
}
