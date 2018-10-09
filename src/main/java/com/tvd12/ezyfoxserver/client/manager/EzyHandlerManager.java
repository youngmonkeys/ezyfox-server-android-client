package com.tvd12.ezyfoxserver.client.manager;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyAppResponseHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyPongHandler;
import com.tvd12.ezyfoxserver.client.socket.EzyPingSchedule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 10/9/18.
 */

public class EzyHandlerManager {

    private final EzyClient client;
    private final EzyPingSchedule pingSchedule;
    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final Map<String, Map<String, EzyAppDataHandlers>> zoneAppDataHandlerss;

    public EzyHandlerManager(EzyClient client, EzyPingSchedule pingSchedule) {
        this.client = client;
        this.pingSchedule = pingSchedule;
        this.eventHandlers = newEventHandlers();
        this.dataHandlers = newDataHandlers();
        this.zoneAppDataHandlerss = new HashMap<>();
    }

    private EzyEventHandlers newEventHandlers() {
        EzyEventHandlers handlers = new EzyEventHandlers(client, pingSchedule);
        return handlers;
    }

    private EzyDataHandlers newDataHandlers() {
        EzyDataHandlers handlers = new EzyDataHandlers(client, pingSchedule);
        handlers.addHandler(EzyCommand.PONG, new EzyPongHandler());
        handlers.addHandler(EzyCommand.APP_REQUEST, new EzyAppResponseHandler());
        return handlers;
    }

    public EzyDataHandler getDataHandler(Object cmd) {
        return dataHandlers.getHandler(cmd);
    }

    public EzyEventHandler getEventHandler(EzyConstant eventType) {
        return eventHandlers.getHandler(eventType);
    }

    public Map<String, EzyAppDataHandlers> getAppDataHandlerss(String zoneName) {
        Map<String, EzyAppDataHandlers> answer = zoneAppDataHandlerss.get(zoneName);
        if(answer == null) {
            answer = new HashMap<>();
            zoneAppDataHandlerss.put(zoneName, answer);
        }
        return answer;
    }

    public void addDataHandler(Object cmd, EzyDataHandler dataHandler) {
        dataHandlers.addHandler(cmd, dataHandler);
    }

    public void addEventHandler(EzyConstant eventType, EzyEventHandler eventHandler) {
        eventHandlers.addHandler(eventType, eventHandler);
    }

    public Map<String,EzyAppDataHandlers> getDataHandlers(String zoneName) {
        Map<String, EzyAppDataHandlers> appDataHandlerss = zoneAppDataHandlerss.get(zoneName);
        if(appDataHandlerss == null) {
            appDataHandlerss = new HashMap<>();
            zoneAppDataHandlerss.put(zoneName, appDataHandlerss);
        }
        return appDataHandlerss;
    }
}
