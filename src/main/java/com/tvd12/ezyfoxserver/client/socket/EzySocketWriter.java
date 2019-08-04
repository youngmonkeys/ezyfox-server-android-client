package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.logger.EzyLogger;

import java.nio.ByteBuffer;

public class EzySocketWriter extends EzyAbstractSocketEventHandler {

	protected final ByteBuffer writeBuffer;
    protected final EzyPacketQueue packetQueue;
    protected final EzySocketDataHandler dataHandler;

	public EzySocketWriter(EzyPacketQueue packetQueue,
						   EzySocketDataHandler dataHandler) {
		this.packetQueue = packetQueue;
		this.dataHandler = dataHandler;
		this.writeBuffer = ByteBuffer.allocate(32768);
	}

    @Override
    public void handleEvent() {
		try {
            EzyPacket packet = packetQueue.peek();
            dataHandler.firePacketSend(packet, writeBuffer);
            if(packet.isReleased())
                packetQueue.take();
            else
            	packetQueue.wakeup();
		}
		catch (InterruptedException e) {
			EzyLogger.warn("socket-writer thread interrupted: " + Thread.currentThread());
		}
		catch(Throwable throwable) {
			EzyLogger.warn("problems in socket-writer main loop, thread: " + Thread.currentThread(), throwable);
		}
	}
	
	@Override
	public void destroy() {
		packetQueue.clear();
	}

    @Override
    public void reset() {
        writeBuffer.clear();
    }
	
}
