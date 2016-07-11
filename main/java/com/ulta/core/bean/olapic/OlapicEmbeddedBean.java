package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class OlapicEmbeddedBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5309072864101290835L;
	private List<OlapicMediaBean> media;

	public List<OlapicMediaBean> getMedia() {
		return media;
	}

	public void setMedia(List<OlapicMediaBean> media) {
		this.media = media;
	}

}
