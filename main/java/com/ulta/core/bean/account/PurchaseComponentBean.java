package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class PurchaseComponentBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3966806733516000118L;
	private String beautyClubNumber;

	public String getBeautyClubNumber() {
		return beautyClubNumber;
	}

	public void setBeautyClubNumber(String beautyClubNumber) {
		this.beautyClubNumber = beautyClubNumber;
	}

	private PurchaseHistoryBean purchaseHistory;

	public void setPurchaseHistory(PurchaseHistoryBean purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}

	public PurchaseHistoryBean getPurchaseHistory() {
		return purchaseHistory;
	}
}
