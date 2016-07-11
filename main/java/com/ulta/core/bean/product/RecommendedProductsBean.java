package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.util.Utility;

public class RecommendedProductsBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1422411556600771800L;
	private static int IMAGE_HEIGHT = 150;
	private static int IMAGE_WIDTH = 150;

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
	private String powerReviewRating;
	private String badgeName;

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	public String getPowerReviewRating() {
		return powerReviewRating;
	}

	public void setPowerReviewRating(String powerReviewRating) {
		this.powerReviewRating = powerReviewRating;
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

	public void setImage_url(String image_url) {
		if (Utility.stringNullEmptyValidator(image_url)) {
			String url = Utility.modifyImageResolution(image_url, IMAGE_HEIGHT,
					IMAGE_WIDTH);
			this.productImageUrl = url;
		} else {
			this.productImageUrl = image_url;
		}

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
