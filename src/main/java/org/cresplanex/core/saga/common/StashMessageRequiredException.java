package org.cresplanex.core.saga.common;

public class StashMessageRequiredException extends RuntimeException {

    private final String target;

    public String getTarget() {
        return target;
    }

    public StashMessageRequiredException(String target) {
        this.target = target;

    }
}
