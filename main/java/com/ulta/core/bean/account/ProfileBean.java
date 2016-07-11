/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;



import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.AddressBean;

import java.util.Locale;


/**
 * The Class ProfileBean.
 *
 * @author viva
 */
public class ProfileBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2923181227277198951L;

	/** The default shipping address. */
	private AddressBean defaultShippingAddress;
	
	private BeautyClubPlanTypeBean beautyClubPlanType;

	private String planId;
	/** The date of birth. */
	private String dateOfBirth;
	
	/** The gender. */
	private String gender;


	
	/** The home address. */
	private AddressBean homeAddress;
	
	/** The beauty club number. */
	private String beautyClubNumber;
	
	/** The balance points. */
	private String balancePoints;
	
	/** The email. */
	private String email;
	
	/** The first name. */
	private String firstName;
	
	/** The last name. */
	private String lastName;

	/**
	 * Gets the default shipping address.
	 *
	 * @return the default shipping address
	 */
	public AddressBean getDefaultShippingAddress() {
		return defaultShippingAddress;
	}

	/**
	 * Sets the default shipping address.
	 *
	 * @param defaultShippingAddress the new default shipping address
	 */
	public void setDefaultShippingAddress(AddressBean defaultShippingAddress) {
		this.defaultShippingAddress = defaultShippingAddress;
	}

	/**
	 * Gets the date of birth.
	 *
	 * @return the date of birth
	 */
	public String getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * Sets the date of birth.
	 *
	 * @param dateOfBirth the new date of birth
	 */
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 *
	 * @param gender the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Gets the home address.
	 *
	 * @return the home address
	 */
	public AddressBean getHomeAddress() {
		return homeAddress;
	}

	/**
	 * Sets the home address.
	 *
	 * @param homeAddress the new home address
	 */
	public void setHomeAddress(AddressBean homeAddress) {
		this.homeAddress = homeAddress;
	}

	/**
	 * Gets the beauty club number.
	 *
	 * @return the beauty club number
	 */
	public String getBeautyClubNumber() {
		return beautyClubNumber;
	}

	/**
	 * Sets the beauty club number.
	 *
	 * @param beautyClubNumber the new beauty club number
	 */
	public void setBeautyClubNumber(String beautyClubNumber) {
		this.beautyClubNumber = beautyClubNumber;
	}

	/**
	 * Gets the balance points.
	 *
	 * @return the balance points
	 */
	public String getBalancePoints() {
		return balancePoints;
	}

	/**
	 * Sets the balance points.
	 *
	 * @param balancePoints the new balance points
	 */
	public void setBalancePoints(String balancePoints) {
		this.balancePoints = balancePoints;
	}

	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		firstName = firstName.substring(0, 1).toUpperCase(Locale.US)
				+ firstName.substring(1).toLowerCase(Locale.US);
		return firstName;

	}

	/**
	 * Sets the first name.
	 *
	 * @param firstName the new first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		lastName = lastName.substring(0,1).toUpperCase(Locale.US)
				+ lastName.substring(1).toLowerCase(Locale.US);

		return lastName;
	}

	/**
	 * Sets the last name.
	 *
	 * @param lastName the new last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public BeautyClubPlanTypeBean getBeautyClubPlanType() {
		return beautyClubPlanType;
	}

	public void setBeautyClubPlanType(BeautyClubPlanTypeBean beautyClubPlanType) {
		this.beautyClubPlanType = beautyClubPlanType;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanId() {
		return planId;
	}
	
	
}
