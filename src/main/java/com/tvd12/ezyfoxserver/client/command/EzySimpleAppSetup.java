package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;

/**
 * Created by tavandung12 on 10/3/18.
 */

public class EzySimpleAppSetup implements EzyAppSetup {

    private final EzyAppDataHandlers dataHandlers;
    private final EzyZoneSetup parent;

    public EzySimpleAppSetup(EzyAppDataHandlers dataHandlers, EzyZoneSetup parent) {
        this.parent = parent;
        this.dataHandlers = dataHandlers;
    }

    @Override
    public EzyAppSetup addDataHandler(Object cmd, EzyAppDataHandler dataHandler) {
        dataHandlers.addHandler(cmd, dataHandler);
        return this;
    }

    @Override
    public EzyZoneSetup done() {
        return parent;
    }
}
