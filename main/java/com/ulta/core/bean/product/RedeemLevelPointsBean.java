package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.ArrayList;
//3.2Release
@SuppressWarnings("serial")
public class RedeemLevelPointsBean extends UltaBean {
	private ArrayList<RedeemPointBean> redeemLevels;

	public ArrayList<RedeemPointBean> getRedeemLevels() {
		return redeemLevels;
	}

	public void setRedeemLevels(ArrayList<RedeemPointBean> redeemLevels) {
		this.redeemLevels = redeemLevels;
	}

}
