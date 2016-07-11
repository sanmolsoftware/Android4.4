package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

@SuppressWarnings("serial")
public class BrandsBean extends UltaBean {
	private List<BrandBean> atgResponse;

	public List<BrandBean> getAtgResponse() {
		return atgResponse;
	}

	public void setAtgResponse(List<BrandBean> atgResponse) {
		this.atgResponse = atgResponse;
	}

}
