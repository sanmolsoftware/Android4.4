package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class CheckoutElectronicShippingGroupBean extends UltaBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -820753951560645550L;
	private String currencyCode;
	private String emailAddress;
	private String id;
	private String message;
	private String shippingGroupClassType;
	private String shippingMethod;
	private String rawShipping;
	private double amount;
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getShippingGroupClassType() {
		return shippingGroupClassType;
	}
	public void setShippingGroupClassType(String shippingGroupClassType) {
		this.shippingGroupClassType = shippingGroupClassType;
	}
	public String getShippingMethod() {
		return shippingMethod;
	}
	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}
	public String getRawShipping() {
		return rawShipping;
	}
	public void setRawShipping(String rawShipping) {
		this.rawShipping = rawShipping;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	



}
