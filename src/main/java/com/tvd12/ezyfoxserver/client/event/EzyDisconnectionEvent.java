package com.tvd12.ezyfoxserver.client.event;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

/**
 * Created by tavandung12 on 10/5/18.
 */

public class EzyDisconnectionEvent implements EzyEvent {

    private final EzyConstant reason;

    public EzyDisconnectionEvent(EzyConstant reason) {
        this.reason = reason;
    }

    public EzyConstant getReason() {
        return reason;
    }

    @Override
    public EzyEventType getType() {
        return EzyEventType.DISCONNECTION;
    }
}
