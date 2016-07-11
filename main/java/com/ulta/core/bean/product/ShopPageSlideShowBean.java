package com.ulta.core.bean.product;


import com.ulta.core.bean.UltaBean;

public class ShopPageSlideShowBean extends UltaBean {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1728818209386425797L;

	private String altText;
	private String name;
	private String path;
	private String serviceName;
	private String serviceParameters;
	boolean isActive;
	private String BrandId;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String brandId) {
		BrandId = brandId;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getAltText() {
		return altText;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceParameters(String serviceParameters) {
		this.serviceParameters = serviceParameters;
	}

	public String getServiceParameters() {
		return serviceParameters;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
