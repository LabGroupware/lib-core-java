package org.cresplanex.core.saga.simpledsl;

import org.cresplanex.core.saga.orchestration.Saga;

public interface SimpleSaga<Data> extends Saga<Data>, SimpleSagaDsl<Data> {
}
