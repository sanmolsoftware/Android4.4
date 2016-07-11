package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class OlapicImageDetailsEmbeddedBean extends UltaBean{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5681136829728724644L;
	private List<OlapicImageDetailStreamBean> stream;


	public List<OlapicImageDetailStreamBean> getStream() {
		return stream;
	}

	public void setStream(List<OlapicImageDetailStreamBean> stream) {
		this.stream = stream;
	}


}
