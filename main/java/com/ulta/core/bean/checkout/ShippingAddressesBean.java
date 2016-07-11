/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

import java.util.List;



/**
 * The Class ShippingAddressesBean.
 *
 * @author viva
 */
public class ShippingAddressesBean extends UltaBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 246253694795794157L;

	/** The default shipping address id. */
	private String defaultShippingAddressId;
	
	/** The shipping addresses. */
	private List<AddressBean> shippingAddresses;

	/**
	 * Gets the default shipping address id.
	 *
	 * @return the defaultShippingAddressId
	 */
	public String getDefaultShippingAddressId() {
		return defaultShippingAddressId;
	}

	/**
	 * Sets the default shipping address id.
	 *
	 * @param defaultShippingAddressId the defaultShippingAddressId to set
	 */
	public void setDefaultShippingAddressId(String defaultShippingAddressId) {
		this.defaultShippingAddressId = defaultShippingAddressId;
	}

	/**
	 * Gets the shipping addresses.
	 *
	 * @return the shippingAddresses
	 */
	public List<AddressBean> getShippingAddresses() {
		return shippingAddresses;
	}

	/**
	 * Sets the shipping addresses.
	 *
	 * @param shippingAddresses the shippingAddresses to set
	 */
	public void setShippingAddresses(List<AddressBean> shippingAddresses) {
		this.shippingAddresses = shippingAddresses;
	}
	
	
}
