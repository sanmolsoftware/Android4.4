/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;

import com.google.gson.annotations.SerializedName;



/**
 * The Class GooglePlacesBean.
 *
 * @author viva
 */
public class GooglePlacesBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2067812040054115887L;
	/** The places url. */
	@SerializedName("url")
	private String placesUrl;

	/**
	 * Gets the places url.
	 *
	 * @return the placesUrl
	 */
	public String getPlacesUrl() {
		return placesUrl;
	}

	/**
	 * Sets the places url.
	 *
	 * @param placesUrl the placesUrl to set
	 */
	public void setPlacesUrl(String placesUrl) {
		this.placesUrl = placesUrl;
	}
	
	
	
}
