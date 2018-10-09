package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.socket.EzyPingSchedule;
import com.tvd12.ezyfoxserver.client.socket.EzyPingScheduleAware;

/**
 * Created by tavandung12 on 10/1/18.
 */

public abstract class EzyHandshakeHandler
        extends EzyAbstractDataHandler
        implements EzyPingScheduleAware {

    protected EzyPingSchedule pingSchedule;

    @Override
    public void setPingSchedule(EzyPingSchedule pingSchedule) {
        this.pingSchedule = pingSchedule;
    }
}
