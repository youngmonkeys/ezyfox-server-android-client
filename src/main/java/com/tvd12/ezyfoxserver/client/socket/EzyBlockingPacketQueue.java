package com.tvd12.ezyfoxserver.client.socket;

import java.util.LinkedList;
import java.util.Queue;

public class EzyBlockingPacketQueue implements EzyPacketQueue {

	private final int capacity;
	private final Queue<EzyPacket> queue;
	private volatile boolean empty = true;
	private volatile boolean processing = false;

	public EzyBlockingPacketQueue() {
		this(10000);
	}

	public EzyBlockingPacketQueue(int capacity) {
		this.capacity = capacity;
		this.queue = new LinkedList<>();
	}

	@Override
	public int size() {
		synchronized (queue) {
			int size = queue.size();
			return size;
		}
	}

	@Override
	public void clear() {
		synchronized (queue) {
			empty = true;
			queue.clear();
		}
	}

	@Override
	public synchronized EzyPacket take() {
		synchronized (queue) {
			EzyPacket packet = queue.poll();
			processing = false;
			empty = queue.isEmpty();
			notifyAll();
			return packet;
		}
	}

	@Override
	public synchronized EzyPacket peek() throws InterruptedException {
		while(empty || processing)
			wait();
		synchronized (queue) {
			processing = true;
			EzyPacket packet = queue.peek();
			return packet;
		}

	}

	@Override
	public boolean isFull() {
		synchronized (queue) {
			int size = queue.size();
			boolean full = size >= capacity;
			return full;
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (queue) {
			boolean empty = queue.isEmpty();
			return empty;
		}
	}

	@Override
	public synchronized boolean add(EzyPacket packet) {
		synchronized (queue) {
			int size = queue.size();
			if(size >= capacity)
				return false;
			boolean success = queue.offer(packet);
			if(success) {
				empty = false;
				if(!processing)
					notifyAll();
			}
			return success;
		}
	}

	@Override
	public void wakeup() {
		this.processing = false;
	}
}
