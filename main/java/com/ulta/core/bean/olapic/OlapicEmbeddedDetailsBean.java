package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicEmbeddedDetailsBean extends UltaBean{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2479365599977460546L;

	private OlapicUploaderBean uploader;
	
	private OlapicStreamsAllBean streamsall;

	public OlapicStreamsAllBean getStreams() {
		return streamsall;
	}

	public void setStreams(OlapicStreamsAllBean streamsall) {
		this.streamsall = streamsall;
	}

	public OlapicUploaderBean getUploader() {
		return uploader;
	}

	public void setUploader(OlapicUploaderBean uploader) {
		this.uploader = uploader;
	}
	
	

}
