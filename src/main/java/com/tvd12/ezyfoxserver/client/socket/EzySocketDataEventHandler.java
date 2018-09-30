package com.tvd12.ezyfoxserver.client.socket;

import android.os.Handler;
import android.util.Log;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySocketDataEventHandler extends EzyAbstractSocketEventHandler {

    private final Handler uihandler;
    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final EzySocketEventQueue socketEventQueue;

    public EzySocketDataEventHandler(Handler uihandler,
                                     EzyEventHandlers eventHandlers,
                                     EzyDataHandlers dataHandlers,
                                     EzySocketEventQueue socketEventQueue) {
        this.uihandler = uihandler;
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
        this.socketEventQueue = socketEventQueue;
    }

    @Override
    public void handleEvent() {
        try {
            EzySocketEvent event = socketEventQueue.take();
            EzyConstant eventType = event.getType();
            Object eventData = event.getData();
            if(eventType == EzySocketEventType.EVENT)
                processEvent((EzyEvent)eventData);
            else
                processResponse((EzyResponse)eventData);
        } catch (InterruptedException e) {
            Log.e("ezyfox-client", "can't take socket response", e);
        }
    }

    private void processEvent(final EzyEvent event) {
        EzyEventType eventType = event.getType();
        final EzyEventHandler handler = eventHandlers.getHandler(eventType);
        if(handler != null) {
            uihandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.handle(event);
                }
            });
        }
        else {
            Log.w("ezyfox-client", "has no handler with event: " + eventType);
        }
    }

    private void processResponse(EzyResponse response) {
        EzyConstant cmd = response.getCommand();
        EzyArray data = response.getData();
        final EzyDataHandler handler = dataHandlers.getDataHandler(cmd);
        if(handler != null) {
            final EzyArray realData = data.getWithDefault(1, null);
            uihandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.handle(realData);
                }
            });
        }
        else {
            Log.w("ezyfox-client","has no handler with command: " + cmd);
        }
    }

    @Override
    public void reset() {
    }

    @Override
    public void destroy() {
    }

}
