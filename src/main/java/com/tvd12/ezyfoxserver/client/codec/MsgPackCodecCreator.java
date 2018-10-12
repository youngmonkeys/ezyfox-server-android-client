package com.tvd12.ezyfoxserver.client.codec;

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
