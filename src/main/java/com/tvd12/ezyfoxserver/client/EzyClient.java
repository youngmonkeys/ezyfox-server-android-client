package com.tvd12.ezyfoxserver.client;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.command.EzySetup;
import com.tvd12.ezyfoxserver.client.command.EzySimpleSetup;
import com.tvd12.ezyfoxserver.client.entity.EzyEntity;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;
import com.tvd12.ezyfoxserver.client.socket.EzySocketClient;
import com.tvd12.ezyfoxserver.client.socket.EzyTcpSocketClient;

/**
 * Created by tavandung12 on 9/20/18.
 */

public class EzyClient extends EzyEntity {

    private final EzyEventHandlers eventHandlers;
    private final EzyDataHandlers dataHandlers;
    private final EzySocketClient socketClient;

    public EzyClient() {
        this.eventHandlers = newEventHandlers();
        this.dataHandlers = newDataHandlers();
        this.socketClient = newSocketClient();
        this.initProperties();
    }

    private void initProperties() {
        this.properties.put(EzySetup.class, new EzySimpleSetup(eventHandlers, dataHandlers));
    }

    private EzyEventHandlers newEventHandlers() {
        EzyEventHandlers handlers = new EzyEventHandlers();
        return handlers;
    }

    private EzyDataHandlers newDataHandlers() {
        EzyDataHandlers handlers = new EzyDataHandlers();
        return handlers;
    }

    private EzySocketClient newSocketClient() {
        EzyTcpSocketClient client = new EzyTcpSocketClient(eventHandlers, dataHandlers);
        return client;
    }

    public void connect(String host, int port) {
        try {
            socketClient.connect(host, port);
        } catch (Exception e) {
            Log.e("ezyfox-client", "connect to server error", e);
        }
    }

    public <T> T get(Class<T> key) {
        return getProperty(key);
    }

}
