package org.cresplanex.core.saga.orchestration;

public interface SqlQueryRow {

    String getString(String name);

    boolean getBoolean(String name);
}
