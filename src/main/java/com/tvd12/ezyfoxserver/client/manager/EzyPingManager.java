package com.tvd12.ezyfoxserver.client.manager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tavandung12 on 10/8/18.
 */

public class EzyPingManager {

    private int maxLostPingCount;
    private final AtomicInteger lostPingCount;

    public EzyPingManager() {
        this.maxLostPingCount = 5;
        this.lostPingCount = new AtomicInteger();
    }

    public void setLostPingCount(int count) {
        lostPingCount.set(count);
    }

    public int increaseLostPingCount() {
        return lostPingCount.incrementAndGet();
    }

    public int getMaxLostPingCount() {
        return maxLostPingCount;
    }

}
