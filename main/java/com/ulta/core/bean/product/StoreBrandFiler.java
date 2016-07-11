package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class StoreBrandFiler extends UltaBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8708599757754170131L;
	private String amenityId;
	private String serviceType;
	
	public String getAmenityId() {
		return amenityId;
	}
	public void setAmenityId(String amenityId) {
		this.amenityId = amenityId;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
}
