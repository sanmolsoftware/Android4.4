/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;



import com.ulta.core.bean.UltaBean;



/**
 * The Class PaymentDetailsBean.
 *
 * @author viva
 */
public class PaymentDetailsBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8241435708784764211L;

	/** The billing address. */
	private AddressBean billingAddress;
	
	/** The credit card number. */
	private String creditCardNumber;
	
	private String nickName;
	
	

	/** The credit card type. */
	private String creditCardType;
	
	//private String expirationDayOfMonth;
	
	/** The expiration month. */
	private String expirationMonth;
	
	/** The expiration year. */
	private String expirationYear;
	
	private String id;
	
	/** The name on card. */
	private String nameOnCard;
	
	/** The repository id. */
	private String repositoryId;

	
	private String address1;
	
	private String address2;
	
	private String city;
	 
	private String firstName;
	
	private String lastName;
	
	private String phoneNumber;
	
	private String postalCode;
	
	private String state;
	private String country;
	/**
	 * Gets the billing address.
	 *
	 * @return the billingAddress
	 */
	public AddressBean getBillingAddress() {
		return billingAddress;
	}

	/**
	 * Sets the billing address.
	 *
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(AddressBean billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * Gets the credit card number.
	 *
	 * @return the creditCardNumber
	 */
	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	/**
	 * Sets the credit card number.
	 *
	 * @param creditCardNumber the creditCardNumber to set
	 */
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	/**
	 * Gets the credit card type.
	 *
	 * @return the creditCardType
	 */
	public String getCreditCardType() {
		return creditCardType;
	}

	/**
	 * Sets the credit card type.
	 *
	 * @param creditCardType the creditCardType to set
	 */
	public void setCreditCardType(String creditCardType) {
		this.creditCardType = creditCardType;
	}

	/**
	 * Gets the expiration month.
	 *
	 * @return the expirationDayOfMonth
	 *//*
	public String getExpirationDayOfMonth() {
		return expirationDayOfMonth;
	}

	*//**
	 * @param expirationDayOfMonth the expirationDayOfMonth to set
	 *//*
	public void setExpirationDayOfMonth(String expirationDayOfMonth) {
		this.expirationDayOfMonth = expirationDayOfMonth;
	}*/

	/**
	 * @return the expirationMonth
	 */
	public String getExpirationMonth() {
		return expirationMonth;
	}

	/**
	 * Sets the expiration month.
	 *
	 * @param expirationMonth the expirationMonth to set
	 */
	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	/**
	 * Gets the expiration year.
	 *
	 * @return the expirationYear
	 */
	public String getExpirationYear() {
		return expirationYear;
	}

	/**
	 * Sets the expiration year.
	 *
	 * @param expirationYear the expirationYear to set
	 */
	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	/**
	 * Gets the name on card.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nameOnCard
	 */
	public String getNameOnCard() {
		return nameOnCard;
	}

	/**
	 * Sets the name on card.
	 *
	 * @param nameOnCard the nameOnCard to set
	 */
	public void setNameOnCard(String nameOnCard) {
		this.nameOnCard = nameOnCard;
	}

	/**
	 * Gets the repository id.
	 *
	 * @return the repositoryId
	 */
	public String getRepositoryId() {
		return repositoryId;
	}

	/**
	 * Sets the repository id.
	 *
	 * @param repositoryId the repositoryId to set
	 */
	public void setRepositoryId(String repositoryId) {
		this.repositoryId = repositoryId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getState() {
		return state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getCity() {
		return city;
	}
	public String getAddress2() {
		return address2;
	}
	public String getAddress1() {
		return address1;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}
}
