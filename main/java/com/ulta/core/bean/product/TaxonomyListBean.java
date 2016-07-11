package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class TaxonomyListBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6462749063687457033L;
	private double CategoryDimensionId;
	private String Name;
	private String totalNoOfProducts;
	private List<TaxonomySubCategories> subCategories;
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
	public List<TaxonomySubCategories> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<TaxonomySubCategories> subCategories) {
		this.subCategories = subCategories;
	}
	public String getTotalNoOfProducts() {
		return totalNoOfProducts;
	}
	public void setTotalNoOfProducts(String totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}
}
