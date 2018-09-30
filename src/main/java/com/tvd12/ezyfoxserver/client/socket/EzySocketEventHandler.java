package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.client.util.EzyResettable;

public interface EzySocketEventHandler extends EzyDestroyable, EzyResettable {

    void handleEvent();
}
