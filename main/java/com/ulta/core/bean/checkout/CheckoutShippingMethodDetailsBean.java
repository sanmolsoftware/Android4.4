/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class CheckoutShippingMethodDetailsBean.
 */
public class CheckoutShippingMethodDetailsBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8606421124019616996L;

	/** The amount. */
	private double	amount;
	
	/** The shipping method. */
	private String  shippingMethod;
	
	//3.2 release
	/**Shipping message*/
	private String shippingMessage;
	
	public String getEstDateOfDelivery() {
		return EstDateOfDelivery;
	}

	public void setEstDateOfDelivery(String estDateOfDelivery) {
		EstDateOfDelivery = estDateOfDelivery;
	}

	private String EstDateOfDelivery;
	
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount.
	 *
	 * @param amount the new amount
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	/**
	 * Gets the shipping method.
	 *
	 * @return the shipping method
	 */
	public String getShippingMethod() {
		return shippingMethod;
	}
	
	/**
	 * Sets the shipping method.
	 *
	 * @param shippingMethod the new shipping method
	 */
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingMessage() {
		return shippingMessage;
	}

	public void setShippingMessage(String shippingMessage) {
		this.shippingMessage = shippingMessage;
	}
	
	

}
