/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.google.gson.annotations.SerializedName;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.VersionBean;

import java.util.List;



/**
 * The Class CategoriesBean.
 *
 * @author Vijish_Varghese
 */
@SuppressWarnings("serial")
public class CategoriesBean extends UltaBean {

	/** The categories. */
	@SerializedName("rootCategories")
	private List<CategoryBean> categories;
	
	private VersionBean versionInfo;
	
	private AppConfigurablesBean appConfigurables;
	
	

	/**
	 * Gets the categories.
	 *
	 * @return the categories
	 */
	public List<CategoryBean> getCategories() {
		return categories;
	}

	/**
	 * Sets the categories.
	 *
	 * @param categories the categories to set
	 */
	public void setCategories(List<CategoryBean> categories) {
		this.categories = categories;
	}

	public VersionBean getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(VersionBean versionInfo) {
		this.versionInfo = versionInfo;
	}
	
	public AppConfigurablesBean getAppConfigurables() {
		return appConfigurables;
	}

	public void setAppConfigurables(AppConfigurablesBean appConfigurables) {
		this.appConfigurables = appConfigurables;
	}
	
	
	
}
