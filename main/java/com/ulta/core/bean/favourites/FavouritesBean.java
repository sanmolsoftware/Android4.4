package com.ulta.core.bean.favourites;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class FavouritesBean extends UltaBean{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5645126802915856269L;

	private List<FavouritesInListBean> favItemDetails;
	
	private GiftListidBean giftlistId;
	
	private String totalNoOfProducts;

	public String getTotalNoOfProducts() {
		return totalNoOfProducts;
	}

	public void setTotalNoOfProducts(String totalNoOfProducts) {
		this.totalNoOfProducts = totalNoOfProducts;
	}

	public List<FavouritesInListBean> getFavItemDetails() {
		return favItemDetails;
	}

	public void setFavItemDetails(List<FavouritesInListBean> favItemDetails) {
		this.favItemDetails = favItemDetails;
	}

	public GiftListidBean getGiftlistId() {
		return giftlistId;
	}

	public void setGiftlistId(GiftListidBean giftlistId) {
		this.giftlistId = giftlistId;
	}	
	
	
}
