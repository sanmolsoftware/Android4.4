package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class QuestionBean extends UltaBean
{
	private String page_id;
	private String merchant_id;
	private String num_questions;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2614207505871791678L;

	public String getNumberOfQuestions() {
		return num_questions;
	}

	public void setNumberOfQuestions(String num_questions) {
		this.num_questions = num_questions;
	}

	public String getMerchantId() {
		return merchant_id;
	}

	public void setMerchantId(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getPageId() {
		return page_id;
	}

	public void setPageId(String page_id) {
		this.page_id = page_id;
	}
}
