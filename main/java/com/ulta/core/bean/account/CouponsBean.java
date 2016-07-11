package com.ulta.core.bean.account;


import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;

import java.util.List;

public class CouponsBean extends UltaBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1559205756968304086L;
	@SerializedName("atgResponse")
	private List<CouponBean> coupons;

	public List<CouponBean> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<CouponBean> coupons) {
		this.coupons = coupons;
	}

	
	
}
