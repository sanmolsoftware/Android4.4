/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class CheckoutOrderAdjustmentBean.
 */
public class CheckoutOrderAdjustmentBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6288824084666229851L;

	/** The pricing model. */
	private String pricingModel;
	
	/** The total adjustment. */
	private double totalAdjustment;
	
	/**
	 * Gets the pricing model.
	 *
	 * @return the pricing model
	 */
	public String getPricingModel() {
		return pricingModel;
	}
	
	/**
	 * Sets the pricing model.
	 *
	 * @param pricingModel the new pricing model
	 */
	public void setPricingModel(String pricingModel) {
		this.pricingModel = pricingModel;
	}
	
	/**
	 * Gets the total adjustment.
	 *
	 * @return the total adjustment
	 */
	public double getTotalAdjustment() {
		return totalAdjustment;
	}
	
	/**
	 * Sets the total adjustment.
	 *
	 * @param totalAdjustment the new total adjustment
	 */
	public void setTotalAdjustment(double totalAdjustment) {
		this.totalAdjustment = totalAdjustment;
	}
	

}
