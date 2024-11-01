package org.cresplanex.core.saga.orchestration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 送信先とリソースを表すクラス。
 */
public class DestinationAndResource {

    private final String destination;
    private final String resource;

    /**
     * 指定した送信先とリソースでインスタンスを作成します。
     *
     * @param destination 送信先
     * @param resource リソース
     */
    public DestinationAndResource(String destination, String resource) {
        this.destination = destination;
        this.resource = resource;
    }

    /**
     * 送信先を取得します。
     *
     * @return 送信先
     */
    public String getDestination() {
        return destination;
    }

    /**
     * リソースを取得します。
     *
     * @return リソース
     */
    public String getResource() {
        return resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DestinationAndResource that = (DestinationAndResource) o;

        return new EqualsBuilder()
                .append(destination, that.destination)
                .append(resource, that.resource)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(destination)
                .append(resource)
                .toHashCode();
    }
}
