package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySimpleSetup implements EzySetup {

    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final Map<String, EzyZoneSetup> zoneSetups;
    private final Map<String, Map<String, EzyAppDataHandlers>> zoneAppDataHandlerss;

    public EzySimpleSetup(EzyEventHandlers eventHandlers,
                          EzyDataHandlers dataHandlers,
                          Map<String, Map<String, EzyAppDataHandlers>> zoneAppDataHandlerss) {
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
        this.zoneSetups = new HashMap<>();
        this.zoneAppDataHandlerss = zoneAppDataHandlerss;
    }

    @Override
    public EzySetup addDataHandler(Object cmd, EzyDataHandler dataHandler) {
        dataHandlers.addHandler(cmd, dataHandler);
        return this;
    }

    @Override
    public EzySetup addEventHandler(EzyEventType eventType, EzyEventHandler eventHandler) {
        eventHandlers.addHandler(eventType, eventHandler);
        return this;
    }

    @Override
    public EzyZoneSetup setupZone(String zoneName) {
        EzyZoneSetup zoneSetup = zoneSetups.get(zoneName);
        if(zoneSetup == null) {
            Map<String, EzyAppDataHandlers> appDataHandlerss = zoneAppDataHandlerss.get(zoneName);
            if(appDataHandlerss == null) {
                appDataHandlerss = new HashMap<>();
                zoneAppDataHandlerss.put(zoneName, appDataHandlerss);
            }
            zoneSetup = new EzySimpleZoneSetup(appDataHandlerss, this);
        }
        return zoneSetup;
    }
}
