package com.ulta.core.bean.favourites;

import com.ulta.core.bean.UltaBean;

public class FavouritesInListBean extends UltaBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6862592077969546666L;
	private String brandName;
	private String catalogRefId;
	private String displayName;
	private String featureType;

	private boolean isOutOfStock;
	private double listPrice;
	private boolean onSale;
	private boolean inStoreOnly;
	private boolean isComingSoon;

	public boolean isComingSoon() {
		return isComingSoon;
	}

	public void setComingSoon(boolean isComingSoon) {
		this.isComingSoon = isComingSoon;
	}

	private int isGWP;
	private boolean isProductExpired;
	private String productId;
	private String smallImageUrl;
	private double rating;
	private double ratingDecimal;
	private int reviews;
	private String promotionId;
	private String offerDesc;

	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	public String getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getRatingDecimal() {
		return ratingDecimal;
	}

	public void setRatingDecimal(double ratingDecimal) {
		this.ratingDecimal = ratingDecimal;
	}

	public int getReviews() {
		return reviews;
	}

	public void setReviews(int reviews) {
		this.reviews = reviews;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCatalogRefId() {
		return catalogRefId;
	}

	public void setCatalogRefId(String catalogRefId) {
		this.catalogRefId = catalogRefId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public boolean isOutOfStock() {
		return isOutOfStock;
	}

	public void setOutOfStock(boolean isOutOfStock) {
		this.isOutOfStock = isOutOfStock;
	}

	public double getListPrice() {
		return listPrice;
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSmallImageUrl() {
		return smallImageUrl;
	}

	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}

	public boolean isInStoreOnly() {
		return inStoreOnly;
	}

	public void setInStoreOnly(boolean inStoreOnly) {
		this.inStoreOnly = inStoreOnly;
	}

	public int getIsGWP() {
		return isGWP;
	}

	public void setIsGWP(int isGWP) {
		this.isGWP = isGWP;
	}

	public boolean isProductExpired() {
		return isProductExpired;
	}

	public void setProductExpired(boolean isProductExpired) {
		this.isProductExpired = isProductExpired;
	}

}
