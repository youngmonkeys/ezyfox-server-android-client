package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.socket.EzySender;
import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.client.util.EzyInstanceFetcher;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyApp extends EzySender, EzyInstanceFetcher, EzyDestroyable {

    int getId();

    String getName();

    EzyClient getClient();

    EzyZone getZone();

    EzyAppDataHandler getDataHandler(Object cmd);

}
