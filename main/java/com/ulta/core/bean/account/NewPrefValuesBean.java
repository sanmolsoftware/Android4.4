package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

public class NewPrefValuesBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9192550865891006937L;

	private String category;
	private String isSelected;
	private String prefMaster;
	private String prefValues;
	private String relationshipId;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIsSelected() {
		return isSelected;
	}
	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}
	public String getPrefMaster() {
		return prefMaster;
	}
	public void setPrefMaster(String prefMaster) {
		this.prefMaster = prefMaster;
	}
	public String getPrefValues() {
		return prefValues;
	}
	public void setPrefValues(String prefValues) {
		this.prefValues = prefValues;
	}
	public String getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}
	
	
}
