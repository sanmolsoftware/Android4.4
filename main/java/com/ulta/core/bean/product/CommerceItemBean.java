/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class CommerceItemBean.
 */
public class CommerceItemBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6422027056107016033L;
	private String emailAddress;
	private String message;
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private boolean outOfStock;
	/** The amount. */
	private double amount;
	
	/** The brand name. */
	private String brandName;
	
	/** The catalog ref id. */
	private String catalogRefId;
	
	/** The currency code. */
	private String currencyCode;
	
	/** The display name. */
	private String displayName;
	
	/** The feature type. */
	private String featureType;
	
	/** The is gift wrap eligible. */
	@SerializedName("giftWrapEligible")
	private boolean isGiftWrapEligible;
	
	/** The hazmat code. */
	private String hazmatCode;
	
	/** The id. */
	private String id;
	
	
	private String isElectronicGiftCard;

	private String isGiftCard;
	/** The is ship separately. */
	private boolean isShipSeparately;
	
	/** The list price. */
	private double listPrice;
	
	/** The sale price. */
	private String salePrice;
	
	/** The not coupon eligible. */
	private boolean notCouponEligible;
	
	/** The offer desc. */
	private String offerDesc;
	
	/** The product id. */
	private String productId;
	
	/** The promotion id. */
	private String promotionId;
	
	/** The quantity. */
	private int quantity;
	
	/** The relationship id. */
	private String relationshipId;
	
	/** The small image url. */
	private String smallImageUrl;
	
	private boolean isGWP;
	
	private String isFreeSample;
	/** The on sale. */
	private boolean onSale;
	
	private String isGiftWrapItem;
	
	public String getIsGiftWrapItem() {
		return isGiftWrapItem;
	}

	public void setIsGiftWrapItem(String isGiftWrapItem) {
		this.isGiftWrapItem = isGiftWrapItem;
	}

	private List<GWPBean> promotions;
	
	public boolean isOutOfStock() {
		return outOfStock;
	}

	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}

	
	public boolean isOnSale() {
		return onSale;
	}

	public void setOnSale(boolean onSale) {
		this.onSale = onSale;
	}

	public String getIsFreeSample() {
		return isFreeSample;
	}

	public void setIsFreeSample(String isFreeSample) {
		this.isFreeSample = isFreeSample;
	}

	public boolean isGWP() {
		return isGWP;
	}

	public void setGWP(boolean isGWP) {
		this.isGWP = isGWP;
	}

	private String maxQty;
	
	public String getMaxQty() {
		return maxQty;
	}

	public void setMaxQty(String maxQty) {
		this.maxQty = maxQty;
	}

	/**
	 * Gets the offer desc.
	 *
	 * @return the offerDesc
	 */
	public String getOfferDesc() {
		return offerDesc;
	}
	
	/**
	 * Sets the offer desc.
	 *
	 * @param offerDesc the offerDesc to set
	 */
	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}
	
	/**
	 * Gets the amount.
	 *
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}
	
	/**
	 * Sets the amount.
	 *
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	/**
	 * Gets the brand name.
	 *
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}
	
	/**
	 * Sets the brand name.
	 *
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	/**
	 * Gets the catalog ref id.
	 *
	 * @return the catalogRefId
	 */
	public String getCatalogRefId() {
		return catalogRefId;
	}
	
	/**
	 * Sets the catalog ref id.
	 *
	 * @param catalogRefId the catalogRefId to set
	 */
	public void setCatalogRefId(String catalogRefId) {
		this.catalogRefId = catalogRefId;
	}
	
	/**
	 * Gets the currency code.
	 *
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * Sets the currency code.
	 *
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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
	 * Gets the feature type.
	 *
	 * @return the featureType
	 */
	public String getFeatureType() {
		return featureType;
	}
	
	/**
	 * Sets the feature type.
	 *
	 * @param featureType the featureType to set
	 */
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	
	/**
	 * Checks if is gift wrap eligible.
	 *
	 * @return the isGiftWrapEligible
	 */
	public boolean isGiftWrapEligible() {
		return isGiftWrapEligible;
	}
	
	/**
	 * Sets the gift wrap eligible.
	 *
	 * @param isGiftWrapEligible the isGiftWrapEligible to set
	 */
	public void setGiftWrapEligible(boolean isGiftWrapEligible) {
		this.isGiftWrapEligible = isGiftWrapEligible;
	}
	
	/**
	 * Gets the hazmat code.
	 *
	 * @return the hazmatCode
	 */
	public String getHazmatCode() {
		return hazmatCode;
	}
	
	/**
	 * Sets the hazmat code.
	 *
	 * @param hazmatCode the hazmatCode to set
	 */
	public void setHazmatCode(String hazmatCode) {
		this.hazmatCode = hazmatCode;
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
	 * Checks if is ship separately.
	 *
	 * @return the isShipSeparately
	 */
	public boolean isShipSeparately() {
		return isShipSeparately;
	}
	
	/**
	 * Sets the ship separately.
	 *
	 * @param isShipSeparately the isShipSeparately to set
	 */
	public void setShipSeparately(boolean isShipSeparately) {
		this.isShipSeparately = isShipSeparately;
	}
	
	/**
	 * Gets the list price.
	 *
	 * @return the listPrice
	 */
	public double getListPrice() {
		return listPrice;
	}
	
	/**
	 * Sets the list price.
	 *
	 * @param listPrice the listPrice to set
	 */
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	
	/**
	 * Checks if is not coupon eligible.
	 *
	 * @return the notCouponEligible
	 */
	public boolean isNotCouponEligible() {
		return notCouponEligible;
	}
	
	/**
	 * Sets the not coupon eligible.
	 *
	 * @param notCouponEligible the notCouponEligible to set
	 */
	public void setNotCouponEligible(boolean notCouponEligible) {
		this.notCouponEligible = notCouponEligible;
	}
	
	/**
	 * Gets the product id.
	 *
	 * @return the productId
	 */
	public String getProductId() {
		return productId;
	}
	
	/**
	 * Sets the product id.
	 *
	 * @param productId the productId to set
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * Gets the promotion id.
	 *
	 * @return the promotionId
	 */
	public String getPromotionId() {
		return promotionId;
	}
	
	/**
	 * Sets the promotion id.
	 *
	 * @param promotionId the promotionId to set
	 */
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Sets the quantity.
	 *
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the relationship id.
	 *
	 * @return the relationshipId
	 */
	public String getRelationshipId() {
		return relationshipId;
	}
	
	/**
	 * Sets the relationship id.
	 *
	 * @param relationshipId the relationshipId to set
	 */
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
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
	 * @param smallImageUrl the smallImageUrl to set
	 */
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	public String getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}
	
	public String getIsElectronicGiftCard() {
		return isElectronicGiftCard;
	}
	public void setIsElectronicGiftCard(String isElectronicGiftCard) {
		this.isElectronicGiftCard = isElectronicGiftCard;
	}
	public void setIsGiftCard(String isGiftCard) {
		this.isGiftCard = isGiftCard;
	}
	public String getIsGiftCard() {
		return isGiftCard;
	}

	public void setPromotions(List<GWPBean> promotions) {
		this.promotions = promotions;
	}

	public List<GWPBean> getPromotions() {
		return promotions;
	}
}
