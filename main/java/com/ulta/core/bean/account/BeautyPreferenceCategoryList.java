package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class BeautyPreferenceCategoryList extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6001689522300556858L;
	
	
	private List<NewPrefValuesBean> newPrefValues;


	public List<NewPrefValuesBean> getNewPrefValues() {
		return newPrefValues;
	}


	public void setNewPrefValues(List<NewPrefValuesBean> newPrefValues) {
		this.newPrefValues = newPrefValues;
	}

}
