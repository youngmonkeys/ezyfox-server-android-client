package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.client.config.EzyClientConfig;
import com.tvd12.ezyfoxserver.client.config.EzyReconnectConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionSuccessEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.event.EzyTryConnectEvent;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.manager.EzyHandlerManager;
import com.tvd12.ezyfoxserver.client.manager.EzyPingManager;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.logger.EzyLogger;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by tavandung12 on 9/20/18.
 */

public class EzyTcpSocketClient
        extends EzyAbstractSocketClient
        implements EzyDisconnectionDelegate {

    protected int reconnectCount;
    protected long startConnectTime;
    protected SocketChannel socketChannel;
    protected SocketAddress socketAddress;
    protected EzySocketThread socketThread;
    protected final EzyReconnectConfig reconnectConfig;
    protected final EzyMainThreadQueue mainThreadQueue;
    protected final EzyHandlerManager handlerManager;
    protected final Set<Object> unloggableCommands;
    protected final EzyCodecFactory codecFactory;
    protected final EzyPacketQueue packetQueue;
    protected final EzySocketEventQueue eventQueue;
    protected final EzyResponseApi responseApi;
    protected final EzySocketDataHandler dataHandler;
    protected final EzyPingSchedule pingSchedule;
    protected final EzyPingManager pingManager;
    protected final EzySocketReader socketReader;
    protected final EzySocketWriter socketWriter;
    protected final EzySocketDataEventHandler socketDataEventHandler;
    protected final EzySocketReadingLoopHandler socketReadingLoopHandler;
    protected final EzySocketWritingLoopHandler socketWritingLoopHandler;
    protected final EzySocketDataEventLoopHandler socketDataEventLoopHandler;

    public EzyTcpSocketClient(EzyClientConfig clientConfig,
                              EzyMainThreadQueue mainThreadQueue,
                              EzyHandlerManager handlerManager,
                              EzyPingManager pingManager,
                              EzyPingSchedule pingSchedule,
                              Set<Object> unloggableCommands) {
        this.reconnectConfig = clientConfig.getReconnect();
        this.handlerManager = handlerManager;
        this.pingManager = pingManager;
        this.pingSchedule = pingSchedule;
        this.unloggableCommands = unloggableCommands;
        this.mainThreadQueue = mainThreadQueue;
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
        this.socketDataEventLoopHandler = newSocketDataEventLoopHandler();
        this.pingSchedule.setDataHandler(dataHandler);
        this.startComponents();
    }

    private void startComponents() {
        try {
            this.socketReadingLoopHandler.start();
            this.socketWritingLoopHandler.start();
            this.socketDataEventLoopHandler.start();
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
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
        return new EzySocketDataHandler(socketDataDecoder, eventQueue, this);
    }

    private EzySocketDataEventHandler newSocketDataEventHandler() {
        return new EzySocketDataEventHandler(
                mainThreadQueue,
                dataHandler,
                pingManager,
                handlerManager,
                eventQueue, unloggableCommands);
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

    private EzySocketDataEventLoopHandler newSocketDataEventLoopHandler() {
        EzySocketDataEventLoopHandler handler = new EzySocketDataEventLoopHandler();
        handler.setEventHandler(socketDataEventHandler);
        return handler;
    }

    @Override
    public void connect(String host, int port) throws Exception {
        socketAddress = new InetSocketAddress(host, port);
        connect();
    }

    @Override
    public void connect() {
        reconnectCount = 0;
        handleConnection(0);
    }

    @Override
    public boolean reconnect() {
        int maxReconnectCount = reconnectConfig.getMaxReconnectCount();
        if(reconnectCount >= maxReconnectCount)
            return false;
        long reconnectSleepTime = getReconnectSleepTime();
        handleConnection(reconnectSleepTime);
        reconnectCount++;
        EzyLogger.info("try reconnect to server: " + reconnectCount + ", wating time: " + reconnectSleepTime);
        EzyEvent tryConnectEvent = new EzyTryConnectEvent(reconnectCount);
        EzySocketEvent tryConnectSocketEvent
                = new EzySimpleSocketEvent(EzySocketEventType.EVENT, tryConnectEvent);
        dataHandler.fireSocketEvent(tryConnectSocketEvent);
        return true;
    }

    private void handleConnection(long sleepTime) {
        if(socketThread != null)
            socketThread.cancel();
        disconnect();
        resetComponents();
        socketThread = new EzySocketThread(sleepTime);
        socketThread.start();
    }

    @Override
    protected boolean connect0() {
        EzyLogger.info("connecting to server");
        EzyEvent event = null;
        boolean success = false;
        try {
            startConnectTime = System.currentTimeMillis();
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            socketChannel.configureBlocking(false);
            socketReader.setSocketChannel(socketChannel);
            dataHandler.setSocketChannel(socketChannel);
            dataHandler.setDisconnected(false);
            event = new EzyConnectionSuccessEvent();
            success = true;
            reconnectCount = 0;
        }
        catch (Exception e) {
            if(e instanceof ConnectException) {
                ConnectException c = (ConnectException)e;
                if("Network is unreachable".equalsIgnoreCase(c.getMessage()))
                    event = EzyConnectionFailureEvent.networkUnreachable();
                else
                    event = EzyConnectionFailureEvent.connectionRefused();
            }
            else if(e instanceof  UnknownHostException) {
                event = EzyConnectionFailureEvent.unknownHost();
            }
            else {
                event = EzyConnectionFailureEvent.unknown();
            }
            EzyLogger.warn("connect to: " + socketAddress + " error", e);
        }
        EzySocketEvent socketEvent = new EzySimpleSocketEvent(EzySocketEventType.EVENT, event);
        dataHandler.fireSocketEvent(socketEvent);
        return success;
    }

    private long getReconnectSleepTime() {
        long now = System.currentTimeMillis();
        long offset = now - startConnectTime;
        long reconnectPeriod = reconnectConfig.getReconnectPeriod();
        long sleepTime = reconnectPeriod - offset;
        return sleepTime;
    }

    @Override
    public void send(EzyRequest request) {
        Object cmd = request.getCommand();
        EzyData data = request.serialize();
        send(cmd, data);
    }

    @Override
    public void send(Object cmd, EzyData data) {
        EzyArray array = EzyEntityFactory.newArrayBuilder()
                .append(((EzyCommand)cmd).getId())
                .append(data)
                .build();
        if(!unloggableCommands.contains(cmd))
            EzyLogger.debug("send command: " + cmd + " and data: " + data);
        EzyPackage pack = new EzySimplePackage(array);
        try {
            responseApi.response(pack);
        } catch (Exception e) {
            EzyLogger.error("send cmd: " + cmd + " with data: " + data + " error", e);
        }
    }

    @Override
    public void disconnect() {
        if(socketChannel != null)
            disconnect0();
        socketChannel = null;
        handleDisconnected();
        dataHandler.setDisconnected(true);
    }

    private void disconnect0() {
        try {
            socketChannel.close();
        } catch (IOException e) {
            EzyLogger.error("close socket error", e);
        }
    }

    @Override
    public void onDisconnected(int reason) {
        handleDisconnected();
    }

    private void handleDisconnected() {
        packetQueue.clear();
        eventQueue.clear();
        pingSchedule.stop();
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
        private volatile boolean cancelled;

        public EzySocketThread() {
            this(0);
        }

        public EzySocketThread(final long sleepTime) {
            this.cancelled = false;
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleConnect(sleepTime);
                }
            });
            thread.setName("socket-connection");
        }

        private void handleConnect(long sleepTime) {
            try {
                EzyLogger.info("sleeping " + sleepTime + "ms before connect to server");
                sleepBeforeConnect(sleepTime);
                if(!cancelled)
                    connect0();
            }
            catch (Exception e) {
                EzyLogger.error("start connect to server error", e);
            }
        }

        private void sleepBeforeConnect(long sleepTime) throws InterruptedException {
            if(sleepTime > 0)
                Thread.sleep(sleepTime);
        }


        public void start() {
            thread.start();
        }

        public void cancel() {
            this.cancelled = true;
        }
    }
}
