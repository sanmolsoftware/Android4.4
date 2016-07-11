package com.ulta.core.bean.olapic;


import com.ulta.core.bean.UltaBean;

public class OlapicImageDetailsDataBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5136638859960885900L;
	private OlapicImageDetailsEmbeddedBean _embedded;

	public OlapicImageDetailsEmbeddedBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicImageDetailsEmbeddedBean _embedded) {
		this._embedded = _embedded;
	}

}
