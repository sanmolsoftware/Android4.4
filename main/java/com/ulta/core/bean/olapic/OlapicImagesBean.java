package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicImagesBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -927869848542849066L;

	private String thumbnail;

	private String mobile;
	
	private String original;

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
