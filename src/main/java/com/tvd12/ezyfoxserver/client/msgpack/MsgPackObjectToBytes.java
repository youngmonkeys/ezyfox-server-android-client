package com.tvd12.ezyfoxserver.client.msgpack;

import com.tvd12.ezyfoxserver.client.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToBytes;

public class MsgPackObjectToBytes implements EzyObjectToBytes {

	private final EzyMessageSerializer serializer;
	
	public MsgPackObjectToBytes(EzyMessageSerializer serializer) {
		this.serializer = serializer;
	}
	
	@Override
	public byte[] convert(Object object) {
		byte[] bytes = serializer.serialize(object);
		return bytes;
	}

}
