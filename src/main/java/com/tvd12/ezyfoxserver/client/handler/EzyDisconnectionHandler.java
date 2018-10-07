package com.tvd12.ezyfoxserver.client.handler;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.EzyConnectionStatusAware;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.event.EzyDisconnectionEvent;

/**
 * Created by tavandung12 on 10/3/18.
 */

public class EzyDisconnectionHandler extends EzyAbstractEventHandler<EzyDisconnectionEvent> {

    @Override
    public final void handle(EzyDisconnectionEvent event) {
        Log.i("ezyfox-client", "handle disconnection, reason = " + event.getReason());
        preHandle(event);
        boolean reconnecting = client.reconnect();
        if(!reconnecting) {
            setSatus();
            control(event);
        }
    }

    protected void preHandle(EzyDisconnectionEvent event) {
    }

    protected void control(EzyDisconnectionEvent event) {
    }

    private void setSatus() {
        ((EzyConnectionStatusAware)client).setConnectionStatus(EzyConnectionStatus.DISCONNECTED);
    }
}
