package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class TaxonomyBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5158520902652563219L;
	private List<TaxonomyListBean> atgResponse;

	public List<TaxonomyListBean> getAtgResponse() {
		return atgResponse;
	}

	public void setAtgResponse(List<TaxonomyListBean> atgResponse) {
		this.atgResponse = atgResponse;
	}
}
