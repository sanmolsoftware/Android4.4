package com.ulta.core.bean.store;

import com.ulta.core.bean.UltaBean;

public class AmenitiesDetailsBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8475845535414167450L;
	private String id;
	private String imageName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
