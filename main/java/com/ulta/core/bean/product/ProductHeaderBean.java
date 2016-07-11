/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.product;

import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class ProductHeaderBean.
 *
 * @author viva
 */
public class ProductHeaderBean extends UltaBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7730271638376598663L;
	private List<BadgeListBean> badgeList;
	/** The description. */
	private String description;
	
	/** The directions. */
	private String directions;
	
	//3.5 Release
	/** Flag for show Q n A tab*/
	private boolean showQandA ;
	
	/** The display name. */
	private String displayName;
	
	/** The id. */
	private String id;
	
	/** The ingredients. */
	private String ingredients;
	
	/** The is gwp. */
	private int isGWP;
	
	/** The long description. */
	private String longDescription;
	
	/** The special instructions. */
	private String specialInstructions;
	
	/** The warnings. */
	private String warnings;

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the directions.
	 *
	 * @return the directions
	 */
	public String getDirections() {
		return directions;
	}

	/**
	 * Sets the directions.
	 *
	 * @param directions the directions to set
	 */
	public void setDirections(String directions) {
		this.directions = directions;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the ingredients.
	 *
	 * @return the ingredients
	 */
	public String getIngredients() {
		return ingredients;
	}

	/**
	 * Sets the ingredients.
	 *
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * Gets the checks if is gwp.
	 *
	 * @return the isGWP
	 */
	public int getIsGWP() {
		return isGWP;
	}

	/**
	 * Sets the checks if is gwp.
	 *
	 * @param isGWP the isGWP to set
	 */
	public void setIsGWP(int isGWP) {
		this.isGWP = isGWP;
	}

	/**
	 * Gets the long description.
	 *
	 * @return the longDescription
	 */
	public String getLongDescription() {
		return longDescription;
	}

	/**
	 * Sets the long description.
	 *
	 * @param longDescription the longDescription to set
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	/**
	 * Gets the special instructions.
	 *
	 * @return the specialInstructions
	 */
	public String getSpecialInstructions() {
		return specialInstructions;
	}

	/**
	 * Sets the special instructions.
	 *
	 * @param specialInstructions the specialInstructions to set
	 */
	public void setSpecialInstructions(String specialInstructions) {
		this.specialInstructions = specialInstructions;
	}

	/**
	 * Gets the warnings.
	 *
	 * @return the warnings
	 */
	public String getWarnings() {
		return warnings;
	}

	/**
	 * Sets the warnings.
	 *
	 * @param warnings the warnings to set
	 */
	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public List<BadgeListBean> getBadgeList() {
		return badgeList;
	}

	public void setBadgeList(List<BadgeListBean> badgeList) {
		this.badgeList = badgeList;
	}

	public boolean isShowQandA() {
		return showQandA;
	}

	public void setShowQandA(boolean showQandA) {
		this.showQandA = showQandA;
	}
	
	
}
