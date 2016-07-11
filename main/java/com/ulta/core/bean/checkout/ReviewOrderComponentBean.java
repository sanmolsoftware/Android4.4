/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class ReviewOrderComponentBean.
 */
public class ReviewOrderComponentBean extends UltaBean {
/**
	 * 
	 */
	private static final long serialVersionUID = 3557678748703742044L;

	//private PaymentDetails paymentDetails;
	/** The payment details. */
private CheckoutCartBean paymentDetails;

	/**
	 * Gets the payment details.
	 *
	 * @return the payment details
	 */
	public CheckoutCartBean getPaymentDetails() {
		return paymentDetails;
	}

	/**
	 * Sets the payment details.
	 *
	 * @param paymentDetails the new payment details
	 */
	public void setPaymentDetails(CheckoutCartBean paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
	
	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	//3.3 Release
	private String tokenId;
	
	private String email;
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
