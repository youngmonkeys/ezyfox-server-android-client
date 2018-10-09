package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleApp;
import com.tvd12.ezyfoxserver.client.entity.EzyZone;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzyAccessAppHandler extends EzyAbstractDataHandler {

    @Override
    public void handle(EzyArray data) {
        int zoneId = data.get(0, int.class);
        EzyZone zone = client.getZoneById(zoneId);
        EzyApp app = newApp(zone, data);
        zone.addApp(app);
        client.addApp(app);
        postHandle(data);
    }

    protected void postHandle(EzyArray data) {
    }

    protected EzyApp newApp(EzyZone zone, EzyArray data) {
        int appId = data.get(1, int.class);
        String appName = data.get(2, String.class);
        EzySimpleApp app = new EzySimpleApp(zone, appId, appName);
        return app;
    }

}
