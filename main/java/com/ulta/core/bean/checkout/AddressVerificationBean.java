package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

public class AddressVerificationBean extends UltaBean  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2406392042495300722L;
	/** The billing address. */
	private AddressBean enteredShipAddress;
	/** The billing address. */
	private AddressBean verifiedShipAddress;
	/**
	 * Gets the billing address.
	 *
	 * @return the billing address
	 */
	public AddressBean getEnteredShipAddress() {
		return enteredShipAddress;
	}

	/**
	 * Sets the billing address.
	 *
	 * @param billingAddress the new billing address
	 */
	public void setEnteredShipAddress(AddressBean enteredShipAddress) {
		this.enteredShipAddress = enteredShipAddress;
	}

	/**
	 * Gets the billing address.
	 *
	 * @return the billing address
	 */
	public AddressBean getVerifiedShipAddress() {
		return verifiedShipAddress;
	}

	/**
	 * Sets the billing address.
	 *
	 * @param billingAddress the new billing address
	 */
	public void setVerifiedShipAddress(AddressBean verifiedShipAddress) {
		this.verifiedShipAddress = verifiedShipAddress;
	}

}
