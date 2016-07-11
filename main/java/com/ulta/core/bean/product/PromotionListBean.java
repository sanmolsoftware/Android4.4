package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.search.FacetListingBean;
import com.ulta.core.bean.search.RedirectInfoBean;
import com.ulta.core.bean.search.SearchResultsBean;

import java.util.List;


public class PromotionListBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6824062446808849423L;
	private FacetListingBean facetListing;
	private FacetListingBean facetListingForLeaf;
	private List<SearchResultsBean> searchResults;	
	private RedirectInfoBean redirectInfo;
	private int totalNoOfProducts;
	private boolean redirectToPDP;

	public boolean isRedirectToPDP() {
		return redirectToPDP;
	}

	public void setRedirectToPDP(boolean redirectToPDP) {
		this.redirectToPDP = redirectToPDP;
	}

	public RedirectInfoBean getRedirectInfo() {
		return redirectInfo;
	}

	public void setRedirectInfo(RedirectInfoBean redirectInfo) {
		this.redirectInfo = redirectInfo;
	}

	public List<SearchResultsBean> getSearchResults() {
		return searchResults;
	}

	public void setSearchResults(List<SearchResultsBean> searchResults) {
		this.searchResults = searchResults;
	}

	public int getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(int totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public FacetListingBean getFacetListing() {
		return facetListing;
	}

	public void setFacetListing(FacetListingBean facetListing) {
		this.facetListing = facetListing;
	}	
	
	public FacetListingBean getFacetListingForLeaf() {
		return facetListingForLeaf;
	}

	public void setFacetListingForLeaf(FacetListingBean facetListingForLeaf) {
		this.facetListingForLeaf = facetListingForLeaf;
	}
}
