package org.cresplanex.core.common.jdbc.sqldialect;

import java.util.Collections;

public class DefaultCoreSqlDialect extends AbstractCoreSqlDialect {

    public DefaultCoreSqlDialect(String customCurrentTimeInMillisecondsExpression) {
        super(Collections.emptySet(), Collections.emptySet(), customCurrentTimeInMillisecondsExpression);
    }

    @Override
    public boolean supports(String driver) {
        return true;
    }

    @Override
    public boolean accepts(String name) {
        return true;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
