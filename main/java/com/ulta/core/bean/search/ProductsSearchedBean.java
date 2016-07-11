package com.ulta.core.bean.search;

import android.util.Log;

import com.ulta.core.bean.UltaBean;

import java.util.ArrayList;
import java.util.List;

public class ProductsSearchedBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8632455424440865757L;
	private int pageTotal;
	private int totalResultCount;
	private int thisPage;
	private List<ProductSearchBean> productList=new ArrayList<ProductSearchBean>();
	private List<FacetGroupSearchBean> facetGroupList=new ArrayList<FacetGroupSearchBean>();
	
	public List<FacetGroupSearchBean> getFacetGroupList() {
		return facetGroupList;
	}

	public void setFacetGroupList(List<FacetGroupSearchBean> facetGroupList) {
		this.facetGroupList = facetGroupList;
		
//		for (int loop=0;loop<facetGroupList.size();loop++)
//		{
//			FacetGroupSearchBean facetsGroup=facetGroupList.get(loop);
//		
//			Logger.Log("----------FACET GROUP--------"+(loop+1));
//			Logger.Log(" facet group id ::"+facetsGroup.getId());
//			Logger.Log("facet group name ::"+facetsGroup.getName());
//			
//			List<FacetSearchBean> facetList=facetsGroup.getFacetList();
//			for (int innerLoop=0;innerLoop<facetList.size();innerLoop++)
//			{
//				FacetSearchBean facet=facetList.get(innerLoop);
//			
//				Logger.Log("----------FACETS--------"+(innerLoop+1));
//				Logger.Log(" facet id ::"+facet.getId());
//				Logger.Log("facet name ::"+facet.getName());
//				Logger.Log("facet count ::"+facet.getCount());
//			}
//		}
	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
		Log.i("","totalResultCount ::"+totalResultCount);
	}

	public int getThisPage() {
		return thisPage;
	}

	public void setThisPage(int thisPage) {
		this.thisPage = thisPage;
		Log.i("","thisPage ::"+thisPage);
	}
	
	public List<ProductSearchBean> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductSearchBean> productList) {
		this.productList = productList;
		
//		for (int loop=0;loop<productList.size();loop++)
//		{
//			ProductSearchBean product=productList.get(loop);
//		
//			Logger.Log("----------PRODUCT--------"+(loop+1));
//			Logger.Log("url ::"+product.getUrl());
//			Logger.Log("title ::"+product.getTitle());
//			Logger.Log("text ::"+product.getText());
//			Logger.Log("image_url ::"+product.getImage_url());
//			Logger.Log("price ::"+product.getPrice());
//			Logger.Log("review_rating ::"+product.getReview_rating());
//			Logger.Log("review_count ::"+product.getReview_count());
//			Logger.Log("product_id ::"+product.getProduct_id());
//			Logger.Log("sku_id ::"+product.getSku_id());
//			Logger.Log("ad_bug ::"+product.getAd_bug());
//			Logger.Log("brand ::"+product.getBrand());
//			Logger.Log("has_variant ::"+product.getHas_variant());
//			Logger.Log("url_title ::"+product.getUrl_title());
//			Logger.Log("rank ::"+product.getRank());
//		}
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
		Log.i("","pageTotal ::"+pageTotal);
	}
}
