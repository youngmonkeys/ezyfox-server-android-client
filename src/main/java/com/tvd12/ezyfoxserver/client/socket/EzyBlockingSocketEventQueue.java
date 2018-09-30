package com.tvd12.ezyfoxserver.client.socket;

import java.util.concurrent.BlockingQueue;

public abstract class EzyBlockingSocketEventQueue implements EzySocketEventQueue {

    private final int capacity;
	private final BlockingQueue<EzySocketEvent> queue;
	
	public EzyBlockingSocketEventQueue() {
	    this(10000);
	}
	
	public EzyBlockingSocketEventQueue(int capacity) {
	    this.capacity = capacity;
        this.queue = newQueue(capacity);
	}
	
	protected abstract BlockingQueue<EzySocketEvent> newQueue(int capacity);

	@Override
	public int size() {
	    return queue.size();
	}
	
	@Override
	public void clear() {
		queue.clear();
	}
	
	@Override
	public boolean isFull() {
	    return queue.size() >= capacity;
	}
	
	@Override
	public boolean isEmpty() {
	    return queue.isEmpty();
	}
	
	@Override
	public boolean add(EzySocketEvent event) {
	    if(queue.size() >= capacity)
	        return false;
		return queue.offer(event);
	}
	
	@Override
	public void remove(EzySocketEvent event) {
	    queue.remove(event);
	}
	
    @Override
	public EzySocketEvent take() throws InterruptedException {
		EzySocketEvent event = queue.take();
		return event;
	}
	
}
