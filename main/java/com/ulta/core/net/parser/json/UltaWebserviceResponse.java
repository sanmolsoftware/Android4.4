/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.parser.json;

import com.ulta.core.bean.ResponseMessageBean;

import org.json.JSONObject;



/**
 * The Class UltaWebserviceResponse.
 *
 * @author viva
 */
public class UltaWebserviceResponse {

	/** The response message. */
	private JSONObject responseMessage;
	
	/** The fault message. */
	private JSONObject faultMessage;
	
	/** The response message bean. */
	private ResponseMessageBean responseMessageBean;
	
	/**
	 * Gets the response message.
	 *
	 * @return the responseMessage
	 */
	public JSONObject getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Sets the response message.
	 *
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(JSONObject responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * Gets the fault message.
	 *
	 * @return the faultMessage
	 */
	public JSONObject getFaultMessage() {
		return faultMessage;
	}

	/**
	 * Sets the fault message.
	 *
	 * @param faultMessage the faultMessage to set
	 */
	public void setFaultMessage(JSONObject faultMessage) {
		this.faultMessage = faultMessage;
	}

	/**
	 * Gets the response message bean.
	 *
	 * @return the responseMessageBean
	 */
	public ResponseMessageBean getResponseMessageBean() {
		return responseMessageBean;
	}

	/**
	 * Sets the response message bean.
	 *
	 * @param responseMessageBean the responseMessageBean to set
	 */
	public void setResponseMessageBean(ResponseMessageBean responseMessageBean) {
		this.responseMessageBean = responseMessageBean;
	}
	
	
}
