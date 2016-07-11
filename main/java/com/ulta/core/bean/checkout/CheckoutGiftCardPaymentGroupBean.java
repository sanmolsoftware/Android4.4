package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class CheckoutGiftCardPaymentGroupBean extends UltaBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5827882868079074017L;
	/** The amount. */
	private double amount;
	/** The payment method. */
	private String paymentMethod;
	private String giftCardNumber;
	private String insufficientGCBalanceMessage;
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public void setGiftCardNumber(String giftCardNumber) {
		this.giftCardNumber = giftCardNumber;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public String getGiftCardNumber() {
		return giftCardNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setInsufficientGCBalanceMessage(
			String insufficientGCBalanceMessage) {
		this.insufficientGCBalanceMessage = insufficientGCBalanceMessage;
	}
	public String getInsufficientGCBalanceMessage() {
		return insufficientGCBalanceMessage;
	}
}
