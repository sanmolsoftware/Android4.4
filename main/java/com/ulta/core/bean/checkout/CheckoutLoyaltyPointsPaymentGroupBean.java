package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class CheckoutLoyaltyPointsPaymentGroupBean extends UltaBean{
	
	private String RedemptionType;
	private String amount;
	private String amountAuthorized;
	private String clubNumber;
	private String currencyCode;
	private String id;
	private String paymentMethod;
	private String redemptionPoints;
	private String state;
	
	
	public String getRedemptionType() {
		return RedemptionType;
	}
	public void setRedemptionType(String redemptionType) {
		RedemptionType = redemptionType;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmountAuthorized() {
		return amountAuthorized;
	}
	public void setAmountAuthorized(String amountAuthorized) {
		this.amountAuthorized = amountAuthorized;
	}
	public String getClubNumber() {
		return clubNumber;
	}
	public void setClubNumber(String clubNumber) {
		this.clubNumber = clubNumber;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getRedemptionPoints() {
		return redemptionPoints;
	}
	public void setRedemptionPoints(String redemptionPoints) {
		this.redemptionPoints = redemptionPoints;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	

}
