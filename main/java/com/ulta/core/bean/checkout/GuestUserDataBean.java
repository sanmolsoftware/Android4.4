package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class GuestUserDataBean extends UltaBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7271719058674904415L;
	
	
	//mail id
	
	String guestMailId;
	

	//shipping details
	String strFirstNameShipping;
	String strLastNameShipping;
	String strphoneShipping;
	String strAddressLine1Shipping;
	String strAddressLine2Shipping;
	String strSelectedStateShipping;
	String strCityShipping;
	String strZipcodeShipping;
	String strsaveShippingAsBilling;
	String strSaveShippingofFuture;
	
	//Billing details
	String strFirstNameBilling;
	String strLastNameBilling;
	String strphoneBilling;
	String strAddressLine1Billing;
	String strAddressLine2Billing;
	String strSelectedStateBilling;
	String strCityBilling;
	String strZipcodeBilling;
	
	/**
	 * get guest maild id
	 * @return
	 */
	public String getGuestMailId() {
		return guestMailId;
	}

	/**
	 * set the guest mail id
	 * @param guestMailId
	 */
	public void setGuestMailId(String guestMailId) {
		this.guestMailId = guestMailId;
	}
	
	
	//shipping details
	/**
	 * Get the first name of shipping address
	 * @return
	 */
	public String getStrFirstNameShipping() {
		return strFirstNameShipping;
	}
	
	/**
	 * Set the first name of shipping address
	 * @param strFirstNameShipping
	 */
	public void setStrFirstNameShipping(String strFirstNameShipping) {
		this.strFirstNameShipping = strFirstNameShipping;
	}
	
	/**
	 * get the last name of shipping address
	 * @return
	 */
	public String getStrLastNameShipping() {
		return strLastNameShipping;
	}
	
	/**
	 * Set the last name of shipping address
	 * @param strLastNameShipping
	 */
		public void setStrLastNameShipping(String strLastNameShipping) {
		this.strLastNameShipping = strLastNameShipping;
	}
		
		/**
		 * get phone number of shipping address
		 * @return
		 */
	public String getStrphoneShipping() {
		return strphoneShipping;
	}
	
	/**
	 * Set the phone number of shipping address
	 * @param strphoneShipping
	 */
	public void setStrphoneShipping(String strphoneShipping) {
		this.strphoneShipping = strphoneShipping;
	}
	
	/**
	 * get Address1 of shipping address
	 * @return
	 */
	public String getStrAddressLine1Shipping() {
		return strAddressLine1Shipping;
	}
	
	/**
	 * Set address1 of shipping address
	 * @param strAddressLine1Shipping
	 */
	
	public void setStrAddressLine1Shipping(String strAddressLine1Shipping) {
		this.strAddressLine1Shipping = strAddressLine1Shipping;
	}
	/**
	 * get Address2 of shipping adress
	 * @return
	 */
	public String getStrAddressLine2Shipping() {
		return strAddressLine2Shipping;
	}
	
	/**
	 * 
	 * @param strAddressLine2Shipping
	 */
	public void setStrAddressLine2Shipping(String strAddressLine2Shipping) {
		this.strAddressLine2Shipping = strAddressLine2Shipping;
	}
	
	/**
	 * get state of shipping address
	 * @return
	 */
	public String getStrSelectedStateShipping() {
		return strSelectedStateShipping;
	}
	/**
	 *  set state of shipping address
	 * @param strSelectedStateShipping
	 */
	public void setStrSelectedStateShipping(String strSelectedStateShipping) {
		this.strSelectedStateShipping = strSelectedStateShipping;
	}
	/**
	 * get city of shipping address
	 * @return
	 */
	public String getStrCityShipping() {
		return strCityShipping;
	}
	/**
	 * set city of shipping address
	 * @param strCityShipping
	 */
	public void setStrCityShipping(String strCityShipping) {
		this.strCityShipping = strCityShipping;
	}
	/**
	 * get zipcode of shipping
	 * @return
	 */
	public String getStrZipcodeShipping() {
		return strZipcodeShipping;
	}
	/**
	 * set zipcode of shipping
	 * @param strZipcodeShipping
	 */
	public void setStrZipcodeShipping(String strZipcodeShipping) {
		this.strZipcodeShipping = strZipcodeShipping;
	}
	/**
	 * get save shipping as billing value
	 * @return
	 */
	public String getStrsaveShippingAsBilling() {
		return strsaveShippingAsBilling;
	}
	/**
	 * set save shipping as billing value
	 * @param strsaveShippingAsBilling
	 */
	public void setStrsaveShippingAsBilling(String strsaveShippingAsBilling) {
		this.strsaveShippingAsBilling = strsaveShippingAsBilling;
	}
	/**
	 * get save shipping for future value
	 * @return
	 */
	public String getStrSaveShippingofFuture() {
		return strSaveShippingofFuture;
	}
	/**
	 * get save shipping for future value
	 * @param strSaveShippingofFuture
	 */
	public void setStrSaveShippingofFuture(String strSaveShippingofFuture) {
		this.strSaveShippingofFuture = strSaveShippingofFuture;
	}
	
	//Billing details
	
	/**
	 * Get the first name of billing address
	 * @return
	 */
	public String getStrFirstNameBilling() {
		return strFirstNameBilling;
	}
	/**
	 * set the first name of billing address
	 * @param strFirstNameBilling
	 */
	public void setStrFirstNameBilling(String strFirstNameBilling) {
		this.strFirstNameBilling = strFirstNameBilling;
	}
	public String getStrLastNameBilling() {
		return strLastNameBilling;
	}
	public void setStrLastNameBilling(String strLastNameBilling) {
		this.strLastNameBilling = strLastNameBilling;
	}
	public String getStrphoneBilling() {
		return strphoneBilling;
	}
	public void setStrphoneBilling(String strphoneBilling) {
		this.strphoneBilling = strphoneBilling;
	}
	public String getStrAddressLine1Billing() {
		return strAddressLine1Billing;
	}
	public void setStrAddressLine1Billing(String strAddressLine1Billing) {
		this.strAddressLine1Billing = strAddressLine1Billing;
	}
	public String getStrAddressLine2Billing() {
		return strAddressLine2Billing;
	}
	public void setStrAddressLine2Billing(String strAddressLine2Billing) {
		this.strAddressLine2Billing = strAddressLine2Billing;
	}
	public String getStrSelectedStateBilling() {
		return strSelectedStateBilling;
	}
	public void setStrSelectedStateBilling(String strSelectedStateBilling) {
		this.strSelectedStateBilling = strSelectedStateBilling;
	}
	public String getStrCityBilling() {
		return strCityBilling;
	}
	public void setStrCityBilling(String strCityBilling) {
		this.strCityBilling = strCityBilling;
	}
	public String getStrZipcodeBilling() {
		return strZipcodeBilling;
	}
	public void setStrZipcodeBilling(String strZipcodeBilling) {
		this.strZipcodeBilling = strZipcodeBilling;
	}

}
