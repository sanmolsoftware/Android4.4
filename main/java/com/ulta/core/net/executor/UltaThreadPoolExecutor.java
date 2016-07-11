/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.executor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/**
 * The Class UltaThreadPoolExecutor.
 *
 * @author viva
 */
public class UltaThreadPoolExecutor extends ThreadPoolExecutor{

	/**
	 * Instantiates a new ulta thread pool executor.
	 *
	 * @param corePoolSize the core pool size
	 * @param maximumPoolSize the maximum pool size
	 * @param keepAliveTime the keep alive time
	 * @param unit the unit
	 * @param workQueue the work queue
	 * @param threadFactory the thread factory
	 */
	public UltaThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
				threadFactory);
	}

}
