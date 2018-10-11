package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.entity.EzyMeAware;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleZone;
import com.tvd12.ezyfoxserver.client.entity.EzyUser;
import com.tvd12.ezyfoxserver.client.entity.EzyZone;
import com.tvd12.ezyfoxserver.client.entity.EzyZoneAware;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;

/**
 * Created by tavandung12 on 10/1/18.
 */

public class EzyLoginSuccessHandler extends EzyAbstractDataHandler {

    @Override
    public void handle(EzyArray data) {
        EzyArray joinedApps = data.get(4, EzyArray.class);
        EzyData responseData = data.get(5, EzyData.class);
        EzyUser user = newUser(data);
        EzyZone zone = newZone(data);
        ((EzyMeAware)client).setMe(user);
        ((EzyZoneAware)client).setZone(zone);
        handleResponseAppDatas(zone.getId(), joinedApps);
        handleResponseData(responseData);
        if(joinedApps.isEmpty())
            handleLoginSuccess(responseData);
        else
            handleReconnectSuccess(responseData);
    }

    protected void handleResponseData(EzyData responseData) {
    }

    protected void handleResponseAppDatas(int zoneId, EzyArray appDatas) {
        EzyDataHandler appAccessHandler =
                handlerManager.getDataHandler(EzyCommand.APP_ACCESS);
        for(int i = 0 ; i < appDatas.size() ; i++) {
            EzyArray appData = appDatas.get(i, EzyArray.class);
            EzyArray accessAppData = newAccessAppData(zoneId, appData);
            appAccessHandler.handle(accessAppData);
        }
    }

    protected EzyArray newAccessAppData(int zoneId, EzyArray appData) {
        EzyArray accessAppData = EzyEntityFactory.newArrayBuilder()
                .append(zoneId)
                .append(appData.get(0, int.class))
                .append(appData.get(1, String.class))
                .build();
        return accessAppData;
    }

    protected EzyUser newUser(EzyArray data) {
        long userId = data.get(2, long.class);
        String username = data.get(3, String.class);
        EzySimpleUser user = new EzySimpleUser(userId, username);
        return user;
    }

    protected EzyZone newZone(EzyArray data) {
        int zoneId = data.get(0, int.class);
        String zoneName = data.get(1, String.class);
        EzySimpleZone zone = new EzySimpleZone(client, zoneId, zoneName);
        return zone;
    }

    protected void handleLoginSuccess(EzyData responseData) {
    }

    protected void handleReconnectSuccess(EzyData responseData) {
    }
}
