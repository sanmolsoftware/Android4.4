/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class ReviewOrderBean.
 */
public class ReviewOrderBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1342924869402349959L;

	/** The component. */
	private ReviewOrderComponentBean component;
	
	/** The result. */
	private boolean result; 

	

	/**
	 * Gets the component.
	 *
	 * @return the component
	 */
	public ReviewOrderComponentBean getComponent() {
		return component;
	}

	/**
	 * Sets the component.
	 *
	 * @param component the new component
	 */
	public void setComponent(ReviewOrderComponentBean component) {
		this.component = component;
	}

	/**
	 * Checks if is result.
	 *
	 * @return true, if is result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(boolean result) {
		this.result = result;
	}
}
