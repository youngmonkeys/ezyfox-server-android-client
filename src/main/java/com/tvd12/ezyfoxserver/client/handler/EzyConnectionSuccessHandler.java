package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.constant.EzyConnectionStatus;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.request.EzyHandshakeRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;

import java.util.UUID;

/**
 * Created by tavandung12 on 10/1/18.
 */

public class EzyConnectionSuccessHandler extends EzyAbstractEventHandler {
    @Override
    public final void handle(EzyEvent event) {
        updateConnectionStatus();
        sendHandshakeRequest();
        postHandle();
    }

    private void updateConnectionStatus() {
        client.setStatus(EzyConnectionStatus.CONNECTED);
    }

    protected void postHandle() {
    }

    protected void sendHandshakeRequest() {
        EzyRequest request = newHandshakeRequest();
        client.send(request);
    }

    protected final EzyRequest newHandshakeRequest() {
        EzyHandshakeRequest request = new EzyHandshakeRequest(
                getClientId(),
                getClientKey(),
                "ANDROID",
                "1.0.0",
                isEnableEncryption(),
                getStoredToken()
        );
        return request;
    }

    protected String getClientId() {
        String id = UUID.randomUUID().toString();
        return id;
    }

    protected String getClientKey() {
        String key = "";
        return key;
    }

    protected boolean isEnableEncryption() {
        return false;
    }

    protected  String getStoredToken() {
        return "";
    }
}
