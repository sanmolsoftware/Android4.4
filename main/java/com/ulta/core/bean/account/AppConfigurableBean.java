package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.product.homePageSectionSlotBean;

import java.util.List;

public class AppConfigurableBean extends UltaBean{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 394064758888723661L;
	private AtgResponseBean appConfig;
	private List<homePageSectionSlotBean> homePageSectionSlotInfo;
	private List<homePageSectionSlotBean> homePageSectionSlotInfoV2;
	private List<CreditCardInfoBean> creditCardsInfo;

	public List<CreditCardInfoBean> getCreditCardsInfo() {
		return creditCardsInfo;
	}

	public void setCreditCardsInfo(List<CreditCardInfoBean> creditCardsInfo) {
		this.creditCardsInfo = creditCardsInfo;
	}
	
	public List<homePageSectionSlotBean> getHomePageSectionInfo() {
		return homePageSectionSlotInfo;
	}

	public void setHomePageSectionInfo(List<homePageSectionSlotBean> homePageSectionSlotInfo) {
		this.homePageSectionSlotInfo = homePageSectionSlotInfo;
	}

	public AtgResponseBean getAtgResponse() {
		return appConfig;
	}

	public void setAtgResponse(AtgResponseBean appConfig) {
		this.appConfig = appConfig;
	}

	public List<homePageSectionSlotBean> getHomePageSectionSlotInfoV2() {
		return homePageSectionSlotInfoV2;
	}

	public void setHomePageSectionSlotInfoV2(List<homePageSectionSlotBean> homePageSectionSlotInfoV2) {
		this.homePageSectionSlotInfoV2 = homePageSectionSlotInfoV2;
	}
}
