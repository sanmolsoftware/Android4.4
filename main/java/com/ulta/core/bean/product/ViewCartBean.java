/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;



/**
 * The Class ViewCartBean.
 */
public class ViewCartBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3097616385553738542L;
	/** The cart. */
	private CartBean cart;
	private FreeGiftBean freeGifts;
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
	/**
	 * @return the freeGifts
	 */
	public FreeGiftBean getFreeGifts() {
		return freeGifts;
	}
	/**
	 * @param freeGifts the freeGifts to set
	 */
	public void setFreeGifts(FreeGiftBean freeGifts) {
		this.freeGifts = freeGifts;
	}
	
}
