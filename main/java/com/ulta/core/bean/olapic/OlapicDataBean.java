package com.ulta.core.bean.olapic;



import com.ulta.core.bean.UltaBean;

public class OlapicDataBean extends UltaBean {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2102459024680511287L;

	private OlapicLinksBean _links;
	
	private OlapicEmbeddedBean _embedded;
	
	public OlapicEmbeddedBean get_embedded() {
		return _embedded;
	}

	public void set_embedded(OlapicEmbeddedBean _embedded) {
		this._embedded = _embedded;
	}

	public OlapicLinksBean get_links() {
		return _links;
	}

	public void set_links(OlapicLinksBean _links) {
		this._links = _links;
	}


}
