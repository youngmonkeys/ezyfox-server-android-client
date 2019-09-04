package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.entity.EzyArray;

public class EzyLoginErrorHandler extends EzyAbstractDataHandler {

    @Override
    public void handle(EzyArray data) {
        client.disconnect(401);
        handleLoginError(data);
    }

    protected void handleLoginError(EzyArray data) {
    }
}

