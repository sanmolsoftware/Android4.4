/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class BrandDetailsBean.
 *
 * @author viva
 */
public class BrandDetailsBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3334674778552260979L;

	/** The brand id. */
	private String brandId;

	/** The brand name. */
	private String brandName;

	/**
	 * Gets the brand id.
	 *
	 * @return the brandId
	 */
	public String getBrandId() {
		return brandId;
	}

	/**
	 * Sets the brand id.
	 *
	 * @param brandId the brandId to set
	 */
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * Gets the brand name.
	 *
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * Sets the brand name.
	 *
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}



}
