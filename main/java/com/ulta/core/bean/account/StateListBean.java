/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;


import com.ulta.core.bean.UltaBean;

import java.util.List;




/**
 * The Class StateListBean.
 */
public class StateListBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1564189518827408668L;
	/** The state list. */
	private List<String> stateList;

	/**
	 * Gets the state list.
	 *
	 * @return the state list
	 */
	public List<String> getStateList() {
		return stateList;
	}

	/**
	 * Sets the state list.
	 *
	 * @param stateList the new state list
	 */
	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	
}
