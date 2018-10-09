package com.tvd12.ezyfoxserver.client.socket;

/**
 * Created by tavandung12 on 9/30/18.
 */

public interface EzySocketClient extends EzySender {

    void connect(String host, int port) throws Exception;

    void connect();

    boolean reconnect();

    void disconnect();

}
