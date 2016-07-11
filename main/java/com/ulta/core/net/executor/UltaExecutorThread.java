/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.executor;

import android.os.Message;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.conf.types.ResponseParserFormat;
import com.ulta.core.net.WebserviceExecutor;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.net.parser.json.UltaJSONParserException;
import com.ulta.core.net.parser.json.UltaJsonParser;
import com.ulta.core.net.parser.xml.UltaXmlParserException;
import com.ulta.core.net.parser.xml.UltaXmlParserFactory;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.log.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.ulta.core.net.WebserviceUtility.formatResponseToString;




/**
 * The Class UltaExecutorThread.
 *
 * @author viva
 */
public class UltaExecutorThread implements Runnable {
	
	/**
	 * The Handler that is being used.
	 */
	private UltaHandler threadHandler;
	/**
	 * The Bean that is populated with response that is being used.
	 */
	private Class<UltaBean> responseBean;
	
	/**
	 * Get, Post etc.
	 */
	private HttpMethod httpMethod;
	
	/** http, https. */
	private HttpProtocol httpProtocol;
	
	/** Service to invoker. */
	private String serviceToInvoke;
	
	/** Parameters required. */
	private Map<String, String> urlParameters;
	
	/** Image Data. */
	/*private byte[] imageByteArray;*/
	
	/** additionalRequestInformation. */
	private String additionalRequestInformation;
	/**
	 * The param that will be used to clear the user session.
	 */
/*	private boolean isUserSessionClearingRequired;*/
	/**
	 * The param that will contain all the invoker params
	 */
	private InvokerParams invokerParams;
	/**
	 * The param that will contain the custom parser class name
	 */
/*	private String customParserClassName;*/
	/**
	 * The param that will contain the response format
	 */
	private ResponseParserFormat responseFormat;
	
	/**
	 * Instantiates a new ulta executor thread.
	 *
	 * @param invokerParams the invoker params
	 * @param beanClazz the bean clazz
	 */
	public UltaExecutorThread(InvokerParams<UltaBean> invokerParams, Class<UltaBean> beanClazz){
		Logger.Log("<UltaExecutorThread><UltaExecutorThread><invokerParams>>"+(invokerParams));
		Logger.Log("<UltaExecutorThread><UltaExecutorThread><beanClazz>>"+(beanClazz));
		this.invokerParams = invokerParams;
		this.responseBean = beanClazz;
		this.threadHandler = invokerParams.getUltaHandler();
		this.httpMethod = invokerParams.getHttpMethod();
		this.httpProtocol = invokerParams.getHttpProtocol();
		this.serviceToInvoke = invokerParams.getServiceToInvoke();
		/*this.imageByteArray = invokerParams.getImageByteArray();
		this.isUserSessionClearingRequired = invokerParams.isUserSessionClearingRequired();*/
		this.additionalRequestInformation = invokerParams.getAdditionalRequestInformation();
		this.responseFormat = invokerParams.getResponseParserFormat();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		final InputStream responseStream;
		Logger.Log("<UltaExecutorThread><run><httpProtocol>>"+(httpProtocol));
		Logger.Log("<UltaExecutorThread><run><httpMethod>>"+(httpMethod));
		Logger.Log("<UltaExecutorThread><run><serviceToInvoke>>"+(serviceToInvoke));
		Logger.Log("<UltaExecutorThread><run><urlParameters>>"+(urlParameters));
		Logger.Log("<UltaExecutorThread><run><responseBean>>"+(responseBean));
		try {
			switch (this.httpProtocol) {
			case http:
//				jsonResponseString = WebserviceExecutor.executeHttpWebservice(httpMethod, serviceToInvoke, urlParameters, imageByteArray, isUserSessionClearingRequired);
				responseStream = WebserviceExecutor.executeHttpWebservice(this.invokerParams);
//				threadHandler.setResponseBean(UltaJsonParser.mapObjectFromJson(jsonResponseString, responseBean, additionalRequestInformation));
				if (ResponseParserFormat.JSON == this.responseFormat) {
					threadHandler.setResponseBean(UltaJsonParser.mapObjectFromJson(formatResponseToString(responseStream), responseBean,additionalRequestInformation));
				}else {
					threadHandler.setResponseBean(new UltaXmlParserFactory(invokerParams).getParsedPopulatedBean(responseStream));
				}
				break;
			case https:
//				responseString = WebserviceExecutor.executeHttpsWebservice(httpMethod, serviceToInvoke, urlParameters,additionalRequestInformation, isUserSessionClearingRequired);
				responseStream = WebserviceExecutor.executeHttpsWebservice(this.invokerParams);
				threadHandler.setResponseBean(UltaJsonParser.mapObjectFromJson(formatResponseToString(responseStream), responseBean, additionalRequestInformation));
				break;
			default:
				threadHandler.setErrorMessage("Undefined HTTP Protocol");
				break;
			}
		} catch (final UltaJSONParserException ultaJSONParserException) {
			Logger.Log("<UltaExecutorThread><run><UltaJSONParserException>");
			Logger.Log(ultaJSONParserException);
			threadHandler.setErrorMessage(ultaJSONParserException.getMessage());
		}catch (final UltaException ultaException) {
			Logger.Log("<UltaExecutorThread><run><UltaException>");
			Logger.Log(ultaException);
			threadHandler.setErrorMessage(ultaException.getMessage());
		} catch (final UltaXmlParserException ultaXmlParserException){
			Logger.Log("<UltaXmlParserException><run><UltaXmlParserException>");
			Logger.Log(ultaXmlParserException);
			threadHandler.setErrorMessage(ultaXmlParserException.getMessage());
		} catch (IOException ioException) {
			Logger.Log("<UltaExecutorThread><run><IOException>");
			Logger.Log(ioException);
			threadHandler.setErrorMessage(UltaException.NETWORK_ERROR_IO_EXCEPTION);
		}
		final Message msg = threadHandler.obtainMessage();
		Logger.Log("<UltaExecutorThread><run><getTarget>>"+(msg.getTarget().getClass().getSimpleName()));
		Logger.Log("<UltaExecutorThread><run><msg>>"+(msg.toString()));
		threadHandler.sendMessage(msg);
	}
	
	/**
	 * Start.
	 */
	public void start() {
		final Thread t = new Thread(this);
		t.start();
	}

}
