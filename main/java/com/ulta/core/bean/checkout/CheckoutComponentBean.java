/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class CheckoutComponentBean.
 */
public class CheckoutComponentBean  extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5750639486834380965L;

	/** The cart. */
	private CheckoutCartBean cart;
	
	/** The shipping methods and redeem levels. */
	private CheckoutShippingMethodsAndRedeemLevelsBean shippingMethods;
	
	/** Beauty club number */
	private String beautyClubNumber;
	
	/** points earned for the order. */
	private String orderPoints;

	/** The shipping methods and redeem levels. */
	private String balancePoints;
	
	
	public String getBalancePoints() {
		return balancePoints;
	}

	public void setBalancePoints(String balancePoints) {
		this.balancePoints = balancePoints;
	}
	public String getBeautyClubNumber() {
		return beautyClubNumber;
	}

	public void setBeautyClubNumber(String beautyClubNumber) {
		this.beautyClubNumber = beautyClubNumber;
	}


	
	public String getOrderPoints() {
		return orderPoints;
	}

	public void setOrderPoints(String orderPoints) {
		this.orderPoints = orderPoints;
	}
	
	private String planId;

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	//3.2 release
	//String to indicate anonymous user's alert message
	private String messageForAnonymousUsers;
	//String for holding the email id of the user
	private String anonymousEmailId;
	//String to indicate whether address validation is required or not
	private String mobileStatusCyberSource;

	private String login;
	
	private boolean mobileStatusVertexDAV;
	
	public String getEmail() {
		return login;
	}

	public void setEmail(String email) {
		this.login = email;
	}

	/**
	 * Gets the cart.
	 *
	 * @return the cart
	 */
	public CheckoutCartBean getCart() {
		return cart;
	}
	
	/**
	 * Sets the cart.
	 *
	 * @param cart the new cart
	 */
	public void setCart(CheckoutCartBean cart) {
		this.cart = cart;
	}
	
	/**
	 * Gets the shipping methods and redeem levels.
	 *
	 * @return the shipping methods and redeem levels
	 */
	public CheckoutShippingMethodsAndRedeemLevelsBean getShippingMethodsAndRedeemLevels() {
		return shippingMethods;
	}

	/**
	 * Sets the shipping methods and redeem levels.
	 *
	 * @param shippingMethodsAndRedeemLevels the new shipping methods and redeem levels
	 */
	public void setShippingMethodsAndRedeemLevels(
			CheckoutShippingMethodsAndRedeemLevelsBean shippingMethodsAndRedeemLevels) {
		this.shippingMethods = shippingMethodsAndRedeemLevels;
	}

	public String getMessageForAnonymousUsers() {
		return messageForAnonymousUsers;
	}

	public void setMessageForAnonymousUsers(String messageForAnonymousUsers) {
		this.messageForAnonymousUsers = messageForAnonymousUsers;
	}

	public String getAnonymousEmailId() {
		return anonymousEmailId;
	}

	public void setAnonymousEmailId(String anonymousEmailId) {
		this.anonymousEmailId = anonymousEmailId;
	}

	public String getMobileStatusCyberSource() {
		return mobileStatusCyberSource;
	}

	public void setMobileStatusCyberSource(String mobileStatusCyberSource) {
		this.mobileStatusCyberSource = mobileStatusCyberSource;
	}
	public boolean getMobileStatusVertexDAV()
	{
		return mobileStatusVertexDAV;
	}
	public void setMobileStatusVertexDAV(boolean mobileStatusVertexDAV)
	{
		this.mobileStatusVertexDAV=mobileStatusVertexDAV;
	}
}
