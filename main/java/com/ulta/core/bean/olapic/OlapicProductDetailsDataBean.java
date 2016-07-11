package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicProductDetailsDataBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1094295444725111810L;
	private OlapicProductDetailsEmbeddedBean _embedded;

	public OlapicProductDetailsEmbeddedBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicProductDetailsEmbeddedBean _embedded) {
		this._embedded = _embedded;
	}
	
	

}
