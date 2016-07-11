package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class BeautyPreferencebean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8107598702875912420L;
	private beautyPrefMapBean beautyPrefMap;
	private profilePrefMapBean profilePrefMap;
	public void setBeautyPrefMap(beautyPrefMapBean beautyPrefMap) {
		this.beautyPrefMap = beautyPrefMap;
	}
	public beautyPrefMapBean getBeautyPrefMap() {
		return beautyPrefMap;
	}
	public void setProfilePrefMap(profilePrefMapBean profilePrefMap) {
		this.profilePrefMap = profilePrefMap;
	}
	public profilePrefMapBean getProfilePrefMap() {
		return profilePrefMap;
	}
}
