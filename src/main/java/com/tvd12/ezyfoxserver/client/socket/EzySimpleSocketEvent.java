package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzySimpleSocketEvent implements EzySocketEvent {

    private final EzyConstant type;
    private final Object data;

    public EzySimpleSocketEvent(EzyConstant type, Object data) {
        this.type = type;
        this.data = data;
    }

    @Override
    public EzyConstant getType() {
        return type;
    }

    @Override
    public Object getData() {
        return data;
    }
}
