/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class ProductListBean.
 */
public class ProductListBean extends UltaBean  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4185498590141429793L;
	/** The products. */
	@SerializedName("atgResponse")
	private List<ProductsInListBean> products;

	/**
	 * Gets the products.
	 *
	 * @return the products
	 */
	public List<ProductsInListBean> getProducts() {
		return products;
	}

	/**
	 * Sets the products.
	 *
	 * @param products the products to set
	 */
	public void setProducts(List<ProductsInListBean> products) {
		this.products = products;
	}
}
