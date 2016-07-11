/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;



/**
 * The Class WishListUserBean.
 */
public class WishListUserBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7634832099708144327L;

	/** The wish list name. */
	private String wishListName;
	
	/** The wish list id. */
	private int wishListId;
	
	/**
	 * Gets the wish list name.
	 *
	 * @return the wish list name
	 */
	public String getWishListName() {
		return wishListName;
	}
	
	/**
	 * Sets the wish list name.
	 *
	 * @param wishListName the new wish list name
	 */
	public void setWishListName(String wishListName) {
		this.wishListName = wishListName;
	}
	
	/**
	 * Gets the wish list id.
	 *
	 * @return the wish list id
	 */
	public int getWishListId() {
		return wishListId;
	}
	
	/**
	 * Sets the wish list id.
	 *
	 * @param wishListId the new wish list id
	 */
	public void setWishListId(int wishListId) {
		this.wishListId = wishListId;
	}
}
