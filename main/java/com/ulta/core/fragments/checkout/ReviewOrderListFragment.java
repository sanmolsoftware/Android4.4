/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */
package com.ulta.core.fragments.checkout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.checkout.CheckoutCommerceItemBean;
import com.ulta.core.bean.checkout.CheckoutLoyaltyPointsPaymentGroupBean;
import com.ulta.core.bean.checkout.ReviewOrderBean;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ReviewOrderListFragment.
 */
public class ReviewOrderListFragment extends ListFragment {
    /*
     * private final static int REQUESTCODE_FREESAMPLES=0; private final static
	 * int REQUESTCODE_GIFTOPTION=1; private final static int
	 * REQUESTCODE_PAYMENTMETHOD=2; private final static int
	 * REQUESTCODE_BILLINGADDRESS=3; private final static int
	 * REQUESTCODE_PROMOCODE=4; private final static int
	 * REQUESTCODE_SHIPPINGADDRESS=5; private final static int
	 * REQUESTCODE_SHIPPINGMETHOD=6;
	 */
    /**
     * The context.
     */
    Context context;

    /**
     * The footer.
     */
    ViewGroup header, footer;

    /**
     * The shipping method.
     */
    LinearLayout giftOption, freeSamples, paymentMethod, billingAddress,
            giftCode, shippingAddress, shippingMethod;

    /**
     * The txt sub total.
     */
    TextView txtShippingCharge, txtShippingMethod, txtRawSubTotal, txtTax,
            txtSubTotal, txtRedeemedAmount;

    /**
     * The chk gift option.
     */
    CheckBox chkFreeSamples, chkGiftOption;

    /**
     * The str credit card number.
     */
    String strShippingMethod, strShippingAddress, strBillingAddress,
            strCreditCardNumber;

    /**
     * The review order bean.
     */
    ReviewOrderBean reviewOrderBean;

    /**
     * The text card details line2.
     */
    TextView textShippingAddressLine1, textShippingAddressLine2,
            textBillingAddressLine1, textBillingAddressLine2,
            textCardDetailsLine1, textCardDetailsLine2;

    /**
     * The str shipping postal code.
     */
    String strShippingFirstName = "", strShippingLastName = "",
            strShippingAddressLine1 = "", strShippingState = "",
            strShippingCity = "", strShippingPostalCode = "";

    /**
     * The str billing postal code.
     */
    String strBillingFirstName = "", strBillingLastName = "",
            strBillingAddressLine1 = "", strBillingState = "",
            strBillingCity = "", strBillingPostalCode = "";

    /**
     * The str card expiry year.
     */
    String strCardType = "", strCardNumber = "", strCardExpiryMonth = "",
            strCardExpiryYear = "", strCreditCardType = "";

    /**
     * The shipping address layout.
     */
    LinearLayout shippingAddressLayout;

    /**
     * The billing address layout.
     */
    LinearLayout billingAddressLayout;

    /**
     * The credit card layout.
     */
    LinearLayout creditCardLayout;

    private LinearLayout mCouponCodeLayout;

    private int i;

    private TextView mBillingAddressHeadingTextView;
    private TextView mCouponTextView, mCouponTextValue;
    private LinearLayout mGiftBoxAndNoteLayout;
    private TextView mGiftBoxAndNoteTextViewValue;
    private LinearLayout additionalDicountLayout;
    private TextView tvAdditionalDiscount;
    private View additionalDiscountView;
    private LinearLayout mEmailSubscriptionLayout;
    private Switch mEmai_subscription_switch;
    public static boolean isEmailOpted = true;
    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.ListFragment#onCreateView(android.view.LayoutInflater
     * , android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ListView view = (ListView) inflater.inflate(R.layout.list, null);
        header = (ViewGroup) inflater.inflate(
                R.layout.submit_order_list_header, null, false);
        // getListView().addHeaderView(header, null, false);

        footer = (ViewGroup) inflater.inflate(
                R.layout.submit_order_list_footer, null, false);
        view.addFooterView(footer, null, false);

        // txtNoOfItems = (TextView)
        // header.findViewById(R.id.tvsubmit_order_numberof_items);
        mEmailSubscriptionLayout= (LinearLayout) footer.findViewById(R.id.emailSubscriptionLayout);
        if (UltaDataCache.getDataCacheInstance()
                .isAnonymousCheckout()) {
            mEmailSubscriptionLayout.setVisibility(View.VISIBLE);
        }
        else {
            mEmailSubscriptionLayout.setVisibility(View.GONE);
        }
        mEmai_subscription_switch= (Switch) footer.findViewById(R.id.emai_subscription_switch);
        mEmai_subscription_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    isEmailOpted = true;

                } else {
                    isEmailOpted = false;
                }
            }
        });
        additionalDicountLayout = (LinearLayout) footer.findViewById(R.id.additionalDicountLayout);
        tvAdditionalDiscount = (TextView) footer.findViewById(R.id.tvAdditionalDiscount);
        additionalDiscountView = footer.findViewById(R.id.additionalDiscountView);
        txtRawSubTotal = (TextView) footer
                .findViewById(R.id.tvSubmitorderRawTotal);
        txtShippingCharge = (TextView) footer
                .findViewById(R.id.tvSubmitorderShipping);
        txtShippingMethod = (TextView) footer
                .findViewById(R.id.shippingMethodTV);

        txtTax = (TextView) footer.findViewById(R.id.tvSubmitorderTax);
        txtSubTotal = (TextView) footer.findViewById(R.id.tvSubmitorderTotal);
        txtRedeemedAmount = (TextView) footer
                .findViewById(R.id.tvRedeemedAmount);

        mGiftBoxAndNoteLayout = (LinearLayout) footer
                .findViewById(R.id.giftBoxAndNoteLayout);

        mGiftBoxAndNoteTextViewValue = (TextView) footer
                .findViewById(R.id.giftBoxAndNoteValueTextView);

        mBillingAddressHeadingTextView = (TextView) footer
                .findViewById(R.id.billingAddressHeadingTextView);

        mCouponCodeLayout = (LinearLayout) footer
                .findViewById(R.id.couponCodeLayout);
        mCouponTextView = (TextView) footer.findViewById(R.id.couponText);
        mCouponTextValue = (TextView) footer.findViewById(R.id.couponValue);
        // return inflater.inflate(R.layout.list, null);
        return view;

        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        context = activity;
        super.onAttach(activity);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
     * , android.view.View, int, long)
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }

    /**
     * Sets the list footer data.
     *
     * @param object
     * the new list footer data
     */
    CheckoutLoyaltyPointsPaymentGroupBean checkOutLoyality;

    public void setListFooterData(ReviewOrderBean object) {
        int quantity = 0;
        double amountAdjusted = 0;
        reviewOrderBean = (ReviewOrderBean) object;
        // 3.2 Release
        checkOutLoyality = new CheckoutLoyaltyPointsPaymentGroupBean();
        if (reviewOrderBean.getComponent().getPaymentDetails()
                .getLoyaltyPointsPaymentGroups() != null
                && !reviewOrderBean.getComponent().getPaymentDetails()
                .getLoyaltyPointsPaymentGroups().isEmpty()) {
            if (reviewOrderBean.getComponent().getPaymentDetails()
                    .getLoyaltyPointsPaymentGroups().size() != 0) {
                for (int i = 0; i < reviewOrderBean.getComponent()
                        .getPaymentDetails().getLoyaltyPointsPaymentGroups()
                        .size(); i++) {
                    checkOutLoyality = reviewOrderBean.getComponent()
                            .getPaymentDetails()
                            .getLoyaltyPointsPaymentGroups().get(i);
                    amountAdjusted += Double.valueOf(checkOutLoyality
                            .getAmount());
                }
            }
        }
        Logger.Log(">>>>>>>>>> reviewOrderBean total"
                + reviewOrderBean.getComponent().getPaymentDetails()
                .getOrderDetails().getTotal());

        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCommerceItems()) {
            for (i = 0; i < reviewOrderBean.getComponent().getPaymentDetails()
                    .getCommerceItems().size(); i++) {
                quantity = quantity
                        + Integer.parseInt(reviewOrderBean.getComponent()
                        .getPaymentDetails().getCommerceItems().get(i)
                        .getQuantity());
                Logger.Log(">>>>>>>>>>>>>>>><<<<<<<<<<||||>>>>>>>><<<<<"
                        + quantity + "----" + i);
            }
        }

        // txtNoOfItems.setText(reviewOrderBean.getComponent().getPaymentDetails().getCommerceItems().size()+"  Products");

        txtRawSubTotal.setText("$"
                + String.format(
                "%.2f",
                Double.valueOf(reviewOrderBean.getComponent()
                        .getPaymentDetails().getOrderDetails()
                        .getRawSubtotal())));
        txtShippingCharge.setText("$"
                + String.format(
                "%.2f",
                Double.valueOf(reviewOrderBean.getComponent()
                        .getPaymentDetails().getOrderDetails()
                        .getShipping())));
        if (Double.valueOf(reviewOrderBean.getComponent()
                .getPaymentDetails().getOrderDetails()
                .getShipping()).equals(0.0)) {
            txtShippingCharge.setText("FREE");
        }
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getHardGoodShippingGroups()
                && reviewOrderBean.getComponent().getPaymentDetails()
                .getHardGoodShippingGroups().size() > 0) {
            if (reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getShippingMethod()
                    .equalsIgnoreCase("ups_next_day")) {
                txtShippingMethod.setText("UPS Next Day Air");
            } else if (reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getShippingMethod()
                    .equalsIgnoreCase("ups_second_day")) {
                txtShippingMethod.setText("UPS 2nd Day Air");
            } else {
                txtShippingMethod.setText("Standard Ground");
            }
        }
        txtTax.setText("$"
                + String.format("%.2f", Double.valueOf(reviewOrderBean
                .getComponent().getPaymentDetails().getOrderDetails()
                .getTax())));
        // 3.2 Release
        if (amountAdjusted != 0) {
            LinearLayout lytRedeemedAmount = (LinearLayout) footer
                    .findViewById(R.id.linearLayout15);
            lytRedeemedAmount.setVisibility(View.VISIBLE);
            txtRedeemedAmount.setText("-$"
                    + String.format("%.2f", amountAdjusted));
            txtSubTotal.setText("$"
                    + String.format(
                    "%.2f",
                    Double.valueOf(reviewOrderBean.getComponent()
                            .getPaymentDetails().getOrderDetails()
                            .getTotal())
                            - amountAdjusted));
        } else {
            txtSubTotal.setText("$"
                    + String.format(
                    "%.2f",
                    Double.valueOf(reviewOrderBean.getComponent()
                            .getPaymentDetails().getOrderDetails()
                            .getTotal())));
        }

        //Tiered Price Break Up promo
        if (null != reviewOrderBean.getComponent()
                .getPaymentDetails().getOrderDetails().getTieredDiscountAmount() && !reviewOrderBean.getComponent()
                .getPaymentDetails().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
            tvAdditionalDiscount.setText("-$"
                    + String.format("%.2f",
                    Double.valueOf(reviewOrderBean.getComponent()
                            .getPaymentDetails().getOrderDetails().getTieredDiscountAmount())));
            additionalDicountLayout.setVisibility(View.VISIBLE);
            additionalDiscountView.setVisibility(View.VISIBLE);
        }
        displayCouponApplied();
        setAddressAndCreditCardDetails();
    }

    private void displayCouponApplied() {
        if (null != reviewOrderBean) {
            if (null != reviewOrderBean.getComponent()) {
                if (null != reviewOrderBean.getComponent().getPaymentDetails()) {
                    if (null != reviewOrderBean.getComponent()
                            .getPaymentDetails().getOrderDetails()) {

                        // Checking new service Coupon discount is available or
                        // not
                        if (null != reviewOrderBean.getComponent()
                                .getPaymentDetails().getCouponDiscount()
                                && null != reviewOrderBean.getComponent()
                                .getPaymentDetails()
                                .getCouponDiscount().getCouponCode()
                                && null != reviewOrderBean.getComponent()
                                .getPaymentDetails()
                                .getCouponDiscount()
                                .getTotalAdjustment()) {

                            mCouponTextView.setText("Coupon Code Discount ");
                            //Coupon Value
                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getCouponDiscount() &&
                                    null != reviewOrderBean.getComponent()
                                            .getPaymentDetails()
                                            .getCouponDiscount().getOrderDiscount() &&
                                    null != reviewOrderBean.getComponent()
                                            .getPaymentDetails()
                                            .getCouponDiscount().getTotalAdjustment()) {
                                if (Double.valueOf(reviewOrderBean.getComponent()
                                        .getPaymentDetails()
                                        .getCouponDiscount().getOrderDiscount()).equals(0.0)) {
                                    mCouponCodeLayout.setVisibility(View.GONE);
                                } else {
                                    mCouponTextValue.setText("-$"
                                            + String.format(
                                            "%.2f",
                                            Double.valueOf(reviewOrderBean.getComponent()
                                                    .getPaymentDetails()
                                                    .getCouponDiscount().getTotalAdjustment())));
                                    mCouponCodeLayout.setVisibility(View.VISIBLE);
                                }

                            }

                            // if isShippingFree is not null the shipping coupon
                            // is applied
//                            if (null != reviewOrderBean.getComponent()
//                                    .getPaymentDetails().getCouponDiscount()
//                                    .isShippingFree()) {
//                                mCouponCodeLayout.setVisibility(View.VISIBLE);
//                                // if isShippingFree is true then shipping
//                                // coupon with shipping free offer is applied
//                                if (reviewOrderBean.getComponent()
//                                        .getPaymentDetails()
//                                        .getCouponDiscount().isShippingFree()
//                                        .equalsIgnoreCase("true")) {
//                                    mCouponCodeLayout.setVisibility(View.GONE);
//									/*
//									 * mCouponTextValue.setText("$0.00");
//									 */
//                                }
//                                // if isShippingFree is false then shipping
//                                // coupon with percent off offer is applied
//                                else {
//
//                                    mCouponTextValue
//                                            .setText("-$"
//                                                    + String.format(
//                                                    "%.2f",
//                                                    Double.valueOf(reviewOrderBean
//                                                            .getComponent()
//                                                            .getPaymentDetails()
//                                                            .getCouponDiscount()
//                                                            .getTotalAdjustment())));
//                                }
//                            }
//                            // if isShippingFree is null then order level or
//                            // item level coupon is applied
//                            else {
//
//                                if (null != reviewOrderBean.getComponent()
//                                        .getPaymentDetails()
//                                        .getCouponDiscount()
//                                        .getTotalAdjustment()) {
//                                    mCouponCodeLayout
//                                            .setVisibility(View.VISIBLE);
//                                    mCouponTextValue
//                                            .setText("-$"
//                                                    + String.format(
//                                                    "%.2f",
//                                                    Double.valueOf(reviewOrderBean
//                                                            .getComponent()
//                                                            .getPaymentDetails()
//                                                            .getCouponDiscount()
//                                                            .getTotalAdjustment())));
//                                }
//                            }

                        }
                        // if not take coupon discount from order adjustment
                        else {
                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails().getOrderDetails()
                                    .getCouponCode()) {
                                mCouponTextView
                                        .setText("Coupon Code Discount ");

                                // mCouponTextView.setText("Coupon Code "
                                // + reviewOrderBean.getComponent()
                                // .getPaymentDetails()
                                // .getOrderDetails().getCouponCode()
                                // + " applied");
                                if (null != reviewOrderBean.getComponent()
                                        .getPaymentDetails()
                                        .getOrderAdjustments()
                                        && reviewOrderBean.getComponent()
                                        .getPaymentDetails()
                                        .getOrderAdjustments().size() == 2) {
                                    if (reviewOrderBean.getComponent()
                                            .getPaymentDetails()
                                            .getOrderAdjustments().get(1)
                                            .getTotalAdjustment() < 0) {
                                        mCouponCodeLayout
                                                .setVisibility(View.VISIBLE);
                                        String couponAmount = String.format(
                                                "%.2f",
                                                Double.valueOf(reviewOrderBean
                                                        .getComponent()
                                                        .getPaymentDetails()
                                                        .getOrderAdjustments()
                                                        .get(1)
                                                        .getTotalAdjustment()));
                                        couponAmount = "-$"
                                                + couponAmount.substring(1);

                                        mCouponTextValue.setText(couponAmount);
                                    }

                                }

                            }
                        }

                    }
                }
            }

        }
    }

    public void populateListData(ReviewOrderBean reviewOrderBean) {
        List<CheckoutCommerceItemBean> combinedList = new ArrayList<CheckoutCommerceItemBean>();
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCommerceItems()
                && null != reviewOrderBean.getComponent().getPaymentDetails()
                .getElectronicGiftCardCommerceItems()) {
            combinedList.addAll(reviewOrderBean.getComponent()
                    .getPaymentDetails().getCommerceItems());
            combinedList.addAll(reviewOrderBean.getComponent()
                    .getPaymentDetails().getElectronicGiftCardCommerceItems());
        } else if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCommerceItems()) {
            combinedList.addAll(reviewOrderBean.getComponent()
                    .getPaymentDetails().getCommerceItems());
        } else if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getElectronicGiftCardCommerceItems()) {
            combinedList.addAll(reviewOrderBean.getComponent()
                    .getPaymentDetails().getElectronicGiftCardCommerceItems());
        }
        // List<CheckoutCommerceItemBean>
        // commerceItems=reviewOrderBean.getComponent().getPaymentDetails().getCommerceItems();
        setListAdapter(new SubmitOrderFragmentAdapter(combinedList));
    }

    /**
     * The Class SubmitOrderFragmentAdapter.
     */
    class SubmitOrderFragmentAdapter extends BaseAdapter {
        List<CheckoutCommerceItemBean> commerceItems;

        public SubmitOrderFragmentAdapter(
                List<CheckoutCommerceItemBean> commerceItems) {
            super();
            this.commerceItems = commerceItems;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return commerceItems.size();
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int arg0) {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.widget.Adapter#getView(int, android.view.View,
         * android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            CheckoutCommerceItemBean bean = commerceItems.get(position);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.submit_order_list_item, null);

			/*
			 * if (bean.isGiftWrapItem()) {
			 * mGiftBoxAndNoteLayout.setVisibility(View.VISIBLE);
			 * mGiftBoxAndNoteTextViewValue.setText("$" + bean.getAmount());
			 * RelativeLayout productListRelativeLayout = (RelativeLayout) view
			 * .findViewById(R.id.reviewOrderProductListLayout);
			 * productListRelativeLayout.setVisibility(View.GONE);
			 * view.setVisibility(View.GONE); } else {
			 */

            ImageView pdtImage = (ImageView) view
                    .findViewById(R.id.submitOrderListItemImg);
			/*
			 * ImageView giftImage=(ImageView)
			 * view.findViewById(R.id.submitOrderListItemGiftImg);
			 */
            TextView pdtName = (TextView) view
                    .findViewById(R.id.submitOrderListItemPdtName);
            TextView brand = (TextView) view
                    .findViewById(R.id.submitOrderListItemBrandName);
            TextView quantity = (TextView) view
                    .findViewById(R.id.submitOrderListItemQunatity);
            TextView price = (TextView) view
                    .findViewById(R.id.submitOrderListItemPrice);
            TextView totalPrice = (TextView) view
                    .findViewById(R.id.reviewOrderTotalPrice);
            TextView freeTextView = (TextView) view
                    .findViewById(R.id.freeTextView);
            TextView oneForFreeTextView = (TextView) view
                    .findViewById(R.id.oneForFreeTextView);

            new AQuery(pdtImage).image(bean.getSmallImageUrl(), true, false,
                    200, R.drawable.dummy_product, null, AQuery.FADE_IN);
			/*
			 * if(bean.isGWP()){ price.setText("FREE");
			 * giftImage.setVisibility(View.VISIBLE);
			 * price.setTextColor(color.roseBudCherry); } else
			 * if(null!=bean.getIsFreeSample() &&
			 * bean.getIsFreeSample().equalsIgnoreCase("true")){
			 * price.setText("FREE"); price.setTextColor(color.roseBudCherry); }
			 */
			/* else{ */
            price.setText(getResources().getString(
                    R.string.review_order_multiply)
                    + getResources().getString(R.string.dollar_sign)
                    + String.format("%.2f", Double.valueOf(bean.getListPrice())));
			/* } */
            if (bean.isGWP()) {
                int tempQuantity = 1;

                if (null != bean.getQuantity()) {
                    tempQuantity = Integer.parseInt(bean.getQuantity());
                }
                if (tempQuantity > 1) {
                    oneForFreeTextView.setVisibility(View.VISIBLE);
                    oneForFreeTextView.setText("1 For Free,remaining for $"
                            + bean.getAmount());
                    price.setVisibility(View.GONE);
                    totalPrice.setVisibility(View.VISIBLE);
                    quantity.setVisibility(View.VISIBLE);
                    freeTextView.setVisibility(View.GONE);
                } else {
                    totalPrice.setText("FREE");
                    price.setVisibility(View.GONE);
                    totalPrice.setVisibility(View.VISIBLE);
                    quantity.setVisibility(View.VISIBLE);
                    freeTextView.setVisibility(View.GONE);
                }
            }

            totalPrice.setText(getResources().getString(R.string.dollar_sign)
                    + String.format("%.2f", Double.valueOf(bean.getAmount())));
            pdtName.setText(bean.getDisplayName());
            brand.setText(bean.getBrandName());
            quantity.setText(bean.getQuantity());
            // price.setText(Double.valueOf(bean.getAmount()).toString());
			/* } */

            return view;
        }
    }

    public void setAddressAndCreditCardDetails() {

        textShippingAddressLine1 = (TextView) footer
                .findViewById(R.id.txtShippingAddress1);
        textShippingAddressLine2 = (TextView) footer
                .findViewById(R.id.txtShippingAddress2);
        textBillingAddressLine1 = (TextView) footer
                .findViewById(R.id.txtBillingAddress1);
        textBillingAddressLine2 = (TextView) footer
                .findViewById(R.id.txtBillingAddress2);
        textCardDetailsLine1 = (TextView) footer
                .findViewById(R.id.txtCardDetails1);
        textCardDetailsLine2 = (TextView) footer
                .findViewById(R.id.txtCardDetails2);

        shippingAddressLayout = (LinearLayout) footer
                .findViewById(R.id.reviewOrderShippningAddressLayout);
        billingAddressLayout = (LinearLayout) footer
                .findViewById(R.id.reviewOrderBillingAddressLayout);
        creditCardLayout = (LinearLayout) footer
                .findViewById(R.id.reviewOrderCreditCardLayout);

        if (UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
            shippingAddressLayout.setVisibility(View.GONE);
        }
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getHardGoodShippingGroups()
                && !reviewOrderBean.getComponent().getPaymentDetails()
                .getHardGoodShippingGroups().isEmpty()) {
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getFirstName())
                strShippingFirstName = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getFirstName();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getLastName())
                strShippingLastName = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getLastName();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getAddress1())
                strShippingAddressLine1 = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getAddress1();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getState())
                strShippingState = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getState();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getCity())
                strShippingCity = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getCity();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getHardGoodShippingGroups().get(0).getPostalCode())
                strShippingPostalCode = reviewOrderBean.getComponent()
                        .getPaymentDetails().getHardGoodShippingGroups().get(0)
                        .getPostalCode();

            textShippingAddressLine1.setText(strShippingFirstName + " "
                    + strShippingLastName);
            textShippingAddressLine2.setText(strShippingAddressLine1 + " "
                    + strShippingCity + " " + strShippingState + " "
                    + strShippingPostalCode);
        }

        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups()
                && !(reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().isEmpty())) {
            billingAddressLayout.setVisibility(View.VISIBLE);
            mBillingAddressHeadingTextView.setVisibility(View.VISIBLE);
            strBillingFirstName = reviewOrderBean.getComponent()
                    .getPaymentDetails().getCreditCardPaymentGroups().get(0)
                    .getFirstName();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getLastName())
                strBillingLastName = reviewOrderBean.getComponent()
                        .getPaymentDetails().getCreditCardPaymentGroups()
                        .get(0).getLastName();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getAddress1())
                strBillingAddressLine1 = reviewOrderBean.getComponent()
                        .getPaymentDetails().getCreditCardPaymentGroups()
                        .get(0).getAddress1();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getCity())
                strBillingCity = reviewOrderBean.getComponent()
                        .getPaymentDetails().getCreditCardPaymentGroups()
                        .get(0).getCity();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getState())
                strBillingState = reviewOrderBean.getComponent()
                        .getPaymentDetails().getCreditCardPaymentGroups()
                        .get(0).getState();
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getPostalCode())
                strBillingPostalCode = reviewOrderBean.getComponent()
                        .getPaymentDetails().getCreditCardPaymentGroups()
                        .get(0).getPostalCode();
            textBillingAddressLine1.setText(strBillingFirstName + " "
                    + strBillingLastName);
            textBillingAddressLine2.setText(strBillingAddressLine1 + " "
                    + strBillingCity + " " + strBillingState + " "
                    + strBillingPostalCode);
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getGiftCardPaymentGroups()
                    && !reviewOrderBean.getComponent().getPaymentDetails()
                    .getGiftCardPaymentGroups().isEmpty()) {
                strCardType = reviewOrderBean.getComponent()
                        .getPaymentDetails().getGiftCardPaymentGroups().get(0)
                        .getPaymentMethod();

                strCardType = getCardtype(strCardType);

                strCardNumber = reviewOrderBean.getComponent()
                        .getPaymentDetails().getGiftCardPaymentGroups().get(0)
                        .getGiftCardNumber();

                textCardDetailsLine1.setText(strCardType
                        + " "
                        + strCardNumber
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails()
                                .getGiftCardPaymentGroups().get(0)
                                .getAmount())));
                // This method will get the new values to variables from credit
                // card details
                getCreditCardInfo();
                if(checkIfExpirationNeeded(strCreditCardType)) {
                    textCardDetailsLine2.setText(strCardType
                            + " "
                            + strCreditCardType
                            + " : "
                            + strCardNumber
                            + "\n"
                            + "Amount : "
                            + "$"
                            + String.format(
                            "%.2f",
                            Double.valueOf(reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getCreditCardPaymentGroups().get(0)
                                    .getAmount())) + "\n" + "Expires On : "
                            + strCardExpiryMonth + "/" + strCardExpiryYear);
                }
                else
                {
                    textCardDetailsLine2.setText(strCardType
                            + " "
                            + strCreditCardType
                            + " : "
                            + strCardNumber
                            + "\n"
                            + "Amount : "
                            + "$"
                            + String.format(
                            "%.2f",
                            Double.valueOf(reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getCreditCardPaymentGroups().get(0)
                                    .getAmount())));
                }
            } else {
                // This method will get the new values to variables from credit
                // card details
                getCreditCardInfo();
                if(checkIfExpirationNeeded(strCreditCardType)) {
                    textCardDetailsLine1.setText(strCardType
                            + " "
                            + strCreditCardType
                            + " : "
                            + strCardNumber
                            + "\n"
                            + "Amount : "
                            + "$"
                            + String.format(
                            "%.2f",
                            Double.valueOf(reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getCreditCardPaymentGroups().get(0)
                                    .getAmount())));
                    textCardDetailsLine2.setText("Expires On : "
                            + strCardExpiryMonth + "/" + strCardExpiryYear);
                }
                else
                {
                    textCardDetailsLine1.setText(strCardType
                            + " "
                            + strCreditCardType
                            + " : "
                            + strCardNumber
                            + "\n"
                            + "Amount : "
                            + "$"
                            + String.format(
                            "%.2f",
                            Double.valueOf(reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getCreditCardPaymentGroups().get(0)
                                    .getAmount())));
//                    textCardDetailsLine2.setText("Expires On : "
//                            + strCardExpiryMonth + "/" + strCardExpiryYear);
                }
            }
        } else if (reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups() != null
                && reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().isEmpty()
                && null != reviewOrderBean.getComponent().getPaymentDetails()
                .getGiftCardPaymentGroups()
                && !(reviewOrderBean.getComponent().getPaymentDetails()
                .getGiftCardPaymentGroups().isEmpty())) {
            billingAddressLayout.setVisibility(View.GONE);
            mBillingAddressHeadingTextView.setVisibility(View.GONE);
            strCardType = reviewOrderBean.getComponent().getPaymentDetails()
                    .getGiftCardPaymentGroups().get(0).getPaymentMethod();

            strCardType = getCardtype(strCardType);

            strCardNumber = reviewOrderBean.getComponent().getPaymentDetails()
                    .getGiftCardPaymentGroups().get(0).getGiftCardNumber();
            textCardDetailsLine1.setText(strCardType
                    + " "
                    + strCardNumber
                    + "\n"
                    + "Amount : "
                    + "$"
                    + String.format(
                    "%.2f",
                    Double.valueOf(reviewOrderBean.getComponent()
                            .getPaymentDetails()
                            .getGiftCardPaymentGroups().get(0)
                            .getAmount())));
            if (reviewOrderBean.getComponent().getPaymentDetails()
                    .getPaypalPaymentGroups() != null
                    && !reviewOrderBean.getComponent().getPaymentDetails()
                    .getPaypalPaymentGroups().isEmpty()) {
                textCardDetailsLine1.append("\n"
                        + "PayPal "
                        + reviewOrderBean.getComponent().getEmail()
                        + "\n"
                        + "Amount : "
                        + "$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails()
                                .getPaypalPaymentGroups().get(0)
                                .getAmount())));

            }

            textCardDetailsLine2.setVisibility(View.GONE);
        } else if (reviewOrderBean.getComponent().getPaymentDetails()
                .getPaypalPaymentGroups() != null
                && !reviewOrderBean.getComponent().getPaymentDetails()
                .getPaypalPaymentGroups().isEmpty()) {
            billingAddressLayout.setVisibility(View.GONE);
            mBillingAddressHeadingTextView.setVisibility(View.GONE);
            textCardDetailsLine1.setText("PayPal Account");
            if (reviewOrderBean.getComponent().getEmail() != null
                    && !reviewOrderBean.getComponent().getEmail().isEmpty()) {
                textCardDetailsLine2.setText(reviewOrderBean.getComponent()
                        .getEmail());

                if (null != reviewOrderBean.getComponent().getPaymentDetails()
                        .getPaypalPaymentGroups().get(0)) {
                    if (null != reviewOrderBean.getComponent()
                            .getPaymentDetails().getPaypalPaymentGroups()
                            .get(0).getAmount()) {
                        textCardDetailsLine2.append("\n"
                                + "Amount : "
                                + "$"
                                + String.format(
                                "%.2f",
                                Double.valueOf(reviewOrderBean
                                        .getComponent()
                                        .getPaymentDetails()
                                        .getPaypalPaymentGroups()
                                        .get(0).getAmount())));
                    }

                }

            }
        } else {
            billingAddressLayout.setVisibility(View.GONE);
        }

    }

    private void getCreditCardInfo() {
        strCardType = reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().get(0).getPaymentMethod();

        strCardType = getCardtype(strCardType);

        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().get(0).getCreditCardNumber()) {
            String numberOfCross = "";
            strCardNumber = reviewOrderBean.getComponent().getPaymentDetails()
                    .getCreditCardPaymentGroups().get(0).getCreditCardNumber();
            for (int i = 0; i < strCardNumber.length() - 4; i++) {
                numberOfCross = numberOfCross + "x";
            }
            strCardNumber = numberOfCross
                    + strCardNumber.substring(strCardNumber.length() - 4,
                    strCardNumber.length());
        }
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().get(0).getExpirationMonth())
            strCardExpiryMonth = reviewOrderBean.getComponent()
                    .getPaymentDetails().getCreditCardPaymentGroups().get(0)
                    .getExpirationMonth();
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().get(0).getExpirationYear())
            strCardExpiryYear = reviewOrderBean.getComponent()
                    .getPaymentDetails().getCreditCardPaymentGroups().get(0)
                    .getExpirationYear();
        if (null != reviewOrderBean.getComponent().getPaymentDetails()
                .getCreditCardPaymentGroups().get(0).getCreditCardType()) {
            strCreditCardType = reviewOrderBean.getComponent()
                    .getPaymentDetails().getCreditCardPaymentGroups().get(0)
                    .getCreditCardType();
        }
    }

    private String getCardtype(String strCardTypeValue) {
        if (strCardTypeValue.equalsIgnoreCase("creditcard"))
            strCardTypeValue = "Credit Card";
        else if (strCardTypeValue.equalsIgnoreCase("giftCard"))
            strCardTypeValue = "GiftCard";
        return strCardTypeValue;
    }
    /**
     * Check if the card requires expiration date
     * @param cardType
     */
    public boolean checkIfExpirationNeeded(String cardType)
    {
        CreditCardInfoBean singleCardInfo = null;
        boolean expirationNeeded=true;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo&&cardType.equalsIgnoreCase(singleCardInfo.getCardType())) {
                        if (!singleCardInfo.getCardUsesExpirationDate()) {
                            expirationNeeded=false;
                        }
                    }
                }
            }
        }
        return expirationNeeded;
    }
}
