package com.ulta.core.bean.favourites;

import com.ulta.core.bean.UltaBean;

public class FavoritesComponentBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9114338509786146937L;
	private MobileFavCartBean component;

	public MobileFavCartBean getComponent() {
		return component;
	}

	public void setComponent(MobileFavCartBean component) {
		this.component = component;
	}
	
}
