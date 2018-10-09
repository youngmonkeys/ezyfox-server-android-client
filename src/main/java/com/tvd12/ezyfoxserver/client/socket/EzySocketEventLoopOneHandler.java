package com.tvd12.ezyfoxserver.client.socket;

public abstract class EzySocketEventLoopOneHandler extends EzySocketEventLoopHandler {

    protected EzySocketEventHandler eventHandler;

	public void setEventHandler(EzySocketEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	@Override
	protected EzySimpleSocketEventLoop newEventLoop() {
	    return new EzySimpleSocketEventLoop() {
            
            @Override
            protected void eventLoop0() {
                while(active) {
                    eventHandler.handleEvent();
                }
            }
        };
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if(eventHandler != null) {
			eventHandler.destroy();
		}
	}
	
}
