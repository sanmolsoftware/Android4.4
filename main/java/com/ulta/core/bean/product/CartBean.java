/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.CouponDiscountBean;

import java.util.List;



/**
 * The Class CartBean.
 */
public class CartBean extends UltaBean{
	 
 	/**
	 * 
	 */
	private static final long serialVersionUID = -5108336381429530851L;

	/** The commerce items. */
 	private List<CommerceItemBean> commerceItems;
 	
 	/** The commerce items. */
 	private List<CommerceItemBean> electronicGiftCardCommerceItems;
 	
	 /** The order adjustments. */
 	private List<OrderAdjustmentBean> orderAdjustments;

	/** The order details. */
 	private OrderDetailBean orderDetails;
	 
 	/** The payment groups. */
 	private List<PaymentBean> paymentGroups;
	 
 	/** The shipping groups. */
 	private List<ShippingBean> shippingGroups;
 	
 	private CouponDiscountBean couponDiscount;
	
 	public CouponDiscountBean getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(CouponDiscountBean couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	public List<CommerceItemBean> getElectronicGiftCardCommerceItems() {
		return electronicGiftCardCommerceItems;
	}

	public void setElectronicGiftCardCommerceItems(
			List<CommerceItemBean> electronicGiftCardCommerceItems) {
		this.electronicGiftCardCommerceItems = electronicGiftCardCommerceItems;
	}
	
	 /**
 	 * Gets the commerce items.
 	 *
 	 * @return the commerceItems
 	 */
	public List<CommerceItemBean> getCommerceItems() {
		return commerceItems;
	}
	
	/**
	 * Sets the commerce items.
	 *
	 * @param commerceItems the commerceItems to set
	 */
	public void setCommerceItems(List<CommerceItemBean> commerceItems) {
		this.commerceItems = commerceItems;
	}
	
	/**
	 * Gets the order adjustments.
	 *
	 * @return the couponDiscount
	 */
//	public double getCouponDiscount() {
//		return couponDiscount;
//	}
	/**
	 * @param couponDiscount the couponDiscount to set
	 */
//	public void setCouponDiscount(double couponDiscount) {
//		this.couponDiscount = couponDiscount;
//	}
	/**
	 * @return the orderAdjustments
	 */
	public List<OrderAdjustmentBean> getOrderAdjustments() {
		return orderAdjustments;
	}
	
	/**
	 * Sets the order adjustments.
	 *
	 * @param orderAdjustments the orderAdjustments to set
	 */
	public void setOrderAdjustments(List<OrderAdjustmentBean> orderAdjustments) {
		this.orderAdjustments = orderAdjustments;
	}
	
	/**
	 * Gets the order details.
	 *
	 * @return the orderDetails
	 */
	public OrderDetailBean getOrderDetails() {
		return orderDetails;
	}
	
	/**
	 * Sets the order details.
	 *
	 * @param orderDetails the orderDetails to set
	 */
	public void setOrderDetails(OrderDetailBean orderDetails) {
		this.orderDetails = orderDetails;
	}
	
	/**
	 * Gets the payment groups.
	 *
	 * @return the paymentGroups
	 */
	public List<PaymentBean> getPaymentGroups() {
		return paymentGroups;
	}
	
	/**
	 * Sets the payment groups.
	 *
	 * @param paymentGroups the paymentGroups to set
	 */
	public void setPaymentGroups(List<PaymentBean> paymentGroups) {
		this.paymentGroups = paymentGroups;
	}
	
	/**
	 * Gets the shipping groups.
	 *
	 * @return the shippingGroups
	 */
	public List<ShippingBean> getShippingGroups() {
		return shippingGroups;
	}
	
	/**
	 * Sets the shipping groups.
	 *
	 * @param shippingGroups the shippingGroups to set
	 */
	public void setShippingGroups(List<ShippingBean> shippingGroups) {
		this.shippingGroups = shippingGroups;
	}
}
