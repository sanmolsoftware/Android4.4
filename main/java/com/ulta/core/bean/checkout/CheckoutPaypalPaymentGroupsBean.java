package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class CheckoutPaypalPaymentGroupsBean extends UltaBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3875096665001286945L;
	private String amount;
    private String amountAuthorized;
    private String  amountCredited;
    private String amountDebited;
    private String currencyCode;
    private String id;
    private String paymentMethod;
    private int state;
    //3.3 release
    private String emailId;
    private String token;
    private String transactionId;
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
	public String getAmountCredited() {
		return amountCredited;
	}
	public void setAmountCredited(String amountCredited) {
		this.amountCredited = amountCredited;
	}
	public String getAmountDebited() {
		return amountDebited;
	}
	public void setAmountDebited(String amountDebited) {
		this.amountDebited = amountDebited;
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
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
