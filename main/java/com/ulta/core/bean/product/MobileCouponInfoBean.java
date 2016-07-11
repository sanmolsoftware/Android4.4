package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class MobileCouponInfoBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6589148134655303579L;

	private MobileCouponInfoAttributesBean attributes;
	
	private String couponDescription;
	
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCouponDescription() {
		return couponDescription;
	}

	public void setCouponDescription(String couponDescription) {
		this.couponDescription = couponDescription;
	}

	public MobileCouponInfoAttributesBean getAttributes() {
		return attributes;
	}

	public void setAttributes(MobileCouponInfoAttributesBean attributes) {
		this.attributes = attributes;
	}
	
	

}
