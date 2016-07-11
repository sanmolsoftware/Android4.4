/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.bean.account;

import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;

import java.util.List;



/**
 * The Class PaymentMethodBean.
 */
public class PaymentMethodBean  extends UltaBean{



	/**
	 * 
	 */
	private static final long serialVersionUID = 5327539172632160350L;

	/** The credit cards. */
	private List<PaymentDetailsBean> creditCards;
	
	/** The default credit card id. */
	private String defaultCreditCardId;

	/**
	 * Gets the credit cards.
	 *
	 * @return the creditCards
	 */
	public  List<PaymentDetailsBean> getCreditCards() {
		return creditCards;
	}

	/**
	 * Sets the credit cards.
	 *
	 * @param creditCards the creditCards to set
	 */
	public void setCreditCards(List<PaymentDetailsBean> creditCards) {
		this.creditCards = creditCards;
	}

	/**
	 * Gets the default credit card id.
	 *
	 * @return the defaultCreditCardId
	 */
	public String getDefaultCreditCardId() {
		return defaultCreditCardId;
	}

	/**
	 * Sets the default credit card id.
	 *
	 * @param defaultCreditCardId the defaultCreditCardId to set
	 */
	public void setDefaultCreditCardId(String defaultCreditCardId) {
		this.defaultCreditCardId = defaultCreditCardId;
	}
	
	
	
	
	
	
 
}
