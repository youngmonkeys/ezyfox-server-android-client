package com.tvd12.ezyfoxserver.client;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.command.EzySetup;
import com.tvd12.ezyfoxserver.client.command.EzySimpleSetup;
import com.tvd12.ezyfoxserver.client.config.EzyReconnectConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.entity.EzyEntity;
import com.tvd12.ezyfoxserver.client.entity.EzyZone;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyAppResponseHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyPongHandler;
import com.tvd12.ezyfoxserver.client.manager.EzySimpleZoneManager;
import com.tvd12.ezyfoxserver.client.manager.EzyZoneManager;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.socket.EzyPingSchedule;
import com.tvd12.ezyfoxserver.client.socket.EzySocketClient;
import com.tvd12.ezyfoxserver.client.socket.EzyTcpSocketClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tavandung12 on 9/20/18.
 */

public class EzyTcpClient
        extends EzyEntity
        implements EzyClient, EzyConnectionStatusAware {

    private final Object name;
    private final EzyZoneManager zoneManager;
    private final Map<Integer, EzyApp> appsById;
    private final EzyReconnectConfig reconnectConfig;
    private final int maxLostPingCount;
    private final AtomicInteger lostPingCount;

    private EzyConstant connectionStatus;
    private final Object connectionStatusLock;
    private final Set<Object> unloggableCommands;

    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final EzySocketClient socketClient;
    private final EzyPingSchedule pingSchedule;
    private final Map<String, Map<String, EzyAppDataHandlers>> zoneAppDataHandlerss;

    public EzyTcpClient() {
        this(DEFAULT_CLIENT_NAME);
    }

    public EzyTcpClient(Object name) {
        this(name, new EzyReconnectConfig());
    }

    public EzyTcpClient(Object name, EzyReconnectConfig reconnectConfig) {
        this.name = name;
        this.reconnectConfig = reconnectConfig;
        this.maxLostPingCount = 5;
        this.lostPingCount = new AtomicInteger(0);
        this.connectionStatus = EzyConnectionStatus.NULL;
        this.connectionStatusLock = new Object();
        this.unloggableCommands = newUnloggableCommands();
        this.zoneManager = new EzySimpleZoneManager();
        this.appsById = new HashMap<>();
        this.pingSchedule = new EzyPingSchedule(this);
        this.eventHandlers = newEventHandlers();
        this.dataHandlers = newDataHandlers();
        this.socketClient = newSocketClient();
        this.zoneAppDataHandlerss = new HashMap<>();
        this.initProperties();
    }

    private Set<Object> newUnloggableCommands() {
        Set<Object> set = new HashSet<>();
        set.add(EzyCommand.PING);
        set.add(EzyCommand.PONG);
        return set;
    }

    private void initProperties() {
        this.properties.put(EzySetup.class, newSetupCommand());
    }

    private EzySetup newSetupCommand() {
        return new EzySimpleSetup(eventHandlers, dataHandlers, zoneAppDataHandlerss);
    }

    private EzyEventHandlers newEventHandlers() {
        EzyEventHandlers handlers = new EzyEventHandlers(this, pingSchedule);
        return handlers;
    }

    private EzyDataHandlers newDataHandlers() {
        EzyDataHandlers handlers = new EzyDataHandlers(this, pingSchedule);
        handlers.addHandler(EzyCommand.PONG, new EzyPongHandler());
        handlers.addHandler(EzyCommand.APP_REQUEST, new EzyAppResponseHandler());
        return handlers;
    }

    private EzySocketClient newSocketClient() {
        EzyTcpSocketClient client = new EzyTcpSocketClient(
                reconnectConfig,
                eventHandlers,
                dataHandlers,
                pingSchedule, unloggableCommands);
        return client;
    }

    @Override
    public void connect(String host, int port) {
        try {
            EzyConstant oldStatus = getConnectionStatus();
            setConnectionStatus(EzyConnectionStatus.CONNECTING);
            if(oldStatus == EzyConnectionStatus.NULL)
                socketClient.connect(host, port);
            else
                connect();
        } catch (Exception e) {
            Log.e("ezyfox-client", "connect to server error", e);
        }
    }

    @Override
    public void connect() {
        zoneManager.reset();
        socketClient.connect();
        setConnectionStatus(EzyConnectionStatus.CONNECTING);
    }

    @Override
    public boolean reconnect() {
        zoneManager.reset();
        boolean success = socketClient.reconnect();
        if(success)
            setConnectionStatus(EzyConnectionStatus.RECONNECTING);
        return success;
    }

    @Override
    public void send(EzyRequest request) {
        socketClient.send(request);
    }

    @Override
    public void send(Object cmd, EzyData data) {
        socketClient.send(cmd, data);
    }

    public <T> T get(Class<T> key) {
        T instance = getProperty(key);
        return instance;
    }

    @Override
    public Object getName() {
        return name;
    }

    @Override
    public EzyConstant getConnectionStatus() {
        synchronized (connectionStatusLock) {
            return connectionStatus;
        }
    }

    @Override
    public void setConnectionStatus(EzyConstant connectionStatus) {
        synchronized (connectionStatusLock) {
            this.connectionStatus = connectionStatus;
        }
    }

    @Override
    public void setLostPingCount(int count) {
        lostPingCount.set(count);
    }

    @Override
    public int increaseLostPingCount() {
        return lostPingCount.incrementAndGet();
    }

    @Override
    public int getMaxLostPingCount() {
        return maxLostPingCount;
    }

    @Override
    public void addZone(EzyZone zone) {
        zoneManager.addZone(zone);
    }

    @Override
    public EzyZone getZoneById(int zoneId) {
        return zoneManager.getZoneById(zoneId);
    }

    @Override
    public void addApp(EzyApp app) {
        appsById.put(app.getId(), app);
    }

    @Override
    public EzyApp getAppById(int appId) {
        if(appsById.containsKey(appId))
            return appsById.get(appId);
        throw new IllegalArgumentException("has no app with id = " + appId);
    }

    @Override
    public EzyZone getZoneByName(String zoneName) {
        return zoneManager.getZoneByName(zoneName);
    }

    @Override
    public EzyZone getZone() {
        return zoneManager.getZone();
    }

    @Override
    public List<EzyZone> getZoneList() {
        return zoneManager.getZoneList();
    }

    @Override
    public EzyDataHandler getDataHandler(Object cmd) {
        return dataHandlers.getHandler(cmd);
    }

    @Override
    public EzyEventHandler getEventHandler(EzyConstant eventType) {
        return eventHandlers.getHandler(eventType);
    }

    @Override
    public Map<String, EzyAppDataHandlers> getAppDataHandlerss(String zoneName) {
        Map<String, EzyAppDataHandlers> answer = zoneAppDataHandlerss.get(zoneName);
        if(answer == null) {
            answer = new HashMap<>();
            zoneAppDataHandlerss.put(zoneName, answer);
        }
        return answer;
    }
}
