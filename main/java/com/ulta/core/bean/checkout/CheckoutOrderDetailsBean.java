/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */
package com.ulta.core.bean.checkout;

import com.ulta.core.bean.UltaBean;

import java.util.List;


/**
 * The Class CheckoutOrderDetailsBean.
 */
public class CheckoutOrderDetailsBean extends UltaBean {

    /**
     *
     */
    private static final long serialVersionUID = 2274145369348148858L;

    /** The contains gift message. */
    private boolean containsGiftMessage;

    /** The contains gift wrap. */
    private boolean containsGiftWrap;

    /** The currency code. */
    private String currencyCode;

    /** The id. */
    private String id;

    /** The profile id. */
    private String profileId;

    /** The raw subtotal. */
    private double rawSubtotal;

    /** The raw subtotal. */
    private double rawSubtotalWODiscount;


    public double getRawSubtotalWODiscount() {
        return rawSubtotalWODiscount;
    }

    public void setRawSubtotalWODiscount(double rawSubtotalWODiscount) {
        this.rawSubtotalWODiscount = rawSubtotalWODiscount;
    }

    /** The shipping. */
    private double shipping;

    /** The state. */
    private int state;

    /** The tax. */
    private double tax;

    /** The total. */
    private double total;

    private double totalNew;

    public double getTotalNew() {
        return totalNew;
    }

    public void setTotalNew(double totalNew) {
        this.totalNew = totalNew;
    }

    private String freeShippingAmount;
    private String tieredDiscountAmount;
    private List<String> orderMessages;

    public List<String> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessages(List<String> orderMessages) {
        this.orderMessages = orderMessages;
    }

    public String getTieredDiscountAmount() {
        return tieredDiscountAmount;
    }

    public void setTieredDiscountAmount(String tieredDiscountAmount) {
        this.tieredDiscountAmount = tieredDiscountAmount;
    }

    private String couponCode;


    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    /**
     * Checks if is contains gift message.
     *
     * @return true, if is contains gift message
     */
    public boolean isContainsGiftMessage() {
        return containsGiftMessage;
    }

    /**
     * Sets the contains gift message.
     *
     * @param containsGiftMessage the new contains gift message
     */
    public void setContainsGiftMessage(boolean containsGiftMessage) {
        this.containsGiftMessage = containsGiftMessage;
    }

    /**
     * Checks if is contains gift wrap.
     *
     * @return true, if is contains gift wrap
     */
    public boolean isContainsGiftWrap() {
        return containsGiftWrap;
    }

    /**
     * Sets the contains gift wrap.
     *
     * @param containsGiftWrap the new contains gift wrap
     */
    public void setContainsGiftWrap(boolean containsGiftWrap) {
        this.containsGiftWrap = containsGiftWrap;
    }

    /**
     * Gets the currency code.
     *
     * @return the currency code
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * Sets the currency code.
     *
     * @param currencyCode the new currency code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the profile id.
     *
     * @return the profile id
     */
    public String getProfileId() {
        return profileId;
    }

    /**
     * Sets the profile id.
     *
     * @param profileId the new profile id
     */
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    /**
     * Gets the raw subtotal.
     *
     * @return the raw subtotal
     */
    public double getRawSubtotal() {
        return rawSubtotal;
    }

    /**
     * Sets the raw subtotal.
     *
     * @param rawSubtotal the new raw subtotal
     */
    public void setRawSubtotal(double rawSubtotal) {
        this.rawSubtotal = rawSubtotal;
    }

    /**
     * Gets the shipping.
     *
     * @return the shipping
     */
    public double getShipping() {
        return shipping;
    }

    /**
     * Sets the shipping.
     *
     * @param shipping the new shipping
     */
    public void setShipping(double shipping) {
        this.shipping = shipping;
    }

    /**
     * Gets the state.
     *
     * @return the state
     */
    public int getState() {
        return state;
    }

    /**
     * Sets the state.
     *
     * @param state the new state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     * Gets the tax.
     *
     * @return the tax
     */
    public double getTax() {
        return tax;
    }

    /**
     * Sets the tax.
     *
     * @param tax the new tax
     */
    public void setTax(double tax) {
        this.tax = tax;
    }

    /**
     * Gets the total.
     *
     * @return the total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total.
     *
     * @param total the new total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    public void setFreeShippingAmount(String freeShippingAmount) {
        this.freeShippingAmount = freeShippingAmount;
    }

    public String getFreeShippingAmount() {
        return freeShippingAmount;
    }

}
