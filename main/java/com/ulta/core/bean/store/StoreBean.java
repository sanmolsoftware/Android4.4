package com.ulta.core.bean.store;

import com.ulta.core.bean.UltaBean;

import java.util.List;

@SuppressWarnings("serial")
public class StoreBean extends UltaBean {
	private List<StoreDetailBean> atgResponse;

	public List<StoreDetailBean> getStores() {
		return atgResponse;
	}

	public void setStores(List<StoreDetailBean> stores) {
		this.atgResponse = stores;
	}

}
