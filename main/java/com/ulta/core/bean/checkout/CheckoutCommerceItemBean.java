/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;



/**
 * The Class CheckoutCommerceItemBean.
 */
public class CheckoutCommerceItemBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3592942069831544020L;

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
	
	/** The gift wrap eligible. */
	private Boolean	giftWrapEligible;
	
	/** The hazmat code. */
	private String hazmatCode;
	
	/** The id. */
	private String id;
	
	/** The is ship separately. */
	private String isShipSeparately;
	
	/** The list price. */
	private double listPrice;
	
	/** The not coupon eligible. */
	private boolean notCouponEligible;
	
	/** The offer desc. */
	private String offerDesc;
	
	/** The product id. */
	private String productId;
	
	/** The promotion id. */
	private String promotionId;
	
	/** The quantity. */
	private String quantity;
	
	/** The relationship id. */
	private String relationshipId;
	
	/** The small image url. */
	private String smallImageUrl;
	private String isElectronicGiftCard;
	private String isGiftCard;
	
	private boolean isGWP;
	
	private boolean isGiftWrapItem;
	
	public boolean isGiftWrapItem() {
		return isGiftWrapItem;
	}

	public void setGiftWrapItem(boolean isGiftWrapItem) {
		this.isGiftWrapItem = isGiftWrapItem;
	}

	public boolean isGWP() {
		return isGWP;
	}

	public void setGWP(boolean isGWP) {
		this.isGWP = isGWP;
	}

private String isFreeSample;
	
	public String getIsFreeSample() {
	return isFreeSample;
}

public void setIsFreeSample(String isFreeSample) {
	this.isFreeSample = isFreeSample;
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
	 * @param amount the new amount
	 */
	public void setAmount(double amount) {
		this.amount = amount;
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
	 * Gets the catalog ref id.
	 *
	 * @return the catalog ref id
	 */
	public String getCatalogRefId() {
		return catalogRefId;
	}
	
	/**
	 * Sets the catalog ref id.
	 *
	 * @param catalogRefId the new catalog ref id
	 */
	public void setCatalogRefId(String catalogRefId) {
		this.catalogRefId = catalogRefId;
	}
	
	/**
	 * Gets the currency code.
	 *
	 * @return the currency code
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}
	
	/**
	 * Sets the currency code.
	 *
	 * @param currencyCode the new currency code
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Gets the gift wrap eligible.
	 *
	 * @return the gift wrap eligible
	 */
	public Boolean getGiftWrapEligible() {
		return giftWrapEligible;
	}
	
	/**
	 * Sets the gift wrap eligible.
	 *
	 * @param giftWrapEligible the new gift wrap eligible
	 */
	public void setGiftWrapEligible(Boolean giftWrapEligible) {
		this.giftWrapEligible = giftWrapEligible;
	}
	
	/**
	 * Gets the hazmat code.
	 *
	 * @return the hazmat code
	 */
	public String getHazmatCode() {
		return hazmatCode;
	}
	
	/**
	 * Sets the hazmat code.
	 *
	 * @param hazmatCode the new hazmat code
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
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the checks if is ship separately.
	 *
	 * @return the checks if is ship separately
	 */
	public String getIsShipSeparately() {
		return isShipSeparately;
	}
	
	/**
	 * Sets the checks if is ship separately.
	 *
	 * @param isShipSeparately the new checks if is ship separately
	 */
	public void setIsShipSeparately(String isShipSeparately) {
		this.isShipSeparately = isShipSeparately;
	}
	
	/**
	 * Gets the list price.
	 *
	 * @return the list price
	 */
	public double getListPrice() {
		return listPrice;
	}
	
	/**
	 * Sets the list price.
	 *
	 * @param listPrice the new list price
	 */
	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}
	
	/**
	 * Checks if is not coupon eligible.
	 *
	 * @return true, if is not coupon eligible
	 */
	public boolean isNotCouponEligible() {
		return notCouponEligible;
	}
	
	/**
	 * Sets the not coupon eligible.
	 *
	 * @param notCouponEligible the new not coupon eligible
	 */
	public void setNotCouponEligible(boolean notCouponEligible) {
		this.notCouponEligible = notCouponEligible;
	}
	
	/**
	 * Gets the offer desc.
	 *
	 * @return the offer desc
	 */
	public String getOfferDesc() {
		return offerDesc;
	}
	
	/**
	 * Sets the offer desc.
	 *
	 * @param offerDesc the new offer desc
	 */
	public void setOfferDesc(String offerDesc) {
		this.offerDesc = offerDesc;
	}
	
	/**
	 * Gets the product id.
	 *
	 * @return the product id
	 */
	public String getProductId() {
		return productId;
	}
	
	/**
	 * Sets the product id.
	 *
	 * @param productId the new product id
	 */
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	/**
	 * Gets the promotion id.
	 *
	 * @return the promotion id
	 */
	public String getPromotionId() {
		return promotionId;
	}
	
	/**
	 * Sets the promotion id.
	 *
	 * @param promotionId the new promotion id
	 */
	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}
	
	/**
	 * Gets the quantity.
	 *
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}
	
	/**
	 * Sets the quantity.
	 *
	 * @param quantity the new quantity
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Gets the relationship id.
	 *
	 * @return the relationship id
	 */
	public String getRelationshipId() {
		return relationshipId;
	}
	
	/**
	 * Sets the relationship id.
	 *
	 * @param relationshipId the new relationship id
	 */
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	/**
	 * Gets the small image url.
	 *
	 * @return the small image url
	 */
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	
	/**
	 * Sets the small image url.
	 *
	 * @param smallImageUrl the new small image url
	 */
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
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

}
