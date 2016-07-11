package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class TransactionDetailsBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3580054831039304467L;
	private String pointBalance;
	private String storeName;
	private String transactionDate;
	private String transactionDescription;
	
	public String getPointBalance() {
		return pointBalance;
	}
	
	public void setPointBalance(String pointBalance) {
		this.pointBalance = pointBalance;
	}
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getTransactionDescription() {
		return transactionDescription;
	}
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

}
