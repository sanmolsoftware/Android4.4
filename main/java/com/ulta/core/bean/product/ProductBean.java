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
 * The Class ProductBean.
 *
 * @author viva
 */
public class ProductBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3164459637805956504L;

	/** The brand details. */
	private BrandDetailsBean brandDetails;
	
	/** The category details. */
	private CategoryBean categoryDetails;
	
	//3.5 Releasse
	/** Flag for RTI Check*/
	private boolean enableStoreSearchinPDP ;
	
	
	/** The product features. */
	private List<ProductFeatureBean> productFeatures;
	
	/** The product header. */
	private ProductHeaderBean productHeader;
	
	/** The product review. */
	private ProductReviewBean productReview;
	
	private List<RecommendedProductsBean> recommendedProducts;
	
	private List<RecentlyViewedProductsBean> recentlyViewedProducts;
	
	
	/**
	 * 
	 * Set the list of recently viewed product details
	 * @return
	 */
	public List<RecentlyViewedProductsBean> getRecentlyViewedProducts() {
		return recentlyViewedProducts;
	}

	public void setRecentlyViewedProducts(
			List<RecentlyViewedProductsBean> recentlyViewedProducts) {
		this.recentlyViewedProducts = recentlyViewedProducts;
	}

	/** The sku details. */
	private List<ProductSkuBean> skuDetails;

	/**
	 * Gets the brand details.
	 *
	 * @return the brandDetails
	 */
	public BrandDetailsBean getBrandDetails() {
		return brandDetails;
	}

	/**
	 * Sets the brand details.
	 *
	 * @param brandDetails the brandDetails to set
	 */
	public void setBrandDetails(BrandDetailsBean brandDetails) {
		this.brandDetails = brandDetails;
	}

	/**
	 * Gets the category details.
	 *
	 * @return the categoryDetails
	 */
	public CategoryBean getCategoryDetails() {
		return categoryDetails;
	}

	/**
	 * Sets the category details.
	 *
	 * @param categoryDetails the categoryDetails to set
	 */
	public void setCategoryDetails(CategoryBean categoryDetails) {
		this.categoryDetails = categoryDetails;
	}

	/**
	 * Gets the product features.
	 *
	 * @return the productFeatures
	 */
	public List<ProductFeatureBean> getProductFeatures() {
		return productFeatures;
	}

	/**
	 * Sets the product features.
	 *
	 * @param productFeatures the productFeatures to set
	 */
	public void setProductFeatures(List<ProductFeatureBean> productFeatures) {
		this.productFeatures = productFeatures;
	}

	/**
	 * Gets the product header.
	 *
	 * @return the productHeader
	 */
	public ProductHeaderBean getProductHeader() {
		return productHeader;
	}

	/**
	 * Sets the product header.
	 *
	 * @param productHeader the productHeader to set
	 */
	public void setProductHeader(ProductHeaderBean productHeader) {
		this.productHeader = productHeader;
	}

	/**
	 * Gets the product review.
	 *
	 * @return the productReview
	 */
	public ProductReviewBean getProductReview() {
		return productReview;
	}

	/**
	 * Sets the product review.
	 *
	 * @param productReview the productReview to set
	 */
	public void setProductReview(ProductReviewBean productReview) {
		this.productReview = productReview;
	}

	/**
	 * Gets the sku details.
	 *
	 * @return the skuDetails
	 */
	public List<ProductSkuBean> getSkuDetails() {
		return skuDetails;
	}

	public List<RecommendedProductsBean> getRecommendedProducts() {
		return recommendedProducts;
	}

	public void setRecommendedProducts(
			List<RecommendedProductsBean> recommendedProducts) {
		this.recommendedProducts = recommendedProducts;
	}

	/**
	 * Sets the sku details.
	 *
	 * @param skuDetails the skuDetails to set
	 */
	public void setSkuDetails(List<ProductSkuBean> skuDetails) {
		this.skuDetails = skuDetails;
	}

	public boolean getEnableStoreSearchinPDP() {
		return enableStoreSearchinPDP;
	}

	public void setEnableStoreSearchinPDP(boolean enableStoreSearchinPDP) {
		this.enableStoreSearchinPDP = enableStoreSearchinPDP;
	}
}
