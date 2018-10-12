package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.socket.EzySender;
import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyApp extends EzySender, EzyDestroyable {

    int getId();

    String getName();

    EzyClient getClient();

    EzyZone getZone();

    <T> T get(Class<T> clazz);

    EzyAppDataHandler getDataHandler(Object cmd);

}
