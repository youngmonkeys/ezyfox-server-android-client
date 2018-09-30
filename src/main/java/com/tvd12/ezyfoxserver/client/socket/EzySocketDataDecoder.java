package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.callback.EzyCallback;
import com.tvd12.ezyfoxserver.client.codec.EzyMessage;
import com.tvd12.ezyfoxserver.client.util.EzyResettable;

/**
 * Created by tavandung12 on 9/21/18.
 */

public interface EzySocketDataDecoder extends EzyResettable {

    Object decode(EzyMessage message) throws Exception;

    void decode(byte[] bytes, EzyCallback<EzyMessage> callback) throws Exception;

}
