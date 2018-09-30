package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.event.EzyEventType;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySimpleSetup implements EzySetup {

    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;

    public EzySimpleSetup(EzyEventHandlers eventHandlers, EzyDataHandlers dataHandlers) {
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
    }

    @Override
    public EzySetup addDataHandler(Object cmd, EzyDataHandler dataHandler) {
        dataHandlers.addHandler(cmd, dataHandler);
        return this;
    }

    @Override
    public EzySetup addEventHandler(EzyEventType eventType, EzyEventHandler eventHandler) {
        eventHandlers.addHandler(eventType, eventHandler);
        return this;
    }
}
