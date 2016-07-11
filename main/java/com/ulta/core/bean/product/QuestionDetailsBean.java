package com.ulta.core.bean.product;

import java.util.List;


public class QuestionDetailsBean
{
	private int merchant_group_id;
	public int getMerchant_group_id() {
		return merchant_group_id;
	}
	public void setMerchant_group_id(int merchant_group_id) {
		this.merchant_group_id = merchant_group_id;
	}
	public String getQ_merchant_user_id() {
		return q_merchant_user_id;
	}
	public void setQ_merchant_user_id(String q_merchant_user_id) {
		this.q_merchant_user_id = q_merchant_user_id;
	}
	public int getProvider_id() {
		return provider_id;
	}
	public void setProvider_id(int provider_id) {
		this.provider_id = provider_id;
	}
	public int getQ_answer_count() {
		return q_answer_count;
	}
	public void setQ_answer_count(int q_answer_count) {
		this.q_answer_count = q_answer_count;
	}
	public int getLocale_id() {
		return locale_id;
	}
	public void setLocale_id(int locale_id) {
		this.locale_id = locale_id;
	}
	public String getQ_email() {
		return q_email;
	}
	public void setQ_email(String q_email) {
		this.q_email = q_email;
	}
	public String getQ_name() {
		return q_name;
	}
	public void setQ_name(String q_name) {
		this.q_name = q_name;
	}
	public int getQ_expert_answer_count() {
		return q_expert_answer_count;
	}
	public void setQ_expert_answer_count(int q_expert_answer_count) {
		this.q_expert_answer_count = q_expert_answer_count;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getQ_location() {
		return q_location;
	}
	public void setQ_location(String q_location) {
		this.q_location = q_location;
	}
	public String getPage_id() {
		return page_id;
	}
	public void setPage_id(String page_id) {
		this.page_id = page_id;
	}
	public int getMerchant_id() {
		return merchant_id;
	}
	public void setMerchant_id(int merchant_id) {
		this.merchant_id = merchant_id;
	}
	public String getQ_created_date() {
		return q_created_date;
	}
	public void setQ_created_date(String q_created_date) {
		this.q_created_date = q_created_date;
	}
	public String getQ_text() {
		return q_text;
	}
	public void setQ_text(String q_text) {
		this.q_text = q_text;
	}
	public List<AnswersDeatilsBean> getAnswers() {
		return answers;
	}
	public void setAnswers(List<AnswersDeatilsBean> answers) {
		this.answers = answers;
	}
	private String q_merchant_user_id;
	private int provider_id;
	private int q_answer_count;
	private int locale_id;
	private String q_email;
	private String q_name;
	private int q_expert_answer_count;
	private String variant;
	private String q_location;
	private String page_id;
	private int merchant_id;
	private String q_created_date;
	private String q_text;
	private List<AnswersDeatilsBean> answers;
	
}

