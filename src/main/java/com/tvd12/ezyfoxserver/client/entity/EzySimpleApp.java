package com.tvd12.ezyfoxserver.client.entity;

import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyAppDataHandlers;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;

/**
 * Created by tavandung12 on 10/2/18.
 */

public class EzySimpleApp extends EzyEntity implements EzyApp {

    protected final EzyClient client;
    protected final EzyZone zone;
    protected final int id;
    protected final String name;
    protected final EzyAppDataHandlers dataHandlers;

    public EzySimpleApp(EzyZone zone, int id, String name) {
        this.client = zone.getClient();
        this.zone = zone;
        this.id = id;
        this.name = name;
        this.dataHandlers = client.getHandlerManager().getAppDataHandlers(name);
    }

    @Override
    public void send(EzyRequest request) {
        Object cmd = request.getCommand();
        EzyData data = request.serialize();
        send(cmd, data);
    }

    @Override
    public void send(Object cmd, EzyData data) {
        EzyArrayBuilder commandData = EzyEntityFactory.newArrayBuilder()
                .append(cmd)
                .append(data);
        EzyData requestData = EzyEntityFactory.newArrayBuilder()
                .append(id)
                .append(commandData)
                .build();
        client.send(EzyCommand.APP_REQUEST, requestData);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EzyClient getClient() {
        return client;
    }

    @Override
    public EzyZone getZone() {
        return zone;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        T instance = getProperty(clazz);
        return instance;
    }

    @Override
    public EzyAppDataHandler getDataHandler(Object cmd) {
        EzyAppDataHandler handler = dataHandlers.getHandler(cmd);
        return handler;
    }
}
