package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;


public class ReviewBean  extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3758768495241012389L;
	private List<DataBean> data;

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

}
