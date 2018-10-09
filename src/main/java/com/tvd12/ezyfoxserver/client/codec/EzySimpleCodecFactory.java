package com.tvd12.ezyfoxserver.client.codec;

import com.tvd12.ezyfoxserver.client.constant.EzyConstant;
import com.tvd12.ezyfoxserver.client.msgpack.MsgPackCodecCreator;

public class EzySimpleCodecFactory implements EzyCodecFactory {

	private final EzyCodecCreator socketCodecCreator;
	
	public EzySimpleCodecFactory() {
		this.socketCodecCreator = newSocketCodecCreator();
	}
	
	@Override
    public Object newEncoder(EzyConstant type) {
        Object encoder = socketCodecCreator.newEncoder();
        return encoder;
    }
	
	@Override
	public Object newDecoder(EzyConstant type) {
		Object decoder = socketCodecCreator.newDecoder(Integer.MAX_VALUE);
		return decoder;
	}
	
	private EzyCodecCreator newSocketCodecCreator() {
		EzyCodecCreator answer = new MsgPackCodecCreator();
		return answer;
	}
	
}
