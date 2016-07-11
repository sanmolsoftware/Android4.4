/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.CouponDiscountBean;

import java.util.List;

/**
 * The Class CheckoutCartBEan.
 */
public class CheckoutCartBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 512617750083861912L;

	/** The order details. */
	private CheckoutOrderDetailsBean orderDetails;

	/** The commerce items. */
	private List<CheckoutCommerceItemBean> commerceItems;

	/** The order adjustments. */
	private List<CheckoutOrderAdjustmentBean> orderAdjustments;

	/** The Credit Card payment groups. */
	private List<CheckoutCreditCardPaymentGroupBean> creditCardPaymentGroups;
	
	/** The Credit Card payment groups. */
	private List<CheckoutCommerceItemBean> electronicGiftCardCommerceItems;

	/** The Gift Card payment groups. */
	private List<CheckoutGiftCardPaymentGroupBean> giftCardPaymentGroups;

	/** The shipping groups. */
	private List<CheckouthardGoodShippingGroupBean> hardGoodShippingGroups;

	/** The shipping groups. */
	private List<CheckoutElectronicShippingGroupBean> electronicShippingGroups;


	private List<CheckoutLoyaltyPointsPaymentGroupBean> loyaltyPointsPaymentGroups;
	
	private List<CheckoutPaypalPaymentGroupsBean> paypalPaymentGroups;
	


	private CouponDiscountBean couponDiscount;

	public List<CheckoutPaypalPaymentGroupsBean> getPaypalPaymentGroups() {
		return paypalPaymentGroups;
	}

	public void setPaypalPaymentGroups(
			List<CheckoutPaypalPaymentGroupsBean> paypalPaymentGroups) {
		this.paypalPaymentGroups = paypalPaymentGroups;
	}

	public List<CheckoutCommerceItemBean> getElectronicGiftCardCommerceItems() {
		return electronicGiftCardCommerceItems;
	}

	public void setElectronicGiftCardCommerceItems(
			List<CheckoutCommerceItemBean> electronicGiftCardCommerceItems) {
		this.electronicGiftCardCommerceItems = electronicGiftCardCommerceItems;
	}


//	private double couponDiscount;
	/**
	 * Gets the order details.
	 *
	 * @return the order details
	 */
	public CheckoutOrderDetailsBean getOrderDetails() {
		return orderDetails;
	}

	/**
	 * Sets the order details.
	 *
	 * @param orderDetails the new order details
	 */
	public void setOrderDetails(CheckoutOrderDetailsBean orderDetails) {
		this.orderDetails = orderDetails;
	}

	/**
	 * Gets the commerce items.
	 *
	 * @return the commerce items
	 */
	public List<CheckoutCommerceItemBean> getCommerceItems() {
		return commerceItems;
	}

	/**
	 * Sets the commerce items.
	 *
	 * @param commerceItems the new commerce items
	 */
	public void setCommerceItems(List<CheckoutCommerceItemBean> commerceItems) {
		this.commerceItems = commerceItems;
	}

	/**
	 * Gets the order adjustments.
	 *
	 * @return the order adjustments
	 */
	public List<CheckoutOrderAdjustmentBean> getOrderAdjustments() {
		return orderAdjustments;
	}

	/**
	 * Sets the order adjustments.
	 *
	 * @param orderAdjustments the new order adjustments
	 */
	public void setOrderAdjustments(List<CheckoutOrderAdjustmentBean> orderAdjustments) {
		this.orderAdjustments = orderAdjustments;
	}

	public void setHardGoodShippingGroups(
			List<CheckouthardGoodShippingGroupBean> hardGoodShippingGroups) {
		this.hardGoodShippingGroups = hardGoodShippingGroups;
	}
	public void setGiftCardPaymentGroups(
			List<CheckoutGiftCardPaymentGroupBean> giftCardPaymentGroups) {
		this.giftCardPaymentGroups = giftCardPaymentGroups;
	}
	public void setElectronicShippingGroups(
			List<CheckoutElectronicShippingGroupBean> electronicShippingGroups) {
		this.electronicShippingGroups = electronicShippingGroups;
	}

	public void setCreditCardPaymentGroups(
			List<CheckoutCreditCardPaymentGroupBean> creditCardPaymentGroups) {
		this.creditCardPaymentGroups = creditCardPaymentGroups;
	}

	public List<CheckouthardGoodShippingGroupBean> getHardGoodShippingGroups() {
		return hardGoodShippingGroups;
	}

	public List<CheckoutGiftCardPaymentGroupBean> getGiftCardPaymentGroups() {
		return giftCardPaymentGroups;
	}

	public List<CheckoutElectronicShippingGroupBean> getElectronicShippingGroups() {
		return electronicShippingGroups;
	}

	public List<CheckoutCreditCardPaymentGroupBean> getCreditCardPaymentGroups() {
		return creditCardPaymentGroups;
	}

	public void setLoyaltyPointsPaymentGroups(
			List<CheckoutLoyaltyPointsPaymentGroupBean> loyaltyPointsPaymentGroups) {
		this.loyaltyPointsPaymentGroups = loyaltyPointsPaymentGroups;
	}

	public List<CheckoutLoyaltyPointsPaymentGroupBean> getLoyaltyPointsPaymentGroups() {
		return loyaltyPointsPaymentGroups;
	}
	public CouponDiscountBean getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(CouponDiscountBean couponDiscount) {
		this.couponDiscount = couponDiscount;
	}
}
