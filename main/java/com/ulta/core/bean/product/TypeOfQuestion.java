package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class TypeOfQuestion extends UltaBean
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1556614202277378388L;
	private List<String> Business_Oriented;
	private List<String> Product_Oriented;
	private String qnSubType1;
	private String qnSubType2;
	private String qnType1;
	private String qnType2;
	public List<String> getBusiness_Oriented() {
		return Business_Oriented;
	}
	public void setBusiness_Oriented(List<String> business_Oriented) {
		Business_Oriented = business_Oriented;
	}
	public List<String> getProduct_Oriented() {
		return Product_Oriented;
	}
	public void setProduct_Oriented(List<String> product_Oriented) {
		Product_Oriented = product_Oriented;
	}
	public String getQnSubType1() {
		return qnSubType1;
	}
	public void setQnSubType1(String qnSubType1) {
		this.qnSubType1 = qnSubType1;
	}
	public String getQnSubType2() {
		return qnSubType2;
	}
	public void setQnSubType2(String qnSubType2) {
		this.qnSubType2 = qnSubType2;
	}
	public String getQnType1() {
		return qnType1;
	}
	public void setQnType1(String qnType1) {
		this.qnType1 = qnType1;
	}
	public String getQnType2() {
		return qnType2;
	}
	public void setQnType2(String qnType2) {
		this.qnType2 = qnType2;
	}
}
