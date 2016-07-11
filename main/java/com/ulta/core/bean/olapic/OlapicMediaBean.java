package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicMediaBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6940901344962161812L;

	private OlapicImagesBean images;
	
	private OlapicEmbeddedDetailsBean _embedded;
	
	private String caption;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public OlapicEmbeddedDetailsBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicEmbeddedDetailsBean _embedded) {
		this._embedded = _embedded;
	}

	public OlapicImagesBean getImages() {
		return images;
	}

	public void setImages(OlapicImagesBean images) {
		this.images = images;
	}

}
