package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class AnswerBean extends UltaBean
{
	private String num_answers;
	private String page_id;
	private String merchant_id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 3808242442059399572L;
	public String getNumberOfAnswers() {
		return num_answers;
	}
	public void setNumberOfAnswers(String num_answers) {
		this.num_answers = num_answers;
	}
	public String getPageId() {
		return page_id;
	}
	public void setPageId(String page_id) {
		this.page_id = page_id;
	}
	public String getMerchantId() {
		return merchant_id;
	}
	public void setMerchantId(String merchant_id) {
		this.merchant_id = merchant_id;
	}

}
