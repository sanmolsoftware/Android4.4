/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;



import com.ulta.core.bean.UltaBean;



/**
 * The Class ComponentBean.
 */
@SuppressWarnings("serial")
public class ComponentBean extends UltaBean{
	
	/** The cart. */
	private CartBean cart;
	
	/** The free gifts. */
	private FreeGiftBean freeGifts;
	
	//3.2Release
	private RedeemLevelPointsBean redeemLevels;
	
	//3.3 Release
	private String tokenId;
	/**
	 * Gets the cart.
	 *
	 * @return the cart
	 */
	public CartBean getCart() {
		return cart;
	}
	
	/**
	 * Sets the cart.
	 *
	 * @param cart the cart to set
	 */
	public void setCart(CartBean cart) {
		this.cart = cart;
	}

	public RedeemLevelPointsBean getRedeemLevels() {
		return redeemLevels;
	}

	public void setRedeemLevels(RedeemLevelPointsBean redeemLevels) {
		this.redeemLevels = redeemLevels;
	}

	
	
	/**
	 * Gets the free gifts.
	 *
	 * @return the freeGifts
	 */
	public FreeGiftBean getFreeGifts() {
		return freeGifts;
	}
	
	/**
	 * Sets the free gifts.
	 *
	 * @param freeGifts the freeGifts to set
	 */
	public void setFreeGifts(FreeGiftBean freeGifts) {
		this.freeGifts = freeGifts;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	
	
	
	
	
}
