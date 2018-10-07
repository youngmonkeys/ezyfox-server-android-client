package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.socket.EzySender;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyApp extends EzySender {

    int getId();

    String getName();

    EzyClient getClient();

    EzyZone getZone();

    <T> T get(Class<T> clazz);

    EzyAppDataHandler getDataHandler(Object cmd);

}
