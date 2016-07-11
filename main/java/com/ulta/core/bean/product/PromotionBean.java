package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

@SuppressWarnings("serial")
public class PromotionBean extends UltaBean {
	
	private List<atgResponseListBean> atgResponse;

	public void setAtgResponseList(List<atgResponseListBean> atgResponseList) {
		this.atgResponse = atgResponseList;
	}

	public List<atgResponseListBean> getAtgResponseList() {
		return atgResponse;
	}
	
	

}
