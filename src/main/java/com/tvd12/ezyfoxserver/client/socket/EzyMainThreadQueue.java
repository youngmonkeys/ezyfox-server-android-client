package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandler;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandler;
import com.tvd12.ezyfoxserver.client.util.EzyLogger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Dung Ta Van on 8/3/19.
 * Copyright © 2019 Young Monkeys. All rights reserved.
 **/
public class EzyMainThreadQueue {

    private final Queue<EzyDataHandlerExecutor> dataExecutors;
    private final Queue<EzyEventHandlerExecutor> eventExecutors;

    public EzyMainThreadQueue() {
        this.eventExecutors = new LinkedList<>();
        this.dataExecutors = new LinkedList<>();
    }

    public void add(EzyEvent evt, EzyEventHandler handler) {
        synchronized (eventExecutors) {
            eventExecutors.add(new EzyEventHandlerExecutor(evt, handler));
        }
    }

    public void add(EzyArray data, EzyDataHandler handler) {
        synchronized (dataExecutors) {
            dataExecutors.add(new EzyDataHandlerExecutor(data, handler));
        }
    }

    public void polls() {
        List<EzyEventHandlerExecutor> eventExecutors = dequeueEventHandlers();
        for (EzyEventHandlerExecutor executor : eventExecutors)
            executor.execute();
        List<EzyDataHandlerExecutor> dataExecutors = dequeueDataHandlers();
        for (EzyDataHandlerExecutor executor : dataExecutors)
            executor.execute();
    }

    private List<EzyEventHandlerExecutor> dequeueEventHandlers() {
        List<EzyEventHandlerExecutor> list = new ArrayList<>();
        synchronized (eventExecutors) {
            while (!eventExecutors.isEmpty()) {
                list.add(eventExecutors.poll());
            }
        }
        return list;
    }

    private List<EzyDataHandlerExecutor> dequeueDataHandlers() {
        List<EzyDataHandlerExecutor> list = new ArrayList<>();
        synchronized (dataExecutors) {
            while (!dataExecutors.isEmpty()) {
                list.add(dataExecutors.poll());
            }
        }
        return list;
    }
}

class EzyEventHandlerExecutor {

    private final EzyEvent evt;
    private final EzyEventHandler handler;

    public EzyEventHandlerExecutor(EzyEvent evt, EzyEventHandler handler) {
        this.evt = evt;
        this.handler = handler;
    }

    public void execute() {
        try {
            handler.handle(evt);
        } catch (Exception ex) {
            EzyLogger.error("handle event: " + evt + " error", ex);
        }
    }
}

class EzyDataHandlerExecutor {

    private final EzyArray data;
    private final EzyDataHandler handler;

    public EzyDataHandlerExecutor(EzyArray data, EzyDataHandler handler) {
        this.data = data;
        this.handler = handler;
    }

    public void execute() {
        try {
            handler.handle(data);
        } catch (Exception ex) {
            EzyLogger.error("handle data: " + data + " error", ex);
        }
    }
}