package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class PurchaseHistoryBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9080471394266024473L;
	private List <TransactionDetailsBean> transactionDetails;

	public void setTransactionDetails(List <TransactionDetailsBean> transactionDetails) {
		this.transactionDetails = transactionDetails;
	}

	public List <TransactionDetailsBean> getTransactionDetails() {
		return transactionDetails;
	}
	
}
