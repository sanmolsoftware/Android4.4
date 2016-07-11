package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class BrandBean extends UltaBean {
	String brandId;
	String brandName;
	String isParent;
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getIsParent() {
		return isParent;
	}
	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}
}
