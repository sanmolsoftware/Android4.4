package com.ulta.core.bean.search;


import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.product.FacetDetailBean;

import java.util.List;


public class FacetListingBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7781246840152995691L;
	private List<FacetDetailBean> brandFacets ;
	private List<FacetDetailBean> categoryFacets ;
	private List<FacetDetailBean> colorFacets;
	private List<FacetDetailBean> priceFacets ;
	private List<FacetDetailBean> promotionFacets ;
	
	public List<FacetDetailBean> getBrandFacets() {
		return brandFacets;
	}
	public void setBrandFacets(List<FacetDetailBean> brandFacets) {
		this.brandFacets = brandFacets;
	}
	public List<FacetDetailBean> getCategoryFacets() {
		return categoryFacets;
	}
	public void setCategoryFacets(List<FacetDetailBean> categoryFacets) {
		this.categoryFacets = categoryFacets;
	}
	public List<FacetDetailBean> getColorFacets() {
		return colorFacets;
	}
	public void setColorFacets(List<FacetDetailBean> colorFacets) { 
		this.colorFacets = colorFacets;
	}
	public List<FacetDetailBean> getPriceFacets() {
		return priceFacets;
	}
	public void setPriceFacets(List<FacetDetailBean> priceFacets) {
		this.priceFacets = priceFacets;
	}
	public List<FacetDetailBean> getPromotionFacets() {
		return promotionFacets;
	}
	public void setPromotionFacets(List<FacetDetailBean> promotionFacets) {
		this.promotionFacets = promotionFacets;
	}
}
