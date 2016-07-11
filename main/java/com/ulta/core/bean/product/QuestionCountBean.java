package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class QuestionCountBean extends UltaBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2614207505871791678L;
	private List<QuestionBean> count;
	public List<QuestionBean> getCount() {
		return count;
	}
	public void setCount(List<QuestionBean> count) {
		this.count = count;
	}
}
