package com.tvd12.ezyfoxserver.client.socket;

/**
 * Created by tavandung12 on 9/20/18.
 */

public abstract class EzyAbstractSocketClient implements EzySocketClient {

    protected abstract boolean connect0();

    protected abstract void resetComponents();

}
