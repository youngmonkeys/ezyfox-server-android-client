package com.tvd12.ezyfoxserver.client.socket;

import android.util.Log;

import com.tvd12.ezyfoxserver.client.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.client.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.client.util.EzyResettable;
import com.tvd12.ezyfoxserver.client.util.EzyStartable;

import java.util.List;
import java.util.concurrent.ExecutorService;

public abstract class EzySocketEventLoop
		implements EzyStartable, EzyDestroyable, EzyResettable {

	protected ExecutorService threadPool;
	
	protected volatile boolean active;
	
	public EzySocketEventLoop() {
	}
	
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
		Runnable task = newServiceTask();
		int threadPoolSize = threadPoolSize();
		for(int i = 0 ; i < threadPoolSize ; i++)
			threadPool.execute(task);
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
	    this.threadPool = EzyExecutors.newFixedThreadPool(threadPoolSize(), threadName());
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				threadPool.shutdown();
			}
		}));
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
			Log.e("ezyfox-client", "destroy socket event loop error", e);
		}
	}
	
	protected void destroy0() throws Exception {
		setActive(false);
		if(threadPool != null) {
		    List<Runnable> remainTasks = threadPool.shutdownNow();
		    Log.i("ezyfox-client", getClass().getSimpleName() + " stopped. Never commenced execution task: " + remainTasks.size());
		}
	}
	
}
