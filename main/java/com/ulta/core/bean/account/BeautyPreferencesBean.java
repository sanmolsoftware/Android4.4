package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class BeautyPreferencesBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4595635623843527211L;
	private BeautyPreferencebean beautyPreferences;

	public void setBeautyPreferences(BeautyPreferencebean beautyPreferences) {
		this.beautyPreferences = beautyPreferences;
	}

	public BeautyPreferencebean getBeautyPreferences() {
		return beautyPreferences;
	}
}
