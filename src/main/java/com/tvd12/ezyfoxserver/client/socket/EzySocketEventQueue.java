package com.tvd12.ezyfoxserver.client.socket;

public interface EzySocketEventQueue {

    int size();
    
	void clear();
	
	boolean isFull();
	
	boolean isEmpty();
	
	boolean add(EzySocketEvent request);
	
	void remove(EzySocketEvent request);

	EzySocketEvent take() throws InterruptedException;
	
}
