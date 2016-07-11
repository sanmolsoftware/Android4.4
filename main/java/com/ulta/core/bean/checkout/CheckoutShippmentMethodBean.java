/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class CheckoutShippmentMethodBean.
 */
public class CheckoutShippmentMethodBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -992391715895128377L;

	/** The component. */
	private CheckoutComponentBean component;
	
	/** The result. */
	private boolean result; 

	/**
	 * Gets the component.
	 *
	 * @return the component
	 */
	public CheckoutComponentBean getComponent() {
		return component;
	}

	/**
	 * Sets the component.
	 *
	 * @param component the new component
	 */
	public void setComponent(CheckoutComponentBean component) {
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
