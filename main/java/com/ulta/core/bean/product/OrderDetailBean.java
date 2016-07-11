/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class OrderDetailBean.
 */
public class OrderDetailBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1794401689467077107L;

	private double freeShippingAmount;
	
	private double subtotalForFreeShipCheck;

	/** The contains gift message. */
	private boolean containsGiftMessage;
	
	/** The contains gift wrap. */
	private boolean containsGiftWrap;
	
	/** The currency code. */
	private String currencyCode;
	
	/** The id. */
	private String id;
	
	/** The profile id. */
	private String profileId;
	
	/** The raw subtotal. */
	private double rawSubtotal;
	
	/** The shipping. */
	private double shipping;
	
	/** The state. */
	private int state;
	
	/** The tax. */
	private double tax;
	
	/** The total. */
	private double total;
	
	private double totalNew;
	private String shippingMessage;

	public String getShippingMessage() {
		return shippingMessage;
	}

	public void setShippingMessage(String shippingMessage) {
		this.shippingMessage = shippingMessage;
	}



	private String tieredDiscountAmount;

	public String getTieredDiscountAmount() {
		return tieredDiscountAmount;
	}

	public void setTieredDiscountAmount(String tieredDiscountAmount) {
		this.tieredDiscountAmount = tieredDiscountAmount;
	}


	public double getTotalNew() {
		return totalNew;
	}

	public void setTotalNew(double totalNew) {
		this.totalNew = totalNew;
	}

	private String couponCode;
	
	/**
	 * Checks if is contains gift message.
	 *
	 * @return the containsGiftMessage
	 */
	public boolean isContainsGiftMessage() {
		return containsGiftMessage;
	}
	
	/**
	 * Sets the contains gift message.
	 *
	 * @param containsGiftMessage the containsGiftMessage to set
	 */
	public void setContainsGiftMessage(boolean containsGiftMessage) {
		this.containsGiftMessage = containsGiftMessage;
	}
	
	/**
	 * Checks if is contains gift wrap.
	 *
	 * @return the containsGiftWrap
	 */
	public boolean isContainsGiftWrap() {
		return containsGiftWrap;
	}
	
	/**
	 * Sets the contains gift wrap.
	 *
	 * @param containsGiftWrap the containsGiftWrap to set
	 */
	public void setContainsGiftWrap(boolean containsGiftWrap) {
		this.containsGiftWrap = containsGiftWrap;
	}
	
	/**
	 * Gets the currency code.
	 *
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * Sets the currency code.
	 *
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the profile id.
	 *
	 * @return the profileId
	 */
	public String getProfileId() {
		return profileId;
	}
	
	/**
	 * Sets the profile id.
	 *
	 * @param profileId the profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	/**
	 * Gets the raw subtotal.
	 *
	 * @return the rawSubtotal
	 */
	public double getRawSubtotal() {
		return rawSubtotal;
	}
	
	/**
	 * Sets the raw subtotal.
	 *
	 * @param rawSubtotal the rawSubtotal to set
	 */
	public void setRawSubtotal(double rawSubtotal) {
		this.rawSubtotal = rawSubtotal;
	}
	
	/**
	 * Gets the shipping.
	 *
	 * @return the shipping
	 */
	public double getShipping() {
		return shipping;
	}
	
	/**
	 * Sets the shipping.
	 *
	 * @param shipping the shipping to set
	 */
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}
	
	/**
	 * Gets the tax.
	 *
	 * @return the tax
	 */
	public double getTax() {
		return tax;
	}
	
	/**
	 * Sets the tax.
	 *
	 * @param tax the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}
	
	/**
	 * Gets the total.
	 *
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}
	
	/**
	 * Sets the total.
	 *
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}
	public double getFreeShippingAmount() {
		return freeShippingAmount;
	}

	public void setFreeShippingAmount(double freeShippingAmount) {
		this.freeShippingAmount = freeShippingAmount;
	}

	public double getSubtotalForFreeShipCheck() {
		return subtotalForFreeShipCheck;
	}

	public void setSubtotalForFreeShipCheck(double subtotalForFreeShipCheck) {
		this.subtotalForFreeShipCheck = subtotalForFreeShipCheck;
	}

	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public String getCouponCode() {
		return couponCode;
	}
}
