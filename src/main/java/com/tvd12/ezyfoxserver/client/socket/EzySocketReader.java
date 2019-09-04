package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.callback.EzyCallback;
import com.tvd12.ezyfoxserver.client.codec.EzyMessage;
import com.tvd12.ezyfoxserver.client.concurrent.EzySynchronizedQueue;
import com.tvd12.ezyfoxserver.client.constant.EzyDisconnectReason;
import com.tvd12.ezyfoxserver.client.constant.EzySocketConstants;
import com.tvd12.ezyfoxserver.client.entity.EzyArray;
import com.tvd12.ezyfoxserver.client.logger.EzyLogger;
import com.tvd12.ezyfoxserver.client.util.EzyQueue;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public abstract class EzySocketReader extends EzySocketAdapter {

	protected ByteBuffer buffer;
	protected EzyQueue<EzyArray> dataQueue;
	protected EzySocketDataDecoder decoder;
	protected final int readBufferSize;
	protected final EzyCallback<EzyMessage> decodeBytesCallback;

	public EzySocketReader() {
		super();
		this.readBufferSize = EzySocketConstants.MAX_READ_BUFFER_SIZE;
		this.decodeBytesCallback = new EzyCallback<EzyMessage>() {
			@Override
			public void call(EzyMessage message) {
				onMesssageReceived(message);
			}
		};
	}

	@Override
	protected void loop() {
		this.dataQueue = new EzySynchronizedQueue<>();
		this.buffer = ByteBuffer.allocateDirect(readBufferSize);
		super.loop();
	}

	@Override
	protected void update() {
		while (true) {
			try {
				Thread.sleep(3);
				if(!active)
					return;
				this.buffer.clear();
				long bytesToRead = readSocketData();
				if(bytesToRead == 0)
					continue;
				else if(bytesToRead < 0)
					return;
				buffer.flip();
				byte[] binary = new byte[buffer.limit()];
				buffer.get(binary);
				decoder.decode(binary, decodeBytesCallback);
			}
			catch (InterruptedException e) {
				EzyLogger.warn("socket reader interrupted", e);
				return;
			}
			catch (Exception e) {
				EzyLogger.warn("I/O error at socket-reader", e);
			}
		}
	}

	protected abstract long readSocketData();

	public void popMessages(List<EzyArray> buffer) {
		dataQueue.pollAll(buffer);
	}

	private void onMesssageReceived(EzyMessage message) {
		try {
			Object data = decoder.decode(message);
			dataQueue.add((EzyArray) data);
		}
		catch (Exception e) {
			EzyLogger.warn("decode error at socket-reader", e);
		}
	}

	public void setDecoder(EzySocketDataDecoder decoder) {
		this.decoder = decoder;
	}

	@Override
	protected String getThreadName() {
		return "ezyfox-socket-reader";
	}
}
