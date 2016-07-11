package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class TaxonomySubCategories extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1672852820806933749L;
	private double CategoryDimensionId;
	private String Name;
	private String isLeaf;
	private String totalNoOfProducts;
	public double getCategoryDimensionId() {
		return CategoryDimensionId;
	}
	public void setCategoryDimensionId(double categoryDimensionId) {
		CategoryDimensionId = categoryDimensionId;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(String isLeaf) {
		this.isLeaf = isLeaf;
	}
	public String getTotalNoOfProducts() {
		return totalNoOfProducts;
	}
	public void setTotalNoOfProducts(String totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}
}
