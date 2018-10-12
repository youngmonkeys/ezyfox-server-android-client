package com.tvd12.ezyfoxserver.client.command;

import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;

/**
 * Created by tavandung12 on 10/3/18.
 */

public interface EzyAppSetup {

    EzyAppSetup addDataHandler(Object cmd, EzyAppDataHandler dataHandler);

    EzySetup done();

}
