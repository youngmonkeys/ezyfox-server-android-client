package com.tvd12.ezyfoxserver.client.socket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class EzyLinkedBlockingEventQueue extends EzyBlockingSocketEventQueue {

    @Override
    protected BlockingQueue<EzySocketEvent> newQueue(int capacity) {
        return new LinkedBlockingQueue<>(capacity);
    }

}
