package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;

@SuppressWarnings("serial")
public class SlideShowBean extends UltaBean {

	private List<atgResponseBean> bannerInfo;

	private List<MobileOffersDescBean> mobileOfferDesc;
	
	private List<MobileCouponInfoBean> mobileCouponInfo;
	
	private List<ShopPageSlideShowBean> shopPageSlideShow;

	public List<ShopPageSlideShowBean> getShopPageSlideShow() {
		return shopPageSlideShow;
	}

	public void setShopPageSlideShow(List<ShopPageSlideShowBean> shopPageSlideShow) {
		this.shopPageSlideShow = shopPageSlideShow;
	}

	public List<MobileCouponInfoBean> getMobileCouponInfo() {
		return mobileCouponInfo;
	}

	public void setMobileCouponInfo(List<MobileCouponInfoBean> mobileCouponInfo) {
		this.mobileCouponInfo = mobileCouponInfo;
	}

	public List<MobileOffersDescBean> getMobileOfferDesc() {
		return mobileOfferDesc;
	}

	public void setMobileOfferDesc(List<MobileOffersDescBean> mobileOfferDesc) {
		this.mobileOfferDesc = mobileOfferDesc;
	}

	public void setResponse(List<atgResponseBean> atgResponse) {
		this.bannerInfo = atgResponse;
	}

	public List<atgResponseBean> getResponse() {
		return bannerInfo;
	}

}
