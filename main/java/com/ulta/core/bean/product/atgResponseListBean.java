package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class atgResponseListBean extends UltaBean {
	private String displayName;
	private String id;
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	
	

}
