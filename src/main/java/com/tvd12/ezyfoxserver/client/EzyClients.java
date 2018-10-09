package com.tvd12.ezyfoxserver.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tavandung12 on 10/2/18.
 */

public final class EzyClients {

    private static final EzyClients INSTANCE = new EzyClients();
    private final Map<Object, EzyClient> clients = new ConcurrentHashMap<>();

    private EzyClients() {
    }

    public static EzyClients getInstance() {
        return INSTANCE;
    }

    public EzyClient newClient(String name) {
        EzyClient client = new EzyTcpClient(name);
        addClient(client);
        return client;
    }

    public EzyClient newDefaultClient() {
        EzyClient client = new EzyTcpClient();
        addClient(client);
        return client;
    }

    public void addClient(EzyClient client) {
        this.clients.put(client.getName(), client);
    }

    public EzyClient getClient(Object name) {
        if(clients.containsKey(name))
            return clients.get(name);
        throw new IllegalArgumentException("has no client with name: " + name);
    }

    public EzyClient getDefaultClient() {
        EzyClient client = getClient(EzyClient.DEFAULT_CLIENT_NAME);
        return client;
    }

}
