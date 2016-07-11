package com.ulta.core.bean.search;

import com.ulta.core.bean.UltaBean;

public class RedirectInfoBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2593567969704046634L;

	private String productId;
	
	private String skuId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
	
	

}
