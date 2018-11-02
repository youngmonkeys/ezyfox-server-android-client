package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/2/18.
 */

public final class EzyClients {

    private String defaultClientName;
    private final Map<String, EzyClient> clients;
    private static final EzyClients INSTANCE = new EzyClients();

    private EzyClients() {
        this.clients = new ConcurrentHashMap<>();
    }

    public static EzyClients getInstance() {
        return INSTANCE;
    }

    public EzyClient newClient(EzyClientConfig config) {
        EzyClient client = new EzyTcpClient(config);
        addClient(client);
        if(defaultClientName == null)
            defaultClientName = client.getName();
        return client;
    }

    public EzyClient newDefaultClient(EzyClientConfig config) {
        EzyClient client = newClient(config);
        defaultClientName = client.getName();
        return client;
    }

    public void addClient(EzyClient client) {
        this.clients.put(client.getName(), client);
    }

    public EzyClient getClient(String name) {
        if(name == null)
            throw new NullPointerException("can not get client with name: null");
        if(clients.containsKey(name))
            return clients.get(name);
        return null;
    }

    public EzyClient getDefaultClient() {
        if(defaultClientName == null)
            return null;
        EzyClient client = getClient(defaultClientName);
        return client;
    }

}
