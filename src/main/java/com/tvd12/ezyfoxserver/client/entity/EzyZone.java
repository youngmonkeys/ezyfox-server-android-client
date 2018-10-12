package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.manager.EzyAppManager;
import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyZone extends EzyDestroyable {

    int getId();

    String getName();

    EzyClient getClient();

    EzyAppManager getAppManager();

}
