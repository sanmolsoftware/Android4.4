/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */
package com.ulta.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;

import java.util.List;

/**
 * The Class ProductSkuBean.
 *
 * @author viva
 */
public class ProductSkuBean extends UltaBean {

    /**
     *
     */
    private static final long serialVersionUID = -559870447707852215L;

    /**
     * The display name.
     */
    private String displayName;

    /**
     * The is gift wrap eligible.
     */
    @SerializedName("giftWrapEligible")
    private boolean isGiftWrapEligible;

    /**
     * The featuretype.
     */
    private String featureType;

    /**
     * The hazmat code.
     */
    private String hazmatCode;

    /**
     * The id.
     */
    private String id;

    /**
     * The is coupon eligible.
     */
    private boolean isCouponEligible;

    private boolean inStoreOnly;

    private boolean isComingSoon;

    public boolean isComingSoon() {
        return isComingSoon;
    }

    public void setIsComingSoon(boolean isComingSoon) {
        this.isComingSoon = isComingSoon;
    }

    private boolean onlineOnly;

    public boolean isOnlineOnly() {
        return onlineOnly;
    }

    public void setOnlineOnly(boolean onlineOnly) {
        this.onlineOnly = onlineOnly;
    }

    /**
     * The is default sku.
     */
    private boolean isDefaultSku;

    private boolean isSelectedOne;

    /**
     * The is ship separately.
     */
    private boolean isShipSeparately;
    private List<BadgeListBean> badgeList;

    private List<ProductSkuGWPBean> gwpSkuList;

    public List<ProductSkuGWPBean> getGwpSkuList() {
        return gwpSkuList;
    }

    public void setGwpSkuList(List<ProductSkuGWPBean> gwpSkuList) {
        this.gwpSkuList = gwpSkuList;
    }

    public List<BadgeListBean> getBadgeList() {
        return badgeList;
    }

    public void setBadgeList(List<BadgeListBean> badgeList) {
        this.badgeList = badgeList;
    }

    public boolean isSelectedOne() {
        return isSelectedOne;
    }

    public void setSelectedOne(boolean isSelectedOne) {
        this.isSelectedOne = isSelectedOne;
    }

    /**
     * The large image url.
     */
    private String largeImageUrl;

    /**
     * The list price.
     */
    private double listPrice;

    /**
     * The on sale.
     */
    private boolean onSale;

    /**
     * The promotion id.
     */
    private String promotionId;

    /**
     * The sale price.
     */
    private double salePrice;

    /**
     * The small image url.
     */
    private String smallImageUrl;

    /**
     * The max qty.
     */
    private int maxQty;

    private String offerDesc;
    private String offerType;

    private boolean isFavSku;

    public boolean isFavSku() {
        return isFavSku;
    }

    public void setFavSku(boolean isFavSku) {
        this.isFavSku = isFavSku;
    }

    private String isElectronicGiftCard;
    //3.5 Release
    private int fisFlagVal;

    /**
     * The description.
     */
    private String description;

    /**
     * The ingredients.
     */
    private String ingredients;


    /**
     * The long description.
     */
    private String longDescription;


    /**
     * The directions.
     */
    private String directions;

    /**
     * The special instructions.
     */
    private String specialInstructions;

    /**
     * The warnings.
     */
    private String warnings;


    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getDirections() {
        return directions;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    /**
     * Gets the featuretype.
     *
     * @return the featuretype
     */
    public String getFeaturetype() {
        return featureType;
    }

    /**
     * Sets the featuretype.
     *
     * @param featuretype the featuretype to set
     */
    public void setFeaturetype(String featuretype) {
        this.featureType = featuretype;
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
     * Checks if is coupon eligible.
     *
     * @return the isCouponEligible
     */
    public boolean isCouponEligible() {
        return isCouponEligible;
    }

    /**
     * Sets the coupon eligible.
     *
     * @param isCouponEligible the isCouponEligible to set
     */
    public void setCouponEligible(boolean isCouponEligible) {
        this.isCouponEligible = isCouponEligible;
    }

    /**
     * Checks if is default sku.
     *
     * @return the isDefaultSku
     */
    public boolean isDefaultSku() {
        return isDefaultSku;
    }

    /**
     * Sets the default sku.
     *
     * @param isDefaultSku the isDefaultSku to set
     */
    public void setDefaultSku(boolean isDefaultSku) {
        this.isDefaultSku = isDefaultSku;
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
     * Gets the large image url.
     *
     * @return the largeImageUrl
     */
    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    /**
     * Sets the large image url.
     *
     * @param largeImageUrl the largeImageUrl to set
     */
    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
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
    public void setListPrice(int listPrice) {
        this.listPrice = listPrice;
    }

    /**
     * Checks if is on sale.
     *
     * @return the onSale
     */
    public boolean isOnSale() {
        return onSale;
    }

    /**
     * Sets the on sale.
     *
     * @param onSale the onSale to set
     */
    public void setOnSale(boolean onSale) {
        this.onSale = onSale;
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
     * Gets the sale price.
     *
     * @return the salePrice
     */
    public double getSalePrice() {
        return salePrice;
    }

    /**
     * Sets the sale price.
     *
     * @param salePrice the salePrice to set
     */
    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
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

    /**
     * Gets the max qty.
     *
     * @return the maxQty
     */
    public int getMaxQty() {
        return maxQty;
    }

    /**
     * Sets the max qty.
     *
     * @param maxQty the maxQty to set
     */
    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getIsElectronicGiftCard() {
        return isElectronicGiftCard;
    }

    public void setIsElectronicGiftCard(String isElectronicGiftCard) {
        this.isElectronicGiftCard = isElectronicGiftCard;
    }

    public void setInStoreOnly(boolean inStoreOnly) {
        this.inStoreOnly = inStoreOnly;
    }

    public boolean isInStoreOnly() {
        return inStoreOnly;
    }

    public int getFisFlagValue() {
        return fisFlagVal;
    }

    public void setFisFlagValue(int fisFlagValue) {
        this.fisFlagVal = fisFlagValue;
    }

}
