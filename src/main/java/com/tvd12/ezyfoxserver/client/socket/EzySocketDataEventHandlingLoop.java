package com.tvd12.ezyfoxserver.client.socket;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.util.EzyStartable;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySocketDataEventHandlingLoop implements EzyStartable {

    private volatile boolean active = true;
    private final EzySocketDataEventHandler socketDataEventHandler;

    public EzySocketDataEventHandlingLoop(EzySocketDataEventHandler socketDataEventHandler) {
        this.socketDataEventHandler = socketDataEventHandler;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void start() throws Exception {
        Log.i("ezyfox-client", "socket data event handling loop start");
        while (active) {
            socketDataEventHandler.handleEvent();
        }
    }
}
