/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.AddressBean;



/**
 * The Class DefaultShippingAddressBean.
 */
public class DefaultShippingAddressBean extends UltaBean{
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1530348504531856219L;
/** The default shipping address. */
private AddressBean defaultShippingAddress;

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
		

	}


