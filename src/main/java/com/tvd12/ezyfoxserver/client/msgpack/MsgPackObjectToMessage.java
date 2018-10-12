package com.tvd12.ezyfoxserver.client.msgpack;

import com.tvd12.ezyfoxserver.client.codec.EzyMessage;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageHeader;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageSerializer;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToBytes;
import com.tvd12.ezyfoxserver.client.codec.EzyObjectToMessage;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleMessage;
import com.tvd12.ezyfoxserver.client.codec.EzySimpleMessageHeader;

public class MsgPackObjectToMessage implements EzyObjectToMessage {

	private final EzyObjectToBytes objectToBytes;
	
	public MsgPackObjectToMessage() {
		this.objectToBytes = new MsgPackObjectToBytes(newSerializer());
	}

	private EzyMessageSerializer newSerializer() {
		return new MsgPackSimpleSerializer();
	}
	
	@Override
	public EzyMessage convert(Object object) {
		EzyMessage message = convert(convertObject(object));
		return message;
	}
	
	private byte[] convertObject(Object object) {
		byte[] bytes = objectToBytes.convert(object);
		return bytes;
	}
	
	private EzyMessage convert(byte[] content) {
		EzyMessage message = new EzySimpleMessage(newHeader(content), content, content.length);
		return message;
	}
	
	private EzyMessageHeader newHeader(byte[] content) {
		EzyMessageHeader header = new EzySimpleMessageHeader(isBigMessage(content), false, false, false);
		return header;
	}
	
	private boolean isBigMessage(byte[] content) {
		boolean answer = content.length > MsgPackConstant.MAX_SMALL_MESSAGE_SIZE;
		return answer;
	}
	
}
