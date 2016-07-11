/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class OrderAdjustmentBean.
 */
public class OrderAdjustmentBean extends UltaBean{
/**
	 * 
	 */
	private static final long serialVersionUID = 2260347706356664625L;
		//		private String pricingModel;
		/** The total adjustment. */
private double totalAdjustment;
		
		/**
		 * Gets the total adjustment.
		 *
		 * @return the pricingModel
		 */
//		public String getPricingModel() {
//			return pricingModel;
//		}
		/**
		 * @param pricingModel the pricingModel to set
		 */
//		public void setPricingModel(String pricingModel) {
//			this.pricingModel = pricingModel;
//		}
		
		/**
		 * @return the totalAdjustment
		 */
		public double getTotalAdjustment() {
			return totalAdjustment;
		}
		
		/**
		 * Sets the total adjustment.
		 *
		 * @param totalAdjustment the totalAdjustment to set
		 */
		public void setTotalAdjustment(double totalAdjustment) {
			this.totalAdjustment = totalAdjustment;
		} 

}
