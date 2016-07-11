package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class MyPurchasesBean extends UltaBean {

	private PurchaseComponentBean component;
	private String result;
	public void setComponent(PurchaseComponentBean component) {
		this.component = component;
	}
	public PurchaseComponentBean getComponent() {
		return component;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResult() {
		return result;
	}
	
}
