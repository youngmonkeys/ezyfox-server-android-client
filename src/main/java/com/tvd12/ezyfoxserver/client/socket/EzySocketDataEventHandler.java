package com.tvd12.ezyfoxserver.client.socket;

import android.os.Handler;
import android.util.Log;

import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;

import java.util.Set;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySocketDataEventHandler extends EzyAbstractSocketEventHandler {

    private final Handler uihandler;
    private final EzySocketDataHandler dataHandler;
    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final EzySocketEventQueue socketEventQueue;
    private final Set<Object> unloggableCommands;

    public EzySocketDataEventHandler(Handler uihandler,
                                     EzySocketDataHandler dataHandler,
                                     EzyEventHandlers eventHandlers,
                                     EzyDataHandlers dataHandlers,
                                     EzySocketEventQueue socketEventQueue,
                                     Set<Object> unloggableCommands) {
        this.uihandler = uihandler;
        this.dataHandler = dataHandler;
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
        this.socketEventQueue = socketEventQueue;
        this.unloggableCommands = unloggableCommands;
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
            Log.i("ezyfox-client", "process event: " + eventType + " with handler: " + handler);
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
        final EzyArray responseData = data.getWithDefault(1, null);
        if(!unloggableCommands.contains(cmd))
            Log.d("ezyfox-client", "received command: " + cmd + " and data: " + responseData);
        if(cmd == EzyCommand.DISCONNECT)
            handleDisconnection(responseData);
        else
            handleOtherCommand(cmd, responseData);
    }

    private void handleDisconnection(EzyArray responseData) {
        int reasonId = responseData.get(0, int.class, 0);
        EzyConstant disconnectReason = EzyDisconnectReason.valueOf(reasonId);
        dataHandler.fireSocketDisconnected(disconnectReason);
    }

    private void handleOtherCommand(final Object cmd, final EzyArray responseData) {
        final EzyDataHandler handler = dataHandlers.getHandler(cmd);
        if (handler != null) {
            uihandler.post(new Runnable() {
                @Override
                public void run() {
                    handler.handle(responseData);
                }
            });
        } else {
            Log.w("ezyfox-client", "has no handler with command: " + cmd);
        }
    }

}
