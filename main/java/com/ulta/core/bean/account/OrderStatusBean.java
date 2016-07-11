package com.ulta.core.bean.account;


import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class OrderStatusBean extends UltaBean {
	private OrderBean atgResponse;

	public OrderBean getAtgResponse() {
		return atgResponse;
	}

	public void setAtgResponse(OrderBean atgResponse) {
		this.atgResponse = atgResponse;
	}

}
