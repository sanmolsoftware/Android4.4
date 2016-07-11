package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class StoreFilterBean extends UltaBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3393540463175123263L;
	private List<StoreBrandFiler> atgResponse;

	public List<StoreBrandFiler> getAtgResponse() {
		return atgResponse;
	}

	public void setAtgResponse(List<StoreBrandFiler> atgResponse) {
		this.atgResponse = atgResponse;
	}
}
