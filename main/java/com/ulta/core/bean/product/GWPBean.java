package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class GWPBean extends UltaBean {
	
	private String id;
	private String offerDesc;
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}
	public String getOfferDesc() {
		return offerDesc;
	}
	

}
