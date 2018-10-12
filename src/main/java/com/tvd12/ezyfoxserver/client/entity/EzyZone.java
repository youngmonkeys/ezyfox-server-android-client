package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.manager.EzyAppManager;
import com.tvd12.ezyfoxserver.client.util.EzyResettable;

/**
 * Created by tavandung12 on 10/2/18.
 */

public interface EzyZone extends EzyResettable {

    int getId();

    String getName();

    EzyClient getClient();

    EzyAppManager getAppManager();

}
