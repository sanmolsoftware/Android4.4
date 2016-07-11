/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class FreeSamplesBean.
 */
public class FreeSamplesBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -883818864373097180L;
	/** The free samples. */
	@SerializedName("atgResponse")
	private List<FreeSamplesDetailBean> freeSamples;

	/**
	 * Gets the free samples.
	 *
	 * @return the freeSamples
	 */
	public List<FreeSamplesDetailBean> getFreeSamples() {
		return freeSamples;
	}

	/**
	 * Sets the free samples.
	 *
	 * @param freeSamples the freeSamples to set
	 */
	public void setFreeSamples(List<FreeSamplesDetailBean> freeSamples) {
		this.freeSamples = freeSamples;
	}
}
