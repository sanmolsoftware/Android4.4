/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author viva
 * 
 */
public class GoogleURLShortenBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5977248911511450179L;

	private String kind;

	@SerializedName("id")
	private String shortUrl;

	private String longUrl;

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind
	 *            the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return the shortUrl
	 */
	public String getShortUrl() {
		return shortUrl;
	}

	/**
	 * @param shortUrl
	 *            the shortUrl to set
	 */
	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	/**
	 * @return the longUrl
	 */
	public String getLongUrl() {
		return longUrl;
	}

	/**
	 * @param longUrl
	 *            the longUrl to set
	 */
	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}
}
