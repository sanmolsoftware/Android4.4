/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class FacetsBean.
 */
public class FacetsBean extends UltaBean  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7859383529924256222L;

	/** The brand facets. */
	private List<FacetDetailBean> brandFacets;
	
	/** The category facets. */
	private List<FacetDetailBean> categoryFacets;
	
	/** The price facets. */
	private List<FacetDetailBean> priceFacets;

	/**
	 * Gets the category facets.
	 *
	 * @return the categoryFacets
	 */
	public List<FacetDetailBean> getCategoryFacets() {
		return categoryFacets;
	}
	
	/**
	 * Sets the category facets.
	 *
	 * @param categoryFacets the categoryFacets to set
	 */
	public void setCategoryFacets(List<FacetDetailBean> categoryFacets) {
		this.categoryFacets = categoryFacets;
	}
	
	/**
	 * Gets the price facets.
	 *
	 * @return the priceFacets
	 */
	public List<FacetDetailBean> getPriceFacets() {
		return priceFacets;
	}
	
	/**
	 * Sets the price facets.
	 *
	 * @param priceFacets the priceFacets to set
	 */
	public void setPriceFacets(List<FacetDetailBean> priceFacets) {
		this.priceFacets = priceFacets;
	}
	
	/**
	 * Gets the brand facets.
	 *
	 * @return the facets
	 */
	public List<FacetDetailBean> getBrandFacets() {
		return brandFacets;
	}
	
	/**
	 * Sets the brand facets.
	 *
	 * @param brandFacets the new brand facets
	 */
	public void setBrandFacets(List<FacetDetailBean> brandFacets) {
		this.brandFacets = brandFacets;
	}

	
}
