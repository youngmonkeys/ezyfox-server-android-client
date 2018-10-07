package com.tvd12.ezyfoxserver.client.config;

/**
 * Created by tavandung12 on 10/6/18.
 */

public class EzyReconnectConfig {

    private final boolean enable;
    private final int maxReconnectCount;
    private final int reconnectPeriod;

    public EzyReconnectConfig() {
        this(true);
    }

    public EzyReconnectConfig(boolean enable) {
        this(enable, 3000);
    }

    public EzyReconnectConfig(boolean enable,
                              int reconnectPeriod) {
        this(enable, reconnectPeriod, 5);
    }

    public EzyReconnectConfig(boolean enable,
                              int reconnectPeriod,
                              int maxReconnectCount) {
        this.enable = enable;
        this.maxReconnectCount = maxReconnectCount;
        this.reconnectPeriod = reconnectPeriod;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public int getMaxReconnectCount() {
        return maxReconnectCount;
    }

    public int getReconnectPeriod() {
        return reconnectPeriod;
    }
}
