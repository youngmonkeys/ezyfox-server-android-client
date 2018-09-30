package com.tvd12.ezyfoxserver.client.msgpack;

import com.tvd12.ezyfoxserver.client.codec.EzyMessage;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageToBytes;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToMessage;

public class MsgPackObjectToByteEncoder implements EzyObjectToByteEncoder {

	protected final EzyMessageToBytes messageToBytes;
	protected final EzyObjectToMessage objectToMessage;
	
	public MsgPackObjectToByteEncoder(
			EzyMessageToBytes messageToBytes,
			EzyObjectToMessage objectToMessage) {
		this.messageToBytes = messageToBytes;
		this.objectToMessage = objectToMessage;
	}
	
	@Override
	public byte[] encode(Object msg) throws Exception {
		return convertObjectToBytes(msg);
	}
	
	protected byte[] convertObjectToBytes(Object object) {
		return convertMessageToBytes(convertObjectToMessage(object));
	}
	
	protected EzyMessage convertObjectToMessage(Object object) {
		return objectToMessage.convert(object);
	}
	
	protected byte[] convertMessageToBytes(EzyMessage message) {
		return messageToBytes.convert(message);
	}
	
}
