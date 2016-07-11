package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class CouponDiscountBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4440973075583827603L;



	private String ItemDiscount;
	private String orderDiscount;
	private String ShippingDiscount;
	public String getShippingDiscount() {
		return ShippingDiscount;
	}

	public void setShippingDiscount(String ShippingDiscount) {
		this.ShippingDiscount = ShippingDiscount;
	}


	private String couponCode;
	private String isShippingFree;
	private String shippingDescription;
	private String totalAdjustment;
	public String getCouponDescription() {
		return couponDescription;
	}
	public void setCouponDescription(String couponDescription) {
		this.couponDescription = couponDescription;
	}
	private String couponDescription;
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public String isShippingFree() {
		return isShippingFree;
	}
	public void setShippingFree(String isShippingFree) {
		this.isShippingFree = isShippingFree;
	}
	public String getShippingDescription() {
		return shippingDescription;
	}
	public void setShippingDescription(String shippingDescription) {
		this.shippingDescription = shippingDescription;
	}
	public String getTotalAdjustment() {
		return totalAdjustment;
	}
	public void setTotalAdjustment(String totalAdjustment) {
		this.totalAdjustment = totalAdjustment;
	}
	public String getItemDiscount() {
		return ItemDiscount;
	}

	public void setItemDiscount(String itemDiscount) {
		ItemDiscount = itemDiscount;
	}

	public String getOrderDiscount() {
		return orderDiscount;
	}

	public void setOrderDiscount(String orderDiscount) {
		this.orderDiscount = orderDiscount;
	}

}
