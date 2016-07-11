/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class CheckoutShippingMethodsAndRedeemLevelsBean.
 */
public class CheckoutShippingMethodsAndRedeemLevelsBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5483140196673472633L;
	/** The available shipping methods. */
	private List<CheckoutShippingMethodDetailsBean> availableShippingMethods;
	

	private String shippingErrorMessage;
	
	
	//private List<String> redeemLevels;

	/**
	 * Gets the available shipping methods.
	 *
	 * @return the available shipping methods
	 */
	public List<CheckoutShippingMethodDetailsBean> getAvailableShippingMethods() {
		return availableShippingMethods;
	}

	/**
	 * Sets the available shipping methods.
	 *
	 * @param availableShippingMethods the new available shipping methods
	 */
	public void setAvailableShippingMethods(
			List<CheckoutShippingMethodDetailsBean> availableShippingMethods) {
		this.availableShippingMethods = availableShippingMethods;
	}
	
	
	public String getShippingErrorMessage() {
		return shippingErrorMessage;
	}

	public void setShippingErrorMessage(String shippingErrorMessage) {
		this.shippingErrorMessage = shippingErrorMessage;
	}

	
}
