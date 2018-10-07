package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzyPingRequest implements EzyRequest {

    private static final EzyArray EMPTY_DATA = EzyEntityFactory.newArray();

    @Override
    public EzyData serialize() {
        return EMPTY_DATA;
    }

    @Override
    public Object getCommand() {
        return EzyCommand.PING;
    }
}
