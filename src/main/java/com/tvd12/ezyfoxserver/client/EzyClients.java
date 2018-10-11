package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/2/18.
 */

public final class EzyClients {

    private String defaultClientName;
    private final Map<Object, EzyClient> clients = new ConcurrentHashMap<>();
    private static final EzyClients INSTANCE = new EzyClients();

    private EzyClients() {
    }

    public static EzyClients getInstance() {
        return INSTANCE;
    }

    public EzyClient newClient(EzyClientConfig config) {
        EzyClient client = new EzyTcpClient(config);
        addClient(client);
        if(defaultClientName == null)
            defaultClientName = client.getZoneName();
        return client;
    }

    public EzyClient newDefaultClient(EzyClientConfig config) {
        EzyClient client = newClient(config);
        defaultClientName = config.getZoneName();
        return client;
    }

    public void addClient(EzyClient client) {
        this.clients.put(client.getZoneName(), client);
    }

    public EzyClient getClient(Object name) {
        if(clients.containsKey(name))
            return clients.get(name);
        throw new IllegalArgumentException("has no client with name: " + name);
    }

    public EzyClient getDefaultClient() {
        EzyClient client = getClient(defaultClientName);
        return client;
    }

}
