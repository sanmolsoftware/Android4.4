package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class profilePrefMapBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5184037301500869949L;
	private List<String> categories;
	private List<String> hairConcerns;
	private List<String> hairType;
	private List<String> skinTone;
	private List<String> skincareConcerns;
	private String hairColor;
	private String skinType;
	private String gender;
	
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setHairConcerns(List<String> hairConcerns) {
		this.hairConcerns = hairConcerns;
	}
	public List<String> getHairConcerns() {
		return hairConcerns;
	}
	public void setHairType(List<String> hairType) {
		this.hairType = hairType;
	}
	public List<String> getHairType() {
		return hairType;
	}
	public void setSkinTone(List<String> skinTone) {
		this.skinTone = skinTone;
	}
	public List<String> getSkinTone() {
		return skinTone;
	}
	public void setSkincareConcerns(List<String> skincareConcerns) {
		this.skincareConcerns = skincareConcerns;
	}
	public List<String> getSkincareConcerns() {
		return skincareConcerns;
	}
	public void setHairColor(String hairColor) {
		this.hairColor = hairColor;
	}
	public String getHairColor() {
		return hairColor;
	}
	public void setSkinType(String skinType) {
		this.skinType = skinType;
	}
	public String getSkinType() {
		return skinType;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getGender() {
		return gender;
	}
}
