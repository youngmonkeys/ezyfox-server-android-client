package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.manager.EzyAppManager;
import com.tvd12.ezyfoxserver.client.manager.EzySimpleAppManager;

import java.util.Map;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzySimpleZone implements EzyZone, EzyMeAware {

    protected EzyUser me;
    protected final int id;
    protected final String name;
    protected final EzyClient client;
    protected final EzyAppManager appManager;
    protected final Map<String, EzyAppDataHandlers> appDataHandlerss;

    public EzySimpleZone(EzyClient client, int id, String name) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.appManager = new EzySimpleAppManager(name);
        this.appDataHandlerss = client.getHandlerManager().getAppDataHandlerss(name);
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
    public EzyUser getMe() {
        return me;
    }

    @Override
    public void setMe(EzyUser me) {
        this.me = me;
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
    public EzyAppDataHandlers getAppDataHandlers(String appName) {
        EzyAppDataHandlers answer = appDataHandlerss.get(appName);
        if(answer == null) {
            answer = new EzyAppDataHandlers();
            appDataHandlerss.put(appName, answer);
        }
        return answer;
    }
}
