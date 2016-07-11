package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class GiftComponentBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4933833221743290209L;
	private String giftcardBalance;
	private String giftcardNumber;
	
	public void setGiftcardBalance(String giftcardBalance) {
		this.giftcardBalance = giftcardBalance;
	}
	
	public void setGiftcardNumber(String giftcardNumber) {
		this.giftcardNumber = giftcardNumber;
	}
	
	
	public String getGiftcardNumber() {
		return giftcardNumber;
	}
	
	public String getGiftcardBalance() {
		return giftcardBalance;
	}
}
