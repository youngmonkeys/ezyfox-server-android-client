package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.manager.EzyHandlerManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySimpleSetup implements EzySetup {

    private final EzyHandlerManager handlerManager;
    private final Map<String, EzyZoneSetup> zoneSetups;

    public EzySimpleSetup(EzyHandlerManager handlerManager) {
        this.handlerManager = handlerManager;
        this.zoneSetups = new HashMap<>();
    }

    @Override
    public EzySetup addDataHandler(Object cmd, EzyDataHandler dataHandler) {
        handlerManager.addDataHandler(cmd, dataHandler);
        return this;
    }

    @Override
    public EzySetup addEventHandler(EzyEventType eventType, EzyEventHandler eventHandler) {
        handlerManager.addEventHandler(eventType, eventHandler);
        return this;
    }

    @Override
    public EzyZoneSetup setupZone(String zoneName) {
        EzyZoneSetup zoneSetup = zoneSetups.get(zoneName);
        if(zoneSetup == null) {
            Map<String, EzyAppDataHandlers> appDataHandlerss = handlerManager.getDataHandlers(zoneName);
            zoneSetup = new EzySimpleZoneSetup(appDataHandlerss, this);
        }
        return zoneSetup;
    }
}
