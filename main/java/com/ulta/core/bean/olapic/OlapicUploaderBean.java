package com.ulta.core.bean.olapic;

import com.ulta.core.bean.UltaBean;

public class OlapicUploaderBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1418485717780095194L;

	private String name;

	private String avatar_url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar_url() {
		return avatar_url;
	}

	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}

}
