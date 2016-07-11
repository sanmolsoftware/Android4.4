/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class AddToCartBean.
 */
public class AddToCartBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7211263674905447623L;

	/** The component. */
	private ComponentBean component;
	
	/** The result. */
	private boolean result;
	
	/**
	 * Gets the component.
	 *
	 * @return the component
	 */
	public ComponentBean getComponent() {
		return component;
	}
	
	/**
	 * Sets the component.
	 *
	 * @param component the component to set
	 */
	public void setComponent(ComponentBean component) {
		this.component = component;
	}
	
	/**
	 * Checks if is result.
	 *
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}
	
	/**
	 * Sets the result.
	 *
	 * @param result the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}
	
	
}
