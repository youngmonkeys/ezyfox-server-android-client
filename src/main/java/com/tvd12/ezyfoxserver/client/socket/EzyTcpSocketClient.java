package com.tvd12.ezyfoxserver.client.socket;

import android.os.Handler;
import android.util.Log;

import com.tvd12.ezyfoxserver.client.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleCodecFactory;
import com.tvd12.ezyfoxserver.client.config.EzyReconnectConfig;
import com.tvd12.ezyfoxserver.client.constant.EzyCommand;
import com.tvd12.ezyfoxserver.client.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.entity.EzyData;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionFailureEvent;
import com.tvd12.ezyfoxserver.client.event.EzyConnectionSuccessEvent;
import com.tvd12.ezyfoxserver.client.event.EzyEvent;
import com.tvd12.ezyfoxserver.client.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.client.handler.EzyDataHandlers;
import com.tvd12.ezyfoxserver.client.handler.EzyEventHandlers;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;

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
    protected final Handler uihandler;
    protected final EzyReconnectConfig reconnectConfig;
    protected final EzyDataHandlers dataHandlers;
    protected final EzyEventHandlers eventHandlers;
    protected final Set<Object> unloggableCommands;
    protected final EzyCodecFactory codecFactory;
    protected final EzyPacketQueue packetQueue;
    protected final EzySocketEventQueue eventQueue;
    protected final EzyResponseApi responseApi;
    protected final EzySocketDataHandler dataHandler;
    protected final EzyPingSchedule pingSchedule;
    protected final EzySocketReader socketReader;
    protected final EzySocketWriter socketWriter;
    protected final EzySocketDataEventHandler socketDataEventHandler;
    protected final EzySocketReadingLoopHandler socketReadingLoopHandler;
    protected final EzySocketWritingLoopHandler socketWritingLoopHandler;

    public EzyTcpSocketClient(EzyReconnectConfig reconnectConfig,
                              EzyEventHandlers eventHandlers,
                              EzyDataHandlers dataHandlers,
                              EzyPingSchedule pingSchedule,
                              Set<Object> unloggableCommands) {
        this.reconnectConfig = reconnectConfig;
        this.eventHandlers = eventHandlers;
        this.dataHandlers = dataHandlers;
        this.pingSchedule = pingSchedule;
        this.unloggableCommands = unloggableCommands;
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
        this.pingSchedule.setDataHandler(dataHandler);
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
                uihandler,
                dataHandler,
                eventHandlers,
                dataHandlers, eventQueue, unloggableCommands);
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
        reconnectCount = 0;
        socketAddress = new InetSocketAddress(host, port);
        socketReadingLoopHandler.start();
        socketWritingLoopHandler.start();
        socketThread = new EzySocketThread();
        socketThread.start();
    }

    @Override
    public void connect() {
        reconnectCount = 0;
        socketThread.cancel();
        resetComponents();
        socketThread = new EzySocketThread();
        socketThread.start();
    }

    @Override
    public boolean reconnect() {
        int maxReconnectCount = reconnectConfig.getMaxReconnectCount();
        if(reconnectCount >= maxReconnectCount)
            return false;
        socketThread.cancel();
        resetComponents();
        long reconnectSleepTime = getReconnectSleepTime();
        socketThread = new EzySocketThread(reconnectSleepTime);
        socketThread.start();
        reconnectCount++;
        Log.i("ezyfox-client", "try reconnect to server: " + reconnectCount + ", wating time: " + reconnectSleepTime);
        return true;
    }

    @Override
    protected boolean connect0() {
        Log.i("ezyfox-client", "connecting to server");
        EzyEvent event = null;
        boolean success = false;
        try {
            startConnectTime = System.currentTimeMillis();
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
            socketChannel.configureBlocking(false);
            socketReader.setSocketChannel(socketChannel);
            dataHandler.setSocketChannel(socketChannel);
            event = new EzyConnectionSuccessEvent();
            success = true;
            reconnectCount = 0;
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
            Log.d("ezyfox-client", "send command: " + cmd + " and data: " + data);
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
    public void onDisconnected(EzyConstant reason) {
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
        private EzySocketDataEventHandlingLoop socketDataEventHandlingLoop;

        public EzySocketThread() {
            this(0);
        }

        public EzySocketThread(final long sleepTime) {
            this.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    handleConnect(sleepTime);
                }
            });
        }

        private void handleConnect(long sleepTime) {
            try {
                Log.i("ezyfox-client", "sleeping before connect to server");
                sleepBeforeConnect(sleepTime);
                connect0();
                startDataEventHandlingLoop();
            }
            catch (Exception e) {
                Log.e("ezyfox-client", "start connect to server error", e);
            }
        }

        private void sleepBeforeConnect(long sleepTime) throws InterruptedException {
            if(sleepTime > 0)
                Thread.sleep(sleepTime);
        }

        private void startDataEventHandlingLoop() throws Exception {
            socketDataEventHandlingLoop =
                    new EzySocketDataEventHandlingLoop(socketDataEventHandler);
            socketDataEventHandlingLoop.start();
        }

        public void start() {
            thread.start();
        }

        public void cancel() {
            socketDataEventHandlingLoop.setActive(false);
        }
    }
}
