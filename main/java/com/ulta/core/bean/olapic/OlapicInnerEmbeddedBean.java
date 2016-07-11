package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicInnerEmbeddedBean extends UltaBean{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -139146587164618522L;
	private OlapicPDPUploaderBean uploader;

	public OlapicPDPUploaderBean getUploader() {
		return uploader;
	}

	public void setUploader(OlapicPDPUploaderBean uploader) {
		this.uploader = uploader;
	}
	


	
}
