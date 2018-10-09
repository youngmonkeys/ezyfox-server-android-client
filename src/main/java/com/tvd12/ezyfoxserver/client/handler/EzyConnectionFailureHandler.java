package com.tvd12.ezyfoxserver.client.handler;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.EzyConnectionStatusAware;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;

/**
 * Created by tavandung12 on 10/3/18.
 */

public class EzyConnectionFailureHandler extends EzyAbstractEventHandler<EzyConnectionFailureEvent> {

    @Override
    public final void handle(EzyConnectionFailureEvent event) {
        Log.i("ezyfox-client", "connection failure, reason = " + event.getReason());
        boolean reconnecting = client.reconnect();
        if(!reconnecting) {
            setSatus();
            control(event);
        }
    }

    protected void control(EzyConnectionFailureEvent event) {
    }

    private void setSatus() {
        ((EzyConnectionStatusAware)client).setStatus(EzyConnectionStatus.FAILURE);
    }
}
