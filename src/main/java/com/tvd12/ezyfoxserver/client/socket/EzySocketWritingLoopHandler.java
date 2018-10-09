package com.tvd12.ezyfoxserver.client.socket;

public class EzySocketWritingLoopHandler extends EzySocketEventLoopOneHandler {

	@Override
	protected String getThreadName() {
		return "socket-writer";
	}
	
}
