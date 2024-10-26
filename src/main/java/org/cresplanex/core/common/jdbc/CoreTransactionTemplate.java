package org.cresplanex.core.common.jdbc;

import java.util.function.Supplier;

public interface CoreTransactionTemplate {
  <T> T executeInTransaction(Supplier<T> callback);
}
