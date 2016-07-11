/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;



/**
 * The Class LoginBean.
 *
 * @author viva
 */
public class LoginBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6595141077328693275L;

	/** The result. */
	private String result;
	
	private ComponentBean component;
	

	public ComponentBean getComponent() {
		return component;
	}

	public void setComponent(ComponentBean component) {
		this.component = component;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	
	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(String result) {
		this.result = result;
	}
	

}
