package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class BuyMoreSaveMoreBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4134341538069715920L;
	
	
	private List<BuyMoreSaveMoreAtgResponseBean> atgResponse;


	public List<BuyMoreSaveMoreAtgResponseBean> getAtgResponse() {
		return atgResponse;
	}


	public void setAtgResponse(List<BuyMoreSaveMoreAtgResponseBean> atgResponse) {
		this.atgResponse = atgResponse;
	}
	
	

}
