/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.executor;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.log.Logger;



/**
 * A factory for creating UltaThread objects.
 *
 * @author viva
 */
public class UltaThreadFactory {
	
	/** The parameters for executing the web services. */
	private InvokerParams<UltaBean> invokerParameters = null;
	
	/** The parameters for executing the web services. */
	private  Class<UltaBean> ultaBeanClazz = null;
	
	/**
	 * Instantiates a new ulta thread factory.
	 *
	 * @param invokerParams the invoker params
	 */
	public UltaThreadFactory(InvokerParams<UltaBean> invokerParams){
		Logger.Log("<UltaThreadFactory><UltaThreadFactory><invokerParams>>"+(invokerParams));
		Logger.Log("<UltaThreadFactory><UltaThreadFactory><Class<UltaBean>>>"+(invokerParams.getUltaBeanClazz()));
		this.invokerParameters = invokerParams;
		this.ultaBeanClazz = invokerParams.getUltaBeanClazz();
	}
	
	/**
	 * New executor thread.
	 *
	 * @return the ulta executor thread
	 */
	public UltaExecutorThread newExecutorThread() {
		Logger.Log("<UltaThreadFactory><newExecutorThread>");
		UltaExecutorThread ultaExecutorThread = new UltaExecutorThread(invokerParameters, ultaBeanClazz);
		return ultaExecutorThread;
	}

}
