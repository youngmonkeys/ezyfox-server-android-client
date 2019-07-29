package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.config.EzyReconnectConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.event.EzyDisconnectionEvent;
import com.tvd12.ezyfoxserver.client.util.EzyLogger;

/**
 * Created by tavandung12 on 10/3/18.
 */

public class EzyDisconnectionHandler extends EzyAbstractEventHandler<EzyDisconnectionEvent> {

    @Override
    public final void handle(EzyDisconnectionEvent event) {
        EzyLogger.info("handle disconnection, reason: " + event.getReason());
        preHandle(event);
        EzyClientConfig config = client.getConfig();
        EzyReconnectConfig reconnectConfig = config.getReconnect();
        boolean shouldReconnect = shouldReconnect(event);
        boolean mustReconnect = reconnectConfig.isEnable() && shouldReconnect;
        boolean reconnecting = false;
        if(mustReconnect)
            reconnecting = client.reconnect();
        if(!reconnecting) {
            client.setStatus(EzyConnectionStatus.DISCONNECTED);
            control(event);
        }
    }

    protected void preHandle(EzyDisconnectionEvent event) {
    }

    protected boolean shouldReconnect(EzyDisconnectionEvent event) {
        return true;
    }

    protected void control(EzyDisconnectionEvent event) {
    }
}
