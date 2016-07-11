/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.net.handler;

import android.os.Handler;

import com.ulta.core.bean.UltaBean;



/**
 * The Class UltaHandler.
 *
 * @author viva
 */
public class UltaHandler extends Handler {

	/** The error code. */
	private String errorCode;
	
	/** The error message. */
	private String errorMessage;
	
	/** The error description. */
	private String errorDescription;
	
	/** The response bean. */
	private UltaBean responseBean;

	/**
	 * Gets the error code.
	 *
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Sets the error code.
	 *
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Gets the error message.
	 *
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the error message.
	 *
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * Gets the error description.
	 *
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * Sets the error description.
	 *
	 * @param errorDescription the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * Gets the response bean.
	 *
	 * @return the responseBean
	 */
	public UltaBean getResponseBean() {
		return responseBean;
	}

	/**
	 * Sets the response bean.
	 *
	 * @param responseBean the responseBean to set
	 */
	public void setResponseBean(UltaBean responseBean) {
		this.responseBean = responseBean;
	}

	
	
	
}
