package com.tvd12.ezyfoxserver.client.socket;

/**
 * Created by tavandung12 on 9/20/18.
 */

public abstract class EzyAbstractSocketClient implements EzySocketClient {

    @Override
    public void reconnect() throws Exception {
        disconnect();
        resetComponents();
        connect0();
    }

    protected abstract void connect0();
    protected abstract void resetComponents();

}
