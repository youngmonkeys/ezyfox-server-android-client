package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.manager.EzyAppManager;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyZone {

    int getId();

    String getName();

    EzyUser getMe();

    EzyClient getClient();

    EzyAppManager getAppManager();

    EzyAppDataHandlers getAppDataHandlers(String appName);

}
