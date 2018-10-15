package com.tvd12.ezyfoxserver.client.socket;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySocketDataEventLoopHandler extends EzySocketEventLoopOneHandler {

    @Override
    protected String getThreadName() {
        return "data-event-handler";
    }
}
