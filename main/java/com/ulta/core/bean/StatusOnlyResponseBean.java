/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;

import com.google.gson.annotations.SerializedName;



/**
 * The Class StatusOnlyResponseBean.
 *
 * @author viva
 * Class for handling the status only kind of responses.
 */
public class StatusOnlyResponseBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1733240630789929053L;
	/** The response status. */
	@SerializedName("atgResponse")
	private String responseStatus;

	/**
	 * Gets the response status.
	 *
	 * @return the responseStatus
	 */
	public String getResponseStatus() {
		return responseStatus;
	}

	/**
	 * Sets the response status.
	 *
	 * @param responseStatus the responseStatus to set
	 */
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	
	
	
}
