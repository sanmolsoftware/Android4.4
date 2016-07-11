/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;





/**
 * The Class ResponseMessageBean.
 *
 * @author viva
 */
public class ResponseMessageBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -731129334705233063L;

	/** The response object. */
	private String responseObject;
	
	/** The response type. */
	private String responseType;
	
	/** The list. */
	private Boolean list;

	/**
	 * Gets the response object.
	 *
	 * @return the responseObject
	 */
	public String getResponseObject() {
		return responseObject;
	}

	/**
	 * Sets the response object.
	 *
	 * @param responseObject the responseObject to set
	 */
	public void setResponseObject(String responseObject) {
		this.responseObject = responseObject;
	}

	/**
	 * Gets the response type.
	 *
	 * @return the responseType
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * Sets the response type.
	 *
	 * @param responseType the responseType to set
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * Gets the list.
	 *
	 * @return the list
	 */
	public Boolean getList() {
		return list;
	}

	/**
	 * Sets the list.
	 *
	 * @param list the list to set
	 */
	public void setList(Boolean list) {
		this.list = list;
	}

	

	
	
	

}
