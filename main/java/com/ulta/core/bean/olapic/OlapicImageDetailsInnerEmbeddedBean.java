package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicImageDetailsInnerEmbeddedBean extends UltaBean {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2493463965722692556L;
	private OlapicImageDetailsBaseImageBean base_image;
	
	
	public OlapicImageDetailsBaseImageBean getBase_image() {
		return base_image;
	}

	public void setBase_image(OlapicImageDetailsBaseImageBean base_image) {
		this.base_image = base_image;
	}
 
}
