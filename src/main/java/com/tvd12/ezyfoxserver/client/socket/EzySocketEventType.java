package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

/**
 * Created by tavandung12 on 9/30/18.
 */

public enum EzySocketEventType implements EzyConstant {

    EVENT(1),
    RESPONSE(2);

    private final int id;

    private EzySocketEventType(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return toString();
    }
}
