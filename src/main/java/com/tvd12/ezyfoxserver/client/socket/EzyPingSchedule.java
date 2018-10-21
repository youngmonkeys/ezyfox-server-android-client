package com.tvd12.ezyfoxserver.client.socket;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.client.event.EzyLostPingEvent;
import com.tvd12.ezyfoxserver.client.manager.EzyPingManager;
import com.tvd12.ezyfoxserver.client.request.EzyPingRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzyPingSchedule {

    private Thread thread;
    private EzySocketDataHandler dataHandler;
    private final EzyClient client;
    private final EzyPingManager pingManager;
    private volatile boolean active = true;

    public EzyPingSchedule(EzyClient client) {
        this.client = client;
        this.pingManager = client.getPingManager();

    }

    public void start() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(active)
                    handle();
            }
        });
        active = true;
        thread.setName("ezyfox-ping-schedule");
        thread.start();
    }

    public void stop() {
        this.active = false;
    }

    private void handle() {
        try {
            long periodMillis = pingManager.getPingPeriod();
            Thread.sleep(periodMillis);
            sendPingRequest();
        } catch (InterruptedException e) {
            Log.e("ezyfox-client", "ping thread has interrupted", e);
        }
    }

    private void sendPingRequest() {
        int lostPingCount = pingManager.increaseLostPingCount();
        int maxLostPingCount = pingManager.getMaxLostPingCount();
        if(lostPingCount >= maxLostPingCount) {
            dataHandler.fireSocketDisconnected(EzyDisconnectReason.SERVER_NOT_RESPONDING.getId());
        }
        else {
            EzyRequest request = new EzyPingRequest();
            client.send(request);
        }
        if(lostPingCount > 1) {
            Log.i("ezyfox-client", "lost ping count: " + lostPingCount);
            EzyLostPingEvent event = new EzyLostPingEvent(lostPingCount);
            EzySocketEvent socketEvent = new EzySimpleSocketEvent(EzySocketEventType.EVENT, event);
            dataHandler.fireSocketEvent(socketEvent);
        }
    }

    public void setDataHandler(EzySocketDataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }
}
