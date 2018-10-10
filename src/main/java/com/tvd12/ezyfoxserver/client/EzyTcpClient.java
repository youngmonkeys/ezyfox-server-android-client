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
import com.tvd12.ezyfoxserver.client.manager.EzyHandlerManager;
import com.tvd12.ezyfoxserver.client.manager.EzyPingManager;
import com.tvd12.ezyfoxserver.client.manager.EzySimpleHandlerManager;
import com.tvd12.ezyfoxserver.client.manager.EzySimplePingManager;
import com.tvd12.ezyfoxserver.client.manager.EzySimpleZoneManager;
import com.tvd12.ezyfoxserver.client.manager.EzyZoneManager;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.socket.EzyPingSchedule;
import com.tvd12.ezyfoxserver.client.socket.EzySocketClient;
import com.tvd12.ezyfoxserver.client.socket.EzyTcpSocketClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by tavandung12 on 9/20/18.
 */

public class EzyTcpClient
        extends EzyEntity
        implements EzyClient, EzyConnectionStatusAware {

    private final Object name;
    private final EzyZoneManager zoneManager;
    private final EzyPingManager pingManager;
    private final EzyHandlerManager handlerManager;
    private final Map<Integer, EzyApp> appsById;
    private final EzyReconnectConfig reconnectConfig;

    private EzyConstant status;
    private final Object statusLock;
    private final Set<Object> unloggableCommands;

    private final EzySocketClient socketClient;
    private final EzyPingSchedule pingSchedule;

    public EzyTcpClient() {
        this(DEFAULT_CLIENT_NAME);
    }

    public EzyTcpClient(Object name) {
        this(name, new EzyReconnectConfig());
    }

    public EzyTcpClient(Object name, EzyReconnectConfig reconnectConfig) {
        this.name = name;
        this.reconnectConfig = reconnectConfig;
        this.status = EzyConnectionStatus.NULL;
        this.statusLock = new Object();
        this.unloggableCommands = newUnloggableCommands();
        this.zoneManager = new EzySimpleZoneManager();
        this.pingManager = new EzySimplePingManager();
        this.appsById = new HashMap<>();
        this.pingSchedule = new EzyPingSchedule(this);
        this.handlerManager = newHandlerManager();
        this.socketClient = newSocketClient();
        this.initProperties();
    }

    private void initProperties() {
        this.properties.put(EzySetup.class, newSetupCommand());
    }

    private EzyHandlerManager newHandlerManager() {
        return new EzySimpleHandlerManager(this, pingSchedule);
    }

    private Set<Object> newUnloggableCommands() {
        Set<Object> set = new HashSet<>();
        set.add(EzyCommand.PING);
        set.add(EzyCommand.PONG);
        return set;
    }

    private EzySetup newSetupCommand() {
        return new EzySimpleSetup(handlerManager);
    }

    private EzySocketClient newSocketClient() {
        EzyTcpSocketClient client = new EzyTcpSocketClient(
                reconnectConfig,
                handlerManager,
                pingManager,
                pingSchedule, unloggableCommands);
        return client;
    }

    @Override
    public void connect(String host, int port) {
        try {
            zoneManager.reset();
            socketClient.connect(host, port);
            setStatus(EzyConnectionStatus.CONNECTING);
        } catch (Exception e) {
            Log.e("ezyfox-client", "connect to server error", e);
        }
    }

    @Override
    public void connect() {
        zoneManager.reset();
        socketClient.connect();
        setStatus(EzyConnectionStatus.CONNECTING);
    }

    @Override
    public boolean reconnect() {
        zoneManager.reset();
        boolean success = socketClient.reconnect();
        if(success)
            setStatus(EzyConnectionStatus.RECONNECTING);
        return success;
    }

    @Override
    public void disconnect() {
        socketClient.disconnect();
        setStatus(EzyConnectionStatus.DISCONNECTED);
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
    public EzyConstant getStatus() {
        synchronized (statusLock) {
            return status;
        }
    }

    @Override
    public void setStatus(EzyConstant status) {
        synchronized (statusLock) {
            this.status = status;
        }
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
    public EzyZoneManager getZoneManager() {
        return zoneManager;
    }

    @Override
    public EzyPingManager getPingManager() {
        return pingManager;
    }

    @Override
    public EzyHandlerManager getHandlerManager() {
        return handlerManager;
    }
}
