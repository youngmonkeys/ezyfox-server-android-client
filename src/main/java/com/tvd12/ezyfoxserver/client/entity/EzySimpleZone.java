package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.manager.EzyAppManager;
import com.tvd12.ezyfoxserver.client.manager.EzySimpleAppManager;

import java.util.Map;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzySimpleZone implements EzyZone {

    protected EzyUser me;
    protected final int id;
    protected final String name;
    protected final EzyClient client;
    protected final EzyAppManager appManager;

    public EzySimpleZone(EzyClient client, int id, String name) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.appManager = new EzySimpleAppManager(name);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EzyClient getClient() {
        return client;
    }

    @Override
    public EzyAppManager getAppManager() {
        return appManager;
    }

    @Override
    public void reset() {
        appManager.clear();
    }
}
