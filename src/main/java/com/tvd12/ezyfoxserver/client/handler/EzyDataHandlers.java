package com.tvd12.ezyfoxserver.client.handler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tavandung12 on 9/30/18.
 */

public class EzyDataHandlers {

    private final Map<Object, EzyDataHandler> handlers = new HashMap<>();

    public void addHandler(Object cmd, EzyDataHandler handler) {
        this.handlers.put(cmd, handler);
    }

    public EzyDataHandler getDataHandler(Object cmd) {
        if(handlers.containsKey(cmd)) {
            EzyDataHandler handler = handlers.get(cmd);
            return handler;
        }
        return null;
    }

}
