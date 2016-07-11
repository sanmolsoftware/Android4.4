package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicImageDetailStreamBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6236872065633760126L;

	private String tag_based_key;
	
	private OlapicImageDetailsInnerEmbeddedBean _embedded;

	public OlapicImageDetailsInnerEmbeddedBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicImageDetailsInnerEmbeddedBean _embedded) {
		this._embedded = _embedded;
	}

	public String getTag_based_key() {
		return tag_based_key;
	}

	public void setTag_based_key(String tag_based_key) {
		this.tag_based_key = tag_based_key;
	}
	
	

}
