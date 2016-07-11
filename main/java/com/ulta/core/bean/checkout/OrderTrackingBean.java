package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

@SuppressWarnings("serial")
public class OrderTrackingBean extends UltaBean {
	private String trackingNumber;
	private String trackingURL;

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getTrackingURL() {
		return trackingURL;
	}

	public void setTrackingURL(String trackingURL) {
		this.trackingURL = trackingURL;
	}

}
