package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicProductDetailsMediaBean extends UltaBean{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7888733571730223090L;

	private OlapicInnerEmbeddedBean _embedded;
	
	private String caption;
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public OlapicInnerEmbeddedBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicInnerEmbeddedBean _embedded) {
		this._embedded = _embedded;
	}

	private OlapicProductDetailsImagesBean images;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public OlapicProductDetailsImagesBean getImages() {
		return images;
	}

	public void setImages(OlapicProductDetailsImagesBean images) {
		this.images = images;
	}
	
	

}
