package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzySimpleZone implements EzyZone, EzyMeAware {

    protected EzyUser me;
    protected final int id;
    protected final String name;
    protected final EzyClient client;
    protected final List<EzyApp> appList = new ArrayList<>();
    protected final Map<Integer, EzyApp> appByIds = new ConcurrentHashMap<>();
    protected final Map<String, EzyApp> appByNames = new ConcurrentHashMap<>();
    protected final Map<String, EzyAppDataHandlers> appDataHandlerss;

    public EzySimpleZone(EzyClient client, int id, String name) {
        this.id = id;
        this.name = name;
        this.me = me;
        this.client = client;
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
    public EzyApp getApp() {
        if(appList.isEmpty())
            throw new IllegalStateException("has no app in zone: " + name);
        EzyApp app = appList.get(0);
        return app;
    }

    @Override
    public void addApp(EzyApp app) {
        this.appList.add(app);
        this.appByIds.put(app.getId(), app);
        this.appByNames.put(app.getName(), app);
    }

    @Override
    public EzyApp getAppById(int appId) {
        EzyApp app = appByIds.get(appId);
        if(app == null)
            throw new IllegalArgumentException("has no app with id = " + appId);
        return app;
    }

    @Override
    public EzyApp getAppByName(String appName) {
        EzyApp app = appByNames.get(appName);
        if(app == null)
            throw new IllegalArgumentException("has no app with name = " + appName);
        return app;
    }

    @Override
    public List<EzyApp> getAppList() {
        List<EzyApp> list = new ArrayList<>(appList);
        return list;
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
