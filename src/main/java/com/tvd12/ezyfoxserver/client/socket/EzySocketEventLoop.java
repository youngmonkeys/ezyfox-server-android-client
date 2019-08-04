package com.tvd12.ezyfoxserver.client.socket;

import com.tvd12.ezyfoxserver.client.concurrent.EzyThreadList;
import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.client.logger.EzyLogger;
import com.tvd12.ezyfoxserver.client.util.EzyResettable;
import com.tvd12.ezyfoxserver.client.util.EzyStartable;

public abstract class EzySocketEventLoop
		implements EzyStartable, EzyDestroyable, EzyResettable {

	protected EzyThreadList threadPool;
	protected volatile boolean active;
	
	protected abstract String threadName();
	protected abstract int threadPoolSize();
	
	@Override
	public void start() throws Exception {
	    initThreadPool();
		setActive(true);
		startLoopService();
	}
	
	protected void setActive(boolean value) {
		this.active = value;
	}
	
	private void startLoopService() {
		threadPool.execute();
	}
	
	private Runnable newServiceTask() {
		return new Runnable() {
			@Override
			public void run() {
				eventLoop();
			}
		};
	}
	
	protected abstract void eventLoop();
	
	protected void initThreadPool() {
		Runnable task = newServiceTask();
		threadPool = new EzyThreadList(threadPoolSize(), task, threadName());
	}

	@Override
	public void reset() {
		active = true;
	}

	@Override
	public void destroy() {
		try {
			destroy0();
		} catch (Exception e) {
			EzyLogger.error("destroy socket event loop error", e);
		}
	}
	
	protected void destroy0() throws Exception {
		this.active = false;
		threadPool = null;
		EzyLogger.info(getClass().getSimpleName() + " stopped");
	}
	
}
