/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class FreeGiftDetailBean.
 */
public class FreeGiftDetailBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7210570567336999083L;

	/** The display name. */
	private String displayName;
	
	/** The id. */
	private String id;
	
	/** The primary sku id. */
	private String featureType;
	private String productId;
	private String smallImageUrl;
	
	private boolean isAddedFreeGift;
	
	public boolean isAddedFreeGift() {
		return isAddedFreeGift;
	}
	public void setAddedFreeGift(boolean isAddedFreeGift) {
		this.isAddedFreeGift = isAddedFreeGift;
	}
	public String getSmallImageUrl() {
		return smallImageUrl;
	}
	public void setSmallImageUrl(String smallImageUrl) {
		this.smallImageUrl = smallImageUrl;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFeatureType() {
		return featureType;
	}
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
}
