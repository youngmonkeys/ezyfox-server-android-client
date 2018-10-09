package com.tvd12.ezyfoxserver.client.handler;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.entity.EzyApp;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyData;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzyAppResponseHandler extends EzyAbstractDataHandler {

    @Override
    public void handle(EzyArray data) {
        int appId = data.get(0, int.class);
        EzyArray commandData = data.get(1, EzyArray.class);
        String cmd = commandData.get(0, String.class);
        EzyData responseData = commandData.get(1, EzyData.class, null);

        EzyApp app = client.getAppById(appId);
        EzyAppDataHandler dataHandler = app.getDataHandler(cmd);
        if(dataHandler != null)
            dataHandler.handle(app, responseData);
        else
            Log.w("ezyfox-client", "app: " + app.getName() + " has no handler for command: " + cmd);
    }
}
