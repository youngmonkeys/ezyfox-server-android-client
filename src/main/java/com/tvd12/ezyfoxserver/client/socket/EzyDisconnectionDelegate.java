package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

/**
 * Created by tavandung12 on 10/5/18.
 */

public interface EzyDisconnectionDelegate {

    void onDisconnected(EzyConstant reason);

}
