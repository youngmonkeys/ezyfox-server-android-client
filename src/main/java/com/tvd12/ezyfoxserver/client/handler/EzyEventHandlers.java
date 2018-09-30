package com.tvd12.ezyfoxserver.client.handler;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzyEventHandlers {

    private final Map<EzyConstant, EzyEventHandler> handlers = new HashMap<>();

    public void addHandler(EzyConstant eventType, EzyEventHandler handler) {
        this.handlers.put(eventType, handler);
    }

    public EzyEventHandler getHandler(EzyConstant eventType) {
        if(handlers.containsKey(eventType)) {
            EzyEventHandler handler = handlers.get(eventType);
            return handler;
        }
        return  null;
    }

}
