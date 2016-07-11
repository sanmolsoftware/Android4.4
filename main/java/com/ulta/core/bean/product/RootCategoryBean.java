package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class RootCategoryBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4090199676124955960L;
	private String facetListing;
	private List<RootCategorySearchBean> searchResults;
	private RootCategoryFacetListingBean facetListingForLeaf;
	private int totalNoOfProducts;



	public List<RootCategorySearchBean> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<RootCategorySearchBean> searchResults) {
		this.searchResults = searchResults;
	}

	public int getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(int totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public String getFacetListing() {
		return facetListing;
	}

	public void setFacetListing(String facetListing) {
		this.facetListing = facetListing;
	}

	public RootCategoryFacetListingBean getFacetListingForLeaf() {
		return facetListingForLeaf;
	}

	public void setFacetListingForLeaf(RootCategoryFacetListingBean facetListingForLeaf) {
		this.facetListingForLeaf = facetListingForLeaf;
	}

	

	
}
