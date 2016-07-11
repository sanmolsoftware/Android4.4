/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;



/**
 * The Class WishListProductBean.
 */
public class WishListProductBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7181729775527180330L;

	/** The item id. */
	private String itemId;
	
	/** The image url. */
	private String imageUrl;
	
	/** The item name. */
	private String itemName;
	
	/** The brand name. */
	private String brandName;
	
	/** The selected option. */
	private String selectedOption;
	
	/** The price. */
	private String price;

	/**
	 * Gets the item id.
	 *
	 * @return the item id
	 */
	public String getItemId() {
		return itemId;
	}
	
	/**
	 * Sets the item id.
	 *
	 * @param itemId the new item id
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * Gets the image url.
	 *
	 * @return the image url
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	
	/**
	 * Sets the image url.
	 *
	 * @param imageUrl the new image url
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	/**
	 * Gets the item name.
	 *
	 * @return the item name
	 */
	public String getItemName() {
		return itemName;
	}
	
	/**
	 * Sets the item name.
	 *
	 * @param itemName the new item name
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	 * Gets the selected option.
	 *
	 * @return the selected option
	 */
	public String getSelectedOption() {
		return selectedOption;
	}
	
	/**
	 * Sets the selected option.
	 *
	 * @param selectedOption the new selected option
	 */
	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}
	
	/**
	 * Gets the price.
	 *
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}
	
	/**
	 * Sets the price.
	 *
	 * @param price the new price
	 */
	public void setPrice(String price) {
		this.price = price;
	}
}
