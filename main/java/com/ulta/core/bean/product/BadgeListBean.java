package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

public class BadgeListBean extends UltaBean{
	/**
	 * 
	 */
	private static final long serialVersionUID = -652193716286706798L;
	private String badgeImgURL;
	private String badgeName;
	public String getBadgeImgURL() {
		return badgeImgURL;
	}
	public void setBadgeImgURL(String badgeImgURL) {
		this.badgeImgURL = badgeImgURL;
	}
	public String getBadgeName() {
		return badgeName;
	}
	public void setBadgeName(String badgeName) {
		this.badgeName = badgeName;
	}

}
