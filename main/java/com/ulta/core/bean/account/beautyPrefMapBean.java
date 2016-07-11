package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;

import java.util.List;

public class beautyPrefMapBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6058826988107905590L;
	private List<String> categories_of_interest;
	private List<String> gender;
	private List<String> hair_color;
	private List<String> hair_concern;
	private List<String> hair_type;
	private List<String> skin_tone;
	private List<String> skin_type;
	private List<String> skincare_concern;
	public void setCategories_of_interest(List<String> categories_of_interest) {
		this.categories_of_interest = categories_of_interest;
	}
	public List<String> getCategories_of_interest() {
		return categories_of_interest;
	}
	public void setGender(List<String> gender) {
		this.gender = gender;
	}
	public List<String> getGender() {
		return gender;
	}
	
	public void setSkincare_concern(List<String> skincare_concern) {
		this.skincare_concern = skincare_concern;
	}
	public void setSkin_type(List<String> skin_type) {
		this.skin_type = skin_type;
	}
	public void setHair_color(List<String> hair_color) {
		this.hair_color = hair_color;
	}
	public List<String> getHair_color() {
		return hair_color;
	}
	public void setHair_concern(List<String> hair_concern) {
		this.hair_concern = hair_concern;
	}
	public List<String> getHair_concern() {
		return hair_concern;
	}
	public void setHair_type(List<String> hair_type) {
		this.hair_type = hair_type;
	}
	public List<String> getHair_type() {
		return hair_type;
	}
	public void setSkin_tone(List<String> skin_tone) {
		this.skin_tone = skin_tone;
	}
	public List<String> getSkin_tone() {
		return skin_tone;
	}
	public List<String> getSkincare_concern() {
		return skincare_concern;
	}
	public List<String> getSkin_type() {
		return skin_type;
	}
	
}
