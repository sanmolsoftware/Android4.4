package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class NewBeautyPreferencesBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3251275314646962272L;
	
	private BeautyPreferenceCategoryList newBeautyPreferences;

	public BeautyPreferenceCategoryList getNewBeautyPreferences() {
		return newBeautyPreferences;
	}

	public void setNewBeautyPreferences(
			BeautyPreferenceCategoryList newBeautyPreferences) {
		this.newBeautyPreferences = newBeautyPreferences;
	}

}
