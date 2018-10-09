package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

/**
 * Created by tavandung12 on 9/30/18.
 */

public interface EzySocketEvent {

    EzyConstant getType();

    Object getData();

}
