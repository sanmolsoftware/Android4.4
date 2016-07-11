package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class OrderHistoryBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2376521203228614344L;
	private List<OrderBean> completeOrderHistory;
	private int numberOfOrder; 

	public List<OrderBean> getCompleteOrderHistory() {
		return completeOrderHistory;
	}

	public void setCompleteOrderHistory(List<OrderBean> completeOrderHistory) {
		this.completeOrderHistory = completeOrderHistory;
	}

	public int getNumberOfOrder() {
		return numberOfOrder;
	}

	public void setNumberOfOrder(int numberOfOrder) {
		this.numberOfOrder = numberOfOrder;
	}
}
