/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class FreeGiftBean.
 */
public class FreeGiftBean extends UltaBean{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 848880676352938697L;
	private List<FreeGiftDetailBean> atgResponse;

	public void setAtgResponse(List<FreeGiftDetailBean> atgResponse) {
		this.atgResponse = atgResponse;
	}
	/**
	 * Gets the free gifts.
	 *
	 * @return the freeGifts
	 */
	
	public List<FreeGiftDetailBean> getAtgResponse() {
		return atgResponse;
	}

	
}
