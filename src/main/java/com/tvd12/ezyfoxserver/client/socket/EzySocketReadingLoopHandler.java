package com.tvd12.ezyfoxserver.client.socket;

public class EzySocketReadingLoopHandler extends EzySocketEventLoopOneHandler {
	
	@Override
	protected String getThreadName() {
		return "socket-reader";
	}
	
}
