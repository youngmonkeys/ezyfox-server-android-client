package com.tvd12.ezyfoxserver.client.config;

import com.tvd12.ezyfoxserver.client.builder.EzyBuilder;

/**
 * Created by tavandung12 on 10/11/18.
 */

public class EzyClientConfig {

    private final String zoneName;
    private final EzyReconnectConfig reconnect;

    protected EzyClientConfig(Builder builder) {
        this.zoneName = builder.zoneName;
        this.reconnect = builder.reconnectConfigBuilder.build();
    }

    public String getZoneName() {
        return zoneName;
    }

    public EzyReconnectConfig getReconnect() {
        return reconnect;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements EzyBuilder<EzyClientConfig> {

        private String zoneName;
        private final EzyReconnectConfig.Builder reconnectConfigBuilder;

        public Builder() {
            this.reconnectConfigBuilder = new EzyReconnectConfig.Builder(this);
        }

        public Builder zoneName(String zoneName) {
            this.zoneName = zoneName;
            return this;
        }

        public EzyReconnectConfig.Builder reconnectConfigBuilder() {
            return reconnectConfigBuilder;
        }

        @Override
        public EzyClientConfig build() {
            return new EzyClientConfig(this);
        }

    }
}
