package com.tvd12.ezyfoxserver.client.msgpack;

import com.tvd12.ezyfoxserver.client.codec.EzyByteToObjectDecoder;
import com.tvd12.ezyfoxserver.client.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageToBytes;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToMessage;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleMessageToBytes;

public class MsgPackCodecCreator implements EzyCodecCreator {

	protected final EzyMessageToBytes messageToBytes
			= new EzySimpleMessageToBytes();
	protected final EzyObjectToMessage objectToMessage
			= new MsgPackObjectToMessage();
	protected final EzyMessageDeserializer deserializer
			= new MsgPackSimpleDeserializer();
	
	@Override
	public EzyByteToObjectDecoder newDecoder(int maxRequestSize) {
		return new MsgPackByteToObjectDecoder(deserializer, maxRequestSize);
	}
	
	@Override
	public EzyObjectToByteEncoder newEncoder() {
		return new MsgPackObjectToByteEncoder(messageToBytes, objectToMessage);
	}

}
