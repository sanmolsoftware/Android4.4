/**
 *
 * Copyright(c) ULTA, Inc. All Rights reserved.
 *
 *
 */

package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

/**
 * The Class MobileCouponInfoAttributesBean.
 *
 * @author Infosys
 */

public class MobileCouponInfoAttributesBean extends UltaBean{
	

	private static final long serialVersionUID = -2855402166428770959L;
	private String isCouponActive;
	


	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String geteCouponActive() {
		return isCouponActive;
	}

	public void seteCouponActive(String isCouponActive) {
		this.isCouponActive = isCouponActive;
	}
	
	

}
