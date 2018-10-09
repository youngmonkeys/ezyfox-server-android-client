package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 10/4/18.
 */

public class EzySimpleZoneSetup implements EzyZoneSetup {

    private final EzySetup parent;
    private final Map<String, EzyAppSetup> appSetups;
    private final Map<String, EzyAppDataHandlers> appDataHandlerss;

    public EzySimpleZoneSetup(Map<String, EzyAppDataHandlers> appDataHandlerss, EzySetup parent) {
        this.parent = parent;
        this.appSetups = new HashMap<>();
        this.appDataHandlerss = appDataHandlerss;
    }

    @Override
    public EzyAppSetup setupApp(String appName) {
        EzyAppSetup appSetup = appSetups.get(appName);
        if(appSetup == null) {
            EzyAppDataHandlers handlers = appDataHandlerss.get(appName);
            if(handlers == null) {
                handlers = new EzyAppDataHandlers();
                appDataHandlerss.put(appName, handlers);
            }
            appSetup = new EzySimpleAppSetup(handlers, this);
            appSetups.put(appName, appSetup);
        }
        return appSetup;
    }

    @Override
    public EzySetup done() {
        return parent;
    }
}
