package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.OrderTrackingBean;

import java.util.List;

@SuppressWarnings("serial")
public class OrderBean extends UltaBean {
	
	private String errorMessage;
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private String id;
	private String lastModifiedDate;
	private String state;
	private double total;
	private List<OrderTrackingBean> trackingInfoList;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<OrderTrackingBean> getTrackingInfoList() {
		return trackingInfoList;
	}

	public void setTrackingInfoList(List<OrderTrackingBean> trackingInfoList) {
		this.trackingInfoList = trackingInfoList;
	}
}
