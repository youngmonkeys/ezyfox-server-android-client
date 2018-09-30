package com.tvd12.ezyfoxserver.client.socket;

import android.os.Handler;
import android.util.Log;

import com.tvd12.ezyfoxserver.client.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionSuccessEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * Created by tavandung12 on 9/20/18.
 */

public class EzyTcpSocketClient
        extends EzyAbstractSocketClient
        implements EzyReconnectable {

    protected SocketChannel socketChannel;
    protected SocketAddress socketAddress;
    protected EzySocketThread socketThread;
    protected final Handler uihandler;
    protected final EzyDataHandlers dataHandlers;
    protected final EzyEventHandlers eventHandlers;
    protected final EzyCodecFactory codecFactory;
    protected final EzyPacketQueue packetQueue;
    protected final EzySocketEventQueue eventQueue;
    protected final EzyResponseApi responseApi;
    protected final EzySocketDataHandler dataHandler;
    protected final EzySocketReader socketReader;
    protected final EzySocketWriter socketWriter;
    protected final EzySocketDataEventHandler socketDataEventHandler;
    protected final EzySocketReadingLoopHandler socketReadingLoopHandler;
    protected final EzySocketWritingLoopHandler socketWritingLoopHandler;

    public EzyTcpSocketClient(EzyEventHandlers eventHandlers,
                              EzyDataHandlers dataHandlers) {
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
        this.uihandler = new Handler();
        this.codecFactory = new EzySimpleCodecFactory();
        this.packetQueue = new EzyBlockingPacketQueue();
        this.eventQueue = new EzyLinkedBlockingEventQueue();
        this.responseApi = newResponseApi();
        this.dataHandler = newSocketDataHandler();
        this.socketReader = new EzySocketReader(dataHandler);
        this.socketWriter = new EzySocketWriter(packetQueue, dataHandler);
        this.socketDataEventHandler = newSocketDataEventHandler();
        this.socketReadingLoopHandler = newSocketReadingLoopHandler();
        this.socketWritingLoopHandler = newSocketWritingLoopHandler();
    }

    private EzyResponseApi newResponseApi() {
        Object encoder = codecFactory.newEncoder(EzyConnectionType.SOCKET);
        EzySocketDataEncoder socketDataEncoder = new EzySimpleSocketDataEncoder(encoder);
        EzyResponseApi api = new EzySocketResponseApi(socketDataEncoder, packetQueue);
        return api;
    }

    private EzySocketDataHandler newSocketDataHandler() {
        Object decoder = codecFactory.newDecoder(EzyConnectionType.SOCKET);
        EzySocketDataDecoder socketDataDecoder = new EzySimpleSocketDataDecoder(decoder);
        return new EzySocketDataHandler(socketDataDecoder, eventQueue, socketChannel);
    }

    private EzySocketDataEventHandler newSocketDataEventHandler() {
        return new EzySocketDataEventHandler(uihandler, eventHandlers, dataHandlers, eventQueue);
    }

    private EzySocketReadingLoopHandler newSocketReadingLoopHandler() {
        EzySocketReadingLoopHandler handler = new EzySocketReadingLoopHandler();
        handler.setEventHandler(socketReader);
        return handler;
    }

    private EzySocketWritingLoopHandler newSocketWritingLoopHandler() {
        EzySocketWritingLoopHandler handler = new EzySocketWritingLoopHandler();
        handler.setEventHandler(socketWriter);
        return handler;
    }

    @Override
    public void connect(String host, int port) throws Exception {
        socketAddress = new InetSocketAddress(host, port);
        socketReadingLoopHandler.start();
        socketWritingLoopHandler.start();
        socketThread = new EzySocketThread();
        socketThread.start();
    }

    @Override
    public void reconnect() throws Exception {
        socketThread.cancel();
        socketThread = new EzySocketThread();
        socketThread.start();
        super.reconnect();
    }

    @Override
    protected void connect0() {
        EzyEvent event = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            socketChannel.configureBlocking(false);
            socketReader.setSocketChannel(socketChannel);
            event = new EzyConnectionSuccessEvent();
        }
        catch (UnknownHostException e) {
            event = EzyConnectionFailureEvent.unknownHost();
        }
        catch (ConnectException e) {
            event = EzyConnectionFailureEvent.connectionRefused();
        }
        catch (Exception e) {
            event = EzyConnectionFailureEvent.unknown();
        }
        EzySocketEvent socketEvent = new EzySimpleSocketEvent(EzySocketEventType.EVENT, event);
        eventQueue.add(socketEvent);
    }

    @Override
    public void send(Object cmd, Object data) {
        EzyArray array = EzyEntityFactory.newArrayBuilder()
                .append(((EzyCommand)cmd).getId())
                .append(data)
                .build();
        EzyPackage pack = new EzySimplePackage(array);
        try {
            responseApi.response(pack);
        } catch (Exception e) {
            Log.e("ezyfox-client", "send cmd: " + cmd + " with data: " + data + " error", e);
        }
    }

    @Override
    public void disconnect() {
        if(socketChannel != null)
            disconnect0();
    }

    private void disconnect0() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            Log.e("ezyfox-client","close socket error", e);
        }
    }

    @Override
    public void close() {
        disconnect();
    }

    @Override
    protected void resetComponents() {
        packetQueue.clear();
        eventQueue.clear();
        dataHandler.reset();
        socketReader.reset();
        socketWriter.reset();
        socketReadingLoopHandler.reset();
        socketWritingLoopHandler.reset();
    }

    private class EzySocketThread {

        private final Thread thread;
        private EzySocketDataEventHandlingLoop socketDataEventHandlingLoop;

        public EzySocketThread() {
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        connect0();
                        socketDataEventHandlingLoop = new EzySocketDataEventHandlingLoop(socketDataEventHandler);
                        socketDataEventHandlingLoop.start();
                    }
                    catch (Exception e) {
                        Log.e("ezyfox-client", "start data event handling loop error", e);
                    }
                }
            });
        }

        public void start() {
            thread.start();
        }

        public void cancel() {
            socketDataEventHandlingLoop.setActive(false);
        }
    }
}
