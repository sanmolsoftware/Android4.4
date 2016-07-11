package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class RecentlyViewedProductsBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5293269235902284753L;
	private String brandName;
	private String childSkuId;
	private double listPrice;
	private String onSale;
	private String productDisplayName;
	private String productId;
	private String productImageUrl;
	private double salePrice;
	private String skuDisplayName;
	private String skuImageUrl;
	private String badgeImgURL;
	private String badgeName;

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public String getBadgeImgURL() {
		return badgeImgURL;
	}

	public void setBadgeImgURL(String badgeImgURL) {
		this.badgeImgURL = badgeImgURL;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getChildSkuId() {
		return childSkuId;
	}

	public void setChildSkuId(String childSkuId) {
		this.childSkuId = childSkuId;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public String getOnSale() {
		return onSale;
	}

	public void setOnSale(String onSale) {
		this.onSale = onSale;
	}

	public String getProductDisplayName() {
		return productDisplayName;
	}

	public void setProductDisplayName(String productDisplayName) {
		this.productDisplayName = productDisplayName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductImageUrl() {
		return productImageUrl;
	}

	public void setProductImageUrl(String productImageUrl) {
		this.productImageUrl = productImageUrl;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public String getSkuDisplayName() {
		return skuDisplayName;
	}

	public void setSkuDisplayName(String skuDisplayName) {
		this.skuDisplayName = skuDisplayName;
	}

	public String getSkuImageUrl() {
		return skuImageUrl;
	}

	public void setSkuImageUrl(String skuImageUrl) {
		this.skuImageUrl = skuImageUrl;
	}

}
