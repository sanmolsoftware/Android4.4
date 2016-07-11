package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class MobileOffersDescBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3509610730802545456L;

	private String couponDescription;
	
	private String couponUrl;

	public String getCouponDescription() {
		return couponDescription;
	}

	public void setCouponDescription(String couponDescription) {
		this.couponDescription = couponDescription;
	}

	public String getCouponUrl() {
		return couponUrl;
	}

	public void setCouponUrl(String couponUrl) {
		this.couponUrl = couponUrl;
	}
	
	

}
