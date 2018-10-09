package com.tvd12.ezyfoxserver.client.codec;

public interface EzyObjectToByteEncoder {

	byte[] encode(Object msg) throws Exception;
	
}
