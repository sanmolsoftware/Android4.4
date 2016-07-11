package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.util.caching.UltaDataCache;

public class AppConfigurablesBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6023670832291720109L;

	private String Encrpytion_Key;
	
	private String isDyantraceEnabled;
	
	private String testStream;
	

	public String getTestStream() {
		return testStream;
	}

	public void setTestStream(String testStream) {
		this.testStream = testStream;

	}

	public String getEncrpytion_Key() {
		return Encrpytion_Key;
	}

	public void setEncrpytion_Key(String encrpytion_Key) {
		Encrpytion_Key = encrpytion_Key;
	}

	public String getIsDyantraceEnabled() {
		return isDyantraceEnabled;
	}

	public void setIsDyantraceEnabled(String isDyantraceEnabled) {
		this.isDyantraceEnabled = isDyantraceEnabled;
	}
	
	

}
