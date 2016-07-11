/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class ProductsInListBean.
 */
public class ProductsInListBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9122173000364673465L;

	/** The creation date. */
	private String creationDate;
	
	/** The display name. */
	private String displayName;
	
	/** The brand name. */
	private String brandName;
	
	/** The id. */
	private String id;
	
	/** The is gwp. */
	private int isGWP;

	/** The small image url. */
	private String smallImageUrl;
	
	/** The list price from. */
	private double listPriceFrom;
	
	/** The list price to. */
	private double listPriceTo;
	
	/** The sale price from. */
	private double salePriceFrom;
	
	/** The sale price to. */
	private double salePriceTo;
	
	/** The has skus on sale. */
	private boolean hasSkusOnSale;
	
	/** The reviews. */
	private double reviews;
	
	/** The rating. */
	private double rating;
	private String offerDesc;
	private String offerType;
	private String badgeImgURL;
	private String badgeName;
	public String getBadgeImgURL() {
		return badgeImgURL;
	}

	public void setBadgeImgURL(String badgeImgURL) {
		this.badgeImgURL = badgeImgURL;
	}

	public String getBadgeName() {
		return badgeName;
	}

	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

	private String giftUrl;


	public String getGiftUrl() {
		return giftUrl;
	}

	public void setGiftUrl(String giftUrl) {
		this.giftUrl = giftUrl;
	}

	public String getOfferDesc() {
		return offerDesc;
	}

	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}

	/**
	 * Gets the reviews.
	 *
	 * @return the reviews
	 */
	public double getReviews() {
		return reviews;
	}

	/**
	 * Sets the reviews.
	 *
	 * @param d the new reviews
	 */
	public void setReviews(double d) {
		this.reviews = d;
	}

	/**
	 * Gets the rating.
	 *
	 * @return the rating
	 */
	public double getRating() {
		return rating;
	}

	/**
	 * Sets the rating.
	 *
	 * @param rating the new rating
	 */
	public void setRating(double rating) {
		this.rating = rating;
	}

	/**
	 * Gets the brand name.
	 *
	 * @return the brand name
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * Sets the brand name.
	 *
	 * @param brandName the new brand name
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * Gets the list price from.
	 *
	 * @return the list price from
	 */
	public double getListPriceFrom() {
		return listPriceFrom;
	}

	/**
	 * Sets the list price from.
	 *
	 * @param listPriceFrom the new list price from
	 */
	public void setListPriceFrom(double listPriceFrom) {
		this.listPriceFrom = listPriceFrom;
	}

	/**
	 * Gets the list price to.
	 *
	 * @return the list price to
	 */
	public double getListPriceTo() {
		return listPriceTo;
	}

	/**
	 * Sets the list price to.
	 *
	 * @param listPriceTo the new list price to
	 */
	public void setListPriceTo(double listPriceTo) {
		this.listPriceTo = listPriceTo;
	}

	/**
	 * Gets the sale price from.
	 *
	 * @return the sale price from
	 */
	public double getSalePriceFrom() {
		return salePriceFrom;
	}

	/**
	 * Sets the sale price from.
	 *
	 * @param salePriceFrom the new sale price from
	 */
	public void setSalePriceFrom(double salePriceFrom) {
		this.salePriceFrom = salePriceFrom;
	}

	/**
	 * Gets the sale price to.
	 *
	 * @return the sale price to
	 */
	public double getSalePriceTo() {
		return salePriceTo;
	}

	/**
	 * Sets the sale price to.
	 *
	 * @param salePriceTo the new sale price to
	 */
	public void setSalePriceTo(double salePriceTo) {
		this.salePriceTo = salePriceTo;
	}

	/**
	 * Checks if is checks for skus on sale.
	 *
	 * @return true, if is checks for skus on sale
	 */
	public boolean isHasSkusOnSale() {
		return hasSkusOnSale;
	}

	/**
	 * Sets the checks for skus on sale.
	 *
	 * @param hasSkusOnSale the new checks for skus on sale
	 */
	public void setHasSkusOnSale(boolean hasSkusOnSale) {
		this.hasSkusOnSale = hasSkusOnSale;
	}

	
	/**
	 * Gets the creation date.
	 *
	 * @return the creationDate
	 */
	public String getCreationDate() {
		return creationDate;
	}
	
	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	/**
	 * Gets the display name.
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets the display name.
	 *
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	/**
	 * Gets the small image url.
	 *
	 * @return the smallImageUrl
	 */
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	
	/**
	 * Sets the small image url.
	 *
	 * @param smallImageUrl the isGWP to smallImageUrl
	 */
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	
	/**
	 * Gets the checks if is gwp.
	 *
	 * @return the isGWP
	 */
	public int getIsGWP() {
		return isGWP;
	}
	
	/**
	 * Sets the checks if is gwp.
	 *
	 * @param isGWP the isGWP to set
	 */
	public void setIsGWP(int isGWP) {
		this.isGWP = isGWP;
	}	
}
