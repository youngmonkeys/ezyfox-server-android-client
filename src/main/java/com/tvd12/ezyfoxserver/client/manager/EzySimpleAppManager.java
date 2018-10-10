package com.tvd12.ezyfoxserver.client.manager;

import com.tvd12.ezyfoxserver.client.entity.EzyApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/10/18.
 */

public class EzySimpleAppManager implements EzyAppManager {

    protected final String zoneName;
    protected final List<EzyApp> appList;
    protected final Map<Integer, EzyApp> appsById;
    protected final Map<String, EzyApp> appsByName;

    public EzySimpleAppManager(String zoneName) {
        this.zoneName = zoneName;
        this.appList = new ArrayList<>();
        this.appsById = new ConcurrentHashMap<>();
        this.appsByName = new ConcurrentHashMap<>();
    }

    @Override
    public void addApp(EzyApp app) {
        this.appList.add(app);
        this.appsById.put(app.getId(), app);
        this.appsByName.put(app.getName(), app);
    }

    @Override
    public EzyApp getApp() {
        if(appList.isEmpty())
            throw new IllegalStateException("has no app in zone: " + zoneName);
        EzyApp app = appList.get(0);
        return app;
    }

    @Override
    public List<EzyApp> getAppList() {
        List<EzyApp> list = new ArrayList<>(appList);
        return list;
    }

    @Override
    public EzyApp getAppById(int appId) {
        EzyApp app = appsById.get(appId);
        return app;
    }

    @Override
    public EzyApp getAppByName(String appName) {
        EzyApp app = appsByName.get(appName);
        return app;
    }
}
