package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class QuestionsListBean extends UltaBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2749834870856829151L;
	private List<QuestionDetailsBean> data;
	public List<QuestionDetailsBean> getData() {
		return data;
	}
	public void setData(List<QuestionDetailsBean> data) {
		this.data = data;
	}
}
