package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class OlapicProductDetailsEmbeddedBean extends UltaBean{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997016736313816090L;
	private List<OlapicProductDetailsMediaBean> media;

	public List<OlapicProductDetailsMediaBean> getMedia() {
		return media;
	}

	public void setMedia(List<OlapicProductDetailsMediaBean> media) {
		this.media = media;
	}
	
	
	

}
