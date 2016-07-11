/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.executor;

import com.ulta.core.conf.types.ResponseParserFormat;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.log.Logger;



/**
 * The Class ExecutionDelegator.
 *
 * @author viva
 */
public class ExecutionDelegator {
	
	/** The constant HANDLER_NULL. */
	private static final String HANDLER_NULL = "Null value for Handler";
	
	/** The constant RESPONSEBEAN_NULL. */
	private static final String RESPONSEBEAN_NULL = "Null value for Response Bean";
	
	/** The constant HTTPMETHOD_NULL. */
	private static final String HTTPMETHOD_NULL= "Null value for HTTPMethod";
	
	/** The constant HTTPPROTOCOL_NULL. */
	private static final String HTTPPROTOCOL_NULL= "Null value for HTTPProtocol";
	
	/** The constant SERVICETOINVOKE_NULL. */
	private static final String SERVICETOINVOKE_NULL= "Null value for Service To Invoke";
	
	/** The constant URLPARAMETERS_NULL. */
	private static final String URLPARAMETERS_NULL= "Null value for URL Parameters";
	
	/**
	 * Instantiates a new execution delegator.
	 *
	 * @param invokerParams the invoker params
	 * @throws UltaException the ulta exception
	 */
	public ExecutionDelegator(InvokerParams invokerParams) throws UltaException {
		Logger.Log("<ExecutionDelegator><ExecutionDelegator><beanClazz>>"+invokerParams.getUltaBeanClazz());
		executeWebservice(invokerParams);
	}
	
	/**
	 * Execute webservice.
	 *
	 * @param invokerParams the invoker params
	 * @throws UltaException the ulta exception
	 */
	private void executeWebservice(InvokerParams invokerParams) throws UltaException{
		String errorMessage = validateInvokerParameters(invokerParams);
		Logger.Log("<ExecutionDelegator><executeWebservice><errorMessage>>"+errorMessage);
		if( errorMessage != null) {
			throw new UltaException(errorMessage);
		} else {
			UltaExecutorThread ultaExecutorThread = new UltaThreadFactory(invokerParams).newExecutorThread();
			ultaExecutorThread.start();
		}
	}
	
	/**
	 * Validate invoker parameters.
	 *
	 * @param invokerParams the invoker params
	 * @return the string
	 */
	private String validateInvokerParameters(InvokerParams invokerParams){
		String errorMessage = null;
		if(invokerParams.getUltaBeanClazz() == null){
			errorMessage = RESPONSEBEAN_NULL;
		} else if(invokerParams.getUltaHandler() == null){
			errorMessage = HANDLER_NULL;
		} else if(invokerParams.getHttpMethod() == null){
			errorMessage = HTTPMETHOD_NULL;
		} else if(invokerParams.getHttpProtocol() == null){
			errorMessage = HTTPPROTOCOL_NULL;
		} else if(invokerParams.getServiceToInvoke() == null){
			errorMessage = SERVICETOINVOKE_NULL;
		}else if(invokerParams.getUrlParameters() == null){
			errorMessage = URLPARAMETERS_NULL;
		}else if(invokerParams.getResponseParserFormat() == null){
			invokerParams.setResponseParserFormat(ResponseParserFormat.JSON);
		}
		return errorMessage;
	}
}
