package com.tvd12.ezyfoxserver.client.msgpack;

import com.tvd12.ezyfoxserver.client.codec.EzyByteToObjectDecoder;
import com.tvd12.ezyfoxserver.client.codec.EzyMessage;
import com.tvd12.ezyfoxserver.client.codec.EzyMessageDeserializer;
import com.tvd12.ezyfoxserver.client.codec.EzyDefaultDecodeHandlers;

import java.nio.ByteBuffer;
import java.util.Queue;

/**
 * Created by tavandung12 on 9/25/18.
 */
public class MsgPackByteToObjectDecoder implements EzyByteToObjectDecoder {

	protected final EzyDefaultDecodeHandlers handlers;
	protected final EzyMessageDeserializer deserializer;

	public MsgPackByteToObjectDecoder(
			EzyMessageDeserializer deserializer, int maxSize) {
		this.deserializer = deserializer;
		this.handlers = EzyDefaultDecodeHandlers.builder()
				.maxSize(maxSize)
				.build();
	}

	@Override
	public Object decode(EzyMessage message) throws Exception {
		Object answer = deserializer.deserialize(message.getContent());
		return answer;
	}

	@Override
	public void decode(ByteBuffer bytes, Queue<EzyMessage> out) throws Exception {
		handlers.handle(bytes, out);
	}

	@Override
	public void reset() {
		handlers.reset();
	}

}
