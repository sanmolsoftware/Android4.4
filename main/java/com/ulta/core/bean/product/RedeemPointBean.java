package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;
//3.2Release
@SuppressWarnings("serial")
public class RedeemPointBean extends UltaBean {
	
	private String points;
	private String value;
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
