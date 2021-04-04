package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.client.setup.EzySetup;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyUser;
import com.tvd12.ezyfoxserver.client.entity.EzyZone;
import com.tvd12.ezyfoxserver.client.manager.EzyHandlerManager;
import com.tvd12.ezyfoxserver.client.manager.EzyPingManager;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.socket.EzyPingSchedule;

/**
 * Created by tavandung12 on 9/20/18.
 */

public interface EzyClient {
    EzySetup setup();
    void connect(String host, int port);
    boolean reconnect();
    void send(EzyRequest request);
    void send(EzyCommand cmd, EzyArray data);
    void disconnect();
    void disconnect(int reason);
    void processEvents();
    String getName();
    EzyClientConfig getConfig();
    EzyUser getMe();
    EzyZone getZone();
    EzyApp getApp();
    EzyConnectionStatus getStatus();
    boolean isConnected();
    void setStatus(EzyConnectionStatus status);
    EzyApp getAppById(int appId);
    EzyPingManager getPingManager();
    EzyPingSchedule getPingSchedule();
    EzyHandlerManager getHandlerManager();
}
