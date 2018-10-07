package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.manager.EzyAppByIdGroup;
import com.tvd12.ezyfoxserver.client.manager.EzyZoneGroup;
import com.tvd12.ezyfoxserver.client.socket.EzySender;

import java.util.Map;

/**
 * Created by tavandung12 on 9/20/18.
 */

public interface EzyClient
        extends EzySender, EzyZoneGroup, EzyAppByIdGroup {

    Object DEFAULT_CLIENT_NAME = "___ezyfox_client___";

    void connect(String host, int port);

    void connect();

    boolean reconnect();

    Object getName();

    <T> T get(Class<T> key);

    void setLostPingCount(int count);

    int increaseLostPingCount();

    int getMaxLostPingCount();

    EzyConstant getConnectionStatus();

    EzyDataHandler getDataHandler(Object cmd);

    EzyEventHandler getEventHandler(EzyConstant eventType);

    Map<String, EzyAppDataHandlers> getAppDataHandlerss(String zoneName);

}
