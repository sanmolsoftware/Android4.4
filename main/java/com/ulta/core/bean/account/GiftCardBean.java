package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class GiftCardBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5090008586640591002L;
	private String result;
	private GiftComponentBean component;
	
	
	public void setResult(String result) {
		this.result = result;
	}
	public void setComponent(GiftComponentBean component) {
		this.component = component;
	}
	
	public String getResult() {
		return result;
	}
	
	public GiftComponentBean getComponent() {
		return component;
	}

}
