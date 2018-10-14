package com.tvd12.ezyfoxserver.client.socket;

public interface EzySocketEventQueue {

    int size();
    
	void clear();
	
	boolean isFull();
	
	boolean isEmpty();
	
	boolean add(EzySocketEvent event);
	
	void remove(EzySocketEvent event);

	EzySocketEvent take() throws InterruptedException;
	
}
