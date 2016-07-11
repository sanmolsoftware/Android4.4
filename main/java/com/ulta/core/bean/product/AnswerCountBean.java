package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class AnswerCountBean extends UltaBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3808242442059399572L;
	private List<AnswerBean> count;
	public List<AnswerBean> getCount() {
		return count;
	}
	public void setCount(List<AnswerBean> count) {
		this.count = count;
	}
}
