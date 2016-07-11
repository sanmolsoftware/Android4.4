package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9005901329675589751L;

	private OlapicDataBean data;
	
	private OlapicMetadataBean metadata;

	public OlapicMetadataBean getMetadata() {
		return metadata;
	}

	public void setMetadata(OlapicMetadataBean metadata) {
		this.metadata = metadata;
	}

	public OlapicDataBean getData() {
		return data;
	}

	public void setData(OlapicDataBean data) {
		this.data = data;
	}

}
