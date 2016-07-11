/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.checkout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.checkout.CheckoutCommerceItemBean;
import com.ulta.core.bean.checkout.CheckoutOrderAdjustmentBean;
import com.ulta.core.bean.checkout.CheckoutShippingMethodDetailsBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.RedeemLevelPointsBean;
import com.ulta.core.bean.product.RedeemPointBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class ShippingMethodActivity.
 */
public class ShippingMethodActivity extends UltaBaseActivity implements
        OnDoneClickedListener, OnSessionTimeOut {

    /**
     * The btn submit.
     */
    Button btnSubmit, titleBarButton;

    /**
     * The shipping method.
     */
    RadioGroup shippingMethod;
    private boolean onlyGiftCard = true;
    /**
     * The ups next day air.
     */
    TextView groundShipping, upsTwoDayAir, upsNextDayAir;

    /**
     * The txt second day air.
     */
    TextView txtGroundShipping, txtNextDayAir, txtSecondDayAir;

    private RadioButton standardradioButton;
    private RadioButton upsNextDayAirRadioButton;
    private RadioButton upsSeconDayAirRadioButton;

    private LinearLayout mStandardGroundLayout;
    private LinearLayout mUpsTwoDayAirLayout;
    private LinearLayout mUPSNextDayAirLayout;
    private LinearLayout mfooterAndButtonLayout;

    private ImageView mShippingGroundImageView;
    private ImageView mShippingUPSTwoDayAirImageView;
    private ImageView mShippingUPSNextDayAirImageView;

    private TextView mDefaultGroundTextView;
    private TextView mDefaultTwoDayTextView;
    private TextView mDefaultNextDayTextView;
    private TextView mShippingTypeHeader;
    private TextView mChoosePreferredShippingAddress;
    private TextView mEstimatedDelivaryTV;

    private RelativeLayout mStandardGroundRelativeLayout;
    private RelativeLayout mUpsTwoDayAirRelativeLayout;
    private RelativeLayout mUpsNextDayAirRelativeLayout;

    private ImageButton mBackToAddressButton;
    private ImageButton mNextbutton;
    private TextView mHazmatTextView;

    private ProgressDialog mProgressDialog;

    private double mshippingFreeAmount;
    private double mshippingStandardAmount;
    private double mshippingUpsNextDayAmount;
    private double mshippingUpsSeconddayAmount;
    private double mshippingAmount;

    // private TextView txtBanner;

    /**
     * The shipping methodfor product.
     */
    String shippingMethodforProduct;

    /**
     * The loading layout.
     */
    FrameLayout loadingLayout;

    double total = 0.0;

    /**
     * The choice.
     */
    String choice = "ups_ground";

    /**
     * The formdata.
     */
    HashMap<String, String> formdata = new HashMap<String, String>();
    // private UemAction shippingOptionsActivity;
    private TextView mCheckout_shipping, mCheckout_payment,
            mCheckout_review_order;
    private CheckoutShippmentMethodBean checkoutShippmentMethodBean;
    CheckoutShippmentMethodBean ultaBean;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadingLayout.setVisibility(View.GONE);
        titleBarButton.setVisibility(View.VISIBLE);
        // if (UltaDataCache.getDataCacheInstance().isMovingBackInChekout()) {
        // finish();
        // }

        if (!UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {
            // mNextbutton
            // .setBackgroundResource(R.drawable.checkout_payment_enabled);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mNextbutton.setImageDrawable(getResources().getDrawable(
                        R.drawable.checkout_payment_enabled));
            } else {
                mNextbutton
                        .setBackgroundResource(R.drawable.checkout_payment_enabled);
            }

			/*
             * mNextbutton.setText(getResources().getString(
			 * R.string.shippingMethodPaymentButton));
			 */
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_method);
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initFooterViews();
        initViews();
        creatingPageName();
        trackAppAction(ShippingMethodActivity.this,
                WebserviceConstants.CHECKOUT_STEP_3_EVENT_ACTION);

        trackAppAction(ShippingMethodActivity.this,
                WebserviceConstants.CHECKOUT_STEP_3_VISIT_EVENT_ACTION);
        checkoutShippmentMethodBean = (CheckoutShippmentMethodBean) getIntent()
                .getExtras().get("ShippingMethodBean");

        if (null != checkoutShippmentMethodBean.getComponent().getCart()
                .getCommerceItems()) {
            for (int j = 0; j < checkoutShippmentMethodBean.getComponent()
                    .getCart().getCommerceItems().size(); j++) {

                if (null != checkoutShippmentMethodBean.getComponent()
                        .getCart().getCommerceItems().get(j).getHazmatCode()) {
                    if (checkoutShippmentMethodBean.getComponent().getCart()
                            .getCommerceItems().get(j).getHazmatCode()
                            .equalsIgnoreCase("H")) {
                        if (null == checkoutShippmentMethodBean.getComponent()
                                .getShippingMethodsAndRedeemLevels()
                                .getAvailableShippingMethods()
                                || checkoutShippmentMethodBean.getComponent()
                                .getShippingMethodsAndRedeemLevels()
                                .getAvailableShippingMethods().size() == 0) {
                            if (null != checkoutShippmentMethodBean
                                    .getComponent()
                                    .getShippingMethodsAndRedeemLevels()
                                    .getShippingErrorMessage()) {
                                mHazmatTextView
                                        .setText(checkoutShippmentMethodBean
                                                .getComponent()
                                                .getShippingMethodsAndRedeemLevels()
                                                .getShippingErrorMessage());
                            }
                            mNextbutton.setVisibility(View.INVISIBLE);
                            mChoosePreferredShippingAddress
                                    .setVisibility(View.GONE);
                            mEstimatedDelivaryTV.setVisibility(View.GONE);
                            mHazmatTextView.setVisibility(View.VISIBLE);
                        } else {
                            if (null != checkoutShippmentMethodBean
                                    .getComponent()
                                    .getShippingMethodsAndRedeemLevels()
                                    .getShippingErrorMessage()
                                    && checkoutShippmentMethodBean
                                    .getComponent()
                                    .getShippingMethodsAndRedeemLevels()
                                    .getShippingErrorMessage().length() > 0) {
                                mHazmatTextView
                                        .setText(checkoutShippmentMethodBean
                                                .getComponent()
                                                .getShippingMethodsAndRedeemLevels()
                                                .getShippingErrorMessage());
                            }
                            mHazmatTextView.setVisibility(View.VISIBLE);
                        }

                    }
                }

                if (null != checkoutShippmentMethodBean.getComponent()
                        .getCart().getCommerceItems().get(j)
                        .getIsElectronicGiftCard()
                        && null != checkoutShippmentMethodBean.getComponent()
                        .getCart().getCommerceItems().get(j)
                        .getIsGiftCard()) {
                    if (checkoutShippmentMethodBean.getComponent().getCart()
                            .getCommerceItems().get(j)
                            .getIsElectronicGiftCard()
                            .equalsIgnoreCase("false")
                            && checkoutShippmentMethodBean.getComponent()
                            .getCart().getCommerceItems().get(j)
                            .getIsGiftCard().equalsIgnoreCase("false"))
                        onlyGiftCard = false;
                } else {
                    onlyGiftCard = false;
                }
            }
            if (!onlyGiftCard) {
                UltaDataCache.getDataCacheInstance().setOnlyEgiftCard(false);
            }
            for (int i = 0; i < checkoutShippmentMethodBean.getComponent()
                    .getCart().getCommerceItems().size(); i++) {
                CheckoutCommerceItemBean commerceItem = checkoutShippmentMethodBean
                        .getComponent().getCart().getCommerceItems().get(i);

                if (null != commerceItem.getIsElectronicGiftCard()
                        && commerceItem.getIsElectronicGiftCard()
                        .equalsIgnoreCase("false")
                        && ("false").equalsIgnoreCase(commerceItem
                        .getIsGiftCard())) {
                    // 3.3
                    total = total
                            + checkoutShippmentMethodBean.getComponent()
                            .getCart().getCommerceItems().get(i)
                            .getAmount();
                }
            }
        }
        if (!UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
            if (null != checkoutShippmentMethodBean.getComponent().getCart()
                    .getOrderDetails()) {
                Logger.Log("Value---------------------------------" + total);
                /*
                 * if (total < Double.valueOf(checkoutShippmentMethodBean
				 * .getComponent().getCart().getOrderDetails()
				 * .getFreeShippingAmount())) { //
				 * txtBanner.setVisibility(View.VISIBLE); //
				 * bannerLayout.setVisibility(View.VISIBLE); //
				 * viewBanner.setVisibility(View.VISIBLE);
				 * 
				 * txtBanner.setText("Add $" + String.format( "%.2f",
				 * (Double.valueOf(checkoutShippmentMethodBean
				 * .getComponent().getCart() .getOrderDetails()
				 * .getFreeShippingAmount()) - total)) +
				 * " more for Free Shipping");
				 * 
				 * } else { // txtBanner.setVisibility(View.GONE); //
				 * bannerLayout.setVisibility(View.GONE); //
				 * viewBanner.setVisibility(View.GONE); }
				 */
            }
        }
        List<CheckoutShippingMethodDetailsBean> listOfShippingMethods = checkoutShippmentMethodBean
                .getComponent().getShippingMethodsAndRedeemLevels()
                .getAvailableShippingMethods();
        for (int i = 0; i <= listOfShippingMethods.size() - 1; i++) {
            if (listOfShippingMethods.get(i).getShippingMethod() == null) {
                continue;
            }
            String shippingMsg = listOfShippingMethods.get(i)
                    .getEstDateOfDelivery();
            if (listOfShippingMethods.get(i).getShippingMethod().toString()
                    .equalsIgnoreCase("free_shipping")) {
                groundShipping.setVisibility(View.VISIBLE);
                txtGroundShipping.setVisibility(View.VISIBLE);
                if (shippingMsg != null && !shippingMsg.isEmpty()) {
                    txtGroundShipping.setText(shippingMsg);
                }
                mStandardGroundLayout.setVisibility(View.VISIBLE);
                choice = listOfShippingMethods.get(i).getShippingMethod()
                        .toString();
                shippingMethodforProduct = choice;
                groundShipping.setText(String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mFreeAmount = String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount()));
                mshippingFreeAmount = listOfShippingMethods.get(i).getAmount();
                mshippingAmount = mshippingFreeAmount;
                mShippingTypeTextView.setText(getResources().getString(
                        R.string.checkout_standard_shipping_type));
                UltaDataCache.getDataCacheInstance().setShippingType(
                        getResources().getString(
                                R.string.checkout_standard_shipping_type));
                mShippingGroundImageView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.truck));
                mStandardGroundRelativeLayout.setVisibility(View.VISIBLE);
                mShippingGroundImageView.setVisibility(View.VISIBLE);
                mDefaultGroundTextView.setVisibility(View.VISIBLE);
                standardradioButton.setVisibility(View.VISIBLE);

            } else if (listOfShippingMethods.get(i).getShippingMethod()
                    .toString().equalsIgnoreCase("ups_ground")) {
                groundShipping.setVisibility(View.VISIBLE);
                txtGroundShipping.setVisibility(View.VISIBLE);
                if (shippingMsg != null && !shippingMsg.isEmpty()) {
                    txtGroundShipping.setText(shippingMsg);
                }
                mStandardGroundLayout.setVisibility(View.VISIBLE);
                choice = listOfShippingMethods.get(i).getShippingMethod()
                        .toString().trim();
                shippingMethodforProduct = choice;
                groundShipping.setText(String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mStandardAmount = (String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mShippingTypeTextView.setText(getResources().getString(
                        R.string.checkout_standard_shipping_type));
                UltaDataCache.getDataCacheInstance().setShippingType(
                        getResources().getString(
                                R.string.checkout_standard_shipping_type));
                mshippingStandardAmount = Double.valueOf(listOfShippingMethods
                        .get(i).getAmount());
                mshippingAmount = mshippingStandardAmount;
                mShippingGroundImageView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.truck));
                mStandardGroundRelativeLayout.setVisibility(View.VISIBLE);
                mShippingGroundImageView.setVisibility(View.VISIBLE);
                mDefaultGroundTextView.setVisibility(View.VISIBLE);
                standardradioButton.setVisibility(View.VISIBLE);

            } else if (listOfShippingMethods.get(i).getShippingMethod()
                    .toString().equalsIgnoreCase("ups_next_day")) {
                upsNextDayAir.setVisibility(View.VISIBLE);
                txtNextDayAir.setVisibility(View.VISIBLE);
                if (shippingMsg != null && !shippingMsg.isEmpty()) {
                    txtNextDayAir.setText(shippingMsg);
                }
                mUPSNextDayAirLayout.setVisibility(View.VISIBLE);
                upsNextDayAir.setText(String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));

                mUpsNextDayAirAmount = (String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mshippingUpsNextDayAmount = Double
                        .valueOf(listOfShippingMethods.get(i).getAmount());
                mShippingUPSNextDayAirImageView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.plane));
                mUpsNextDayAirRelativeLayout.setVisibility(View.VISIBLE);
                mShippingUPSNextDayAirImageView.setVisibility(View.VISIBLE);
                mDefaultNextDayTextView.setVisibility(View.VISIBLE);

            } else if (listOfShippingMethods.get(i).getShippingMethod()
                    .toString().equalsIgnoreCase("ups_second_day")) {
                upsTwoDayAir.setVisibility(View.VISIBLE);
                txtSecondDayAir.setVisibility(View.VISIBLE);
                if (shippingMsg != null && !shippingMsg.isEmpty()) {
                    txtSecondDayAir.setText(shippingMsg);
                }
                mUpsTwoDayAirLayout.setVisibility(View.VISIBLE);
                upsTwoDayAir.setText(String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mUpsTwoDayAirAmount = (String
                        .format(getResources().getString(R.string.dollar_sign)
                                + "%.2f", Double.valueOf(listOfShippingMethods
                                .get(i).getAmount())));
                mshippingUpsSeconddayAmount = Double
                        .valueOf(listOfShippingMethods.get(i).getAmount());
                mShippingUPSTwoDayAirImageView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.plane));
                mUpsTwoDayAirRelativeLayout.setVisibility(View.VISIBLE);
                mShippingUPSTwoDayAirImageView.setVisibility(View.VISIBLE);
                mDefaultTwoDayTextView.setVisibility(View.VISIBLE);

            }
        }
        setFooterValues(checkoutShippmentMethodBean);
        mfooterAndButtonLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * On form submit.
     */
    private void onFormSubmit() {
        formdata.put("shippingOption", shippingMethodforProduct);
        // loadingLayout.setVisibility(View.VISIBLE);
        mProgressDialog = new ProgressDialog(ShippingMethodActivity.this);
        setProgressDialogLoadingColor(mProgressDialog);
        mProgressDialog.setMessage("Loading..");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        titleBarButton.setVisibility(View.GONE);
        // shippingOptionsActivity = UemAction
        // .enterAction(WebserviceConstants.ACTION_VERIFY_SHIPMENT_INVOCATION);
        // shippingOptionsActivity.reportEvent("Move to billing is called");
        invokeMoveToBilling();
    }

    /**
     * Invoke move to billing.
     */
    private void invokeMoveToBilling() {

        InvokerParams<CheckoutShippmentMethodBean> invokerParams = new InvokerParams<CheckoutShippmentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.MOVE_TO_BILLING_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateShippingMethodParameters());
        invokerParams.setUltaBeanClazz(CheckoutShippmentMethodBean.class);
        ShippingMethodHandler userCreatedHandler = new ShippingMethodHandler();
        invokerParams.setUltaHandler(userCreatedHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<BillingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate shipping method parameters.
     *
     * @return the map
     */
    private Map<String, String> populateShippingMethodParameters() {
        HashMap<String, String> shippingMethodMap = new HashMap<String, String>();
        shippingMethodMap.put("atg-rest-output", "json");
        shippingMethodMap
                .put("atg-rest-return-form-handler-properties", "true");
        shippingMethodMap
                .put("atg-rest-return-form-handler-exceptions", "true");
        shippingMethodMap.put("atg-rest-depth", "1");
        shippingMethodMap.put("shippingMethod", shippingMethodforProduct);
        return shippingMethodMap;
    }

    /**
     * Inits the views.
     */
    @SuppressWarnings("deprecation")
    private void initViews() {
        titleBarButton = (Button) findViewById(R.id.title_bar_done_icon);
        loadingLayout = (FrameLayout) findViewById(R.id.loadingDialog);
        // formLayout = (LinearLayout) findViewById(R.id.linearLayout2);
        shippingMethod = (RadioGroup) findViewById(R.id.shippingMethod);
        groundShipping = (TextView) findViewById(R.id.groundShipping);
        upsTwoDayAir = (TextView) findViewById(R.id.upsTwoDayAir);
        upsNextDayAir = (TextView) findViewById(R.id.upsNextDayAir);
        txtGroundShipping = (TextView) findViewById(R.id.textGroundShipping);
        txtNextDayAir = (TextView) findViewById(R.id.textNextDayAir);
        txtSecondDayAir = (TextView) findViewById(R.id.textSecondDayAir);
        mEstimatedDelivaryTV = (TextView) findViewById(R.id.estimatedDelivaryTV);

        standardradioButton = (RadioButton) findViewById(R.id.standardRadioButton);
        upsNextDayAirRadioButton = (RadioButton) findViewById(R.id.UPSNextDayAirRadioButton);
        upsSeconDayAirRadioButton = (RadioButton) findViewById(R.id.UPS2ndDayAirRadioButton);

        mStandardGroundLayout = (LinearLayout) findViewById(R.id.standardGroundShippingLayout);
        mUpsTwoDayAirLayout = (LinearLayout) findViewById(R.id.ups2dayAirLayout);
        mUPSNextDayAirLayout = (LinearLayout) findViewById(R.id.upsNextDayAirLayout);
        mfooterAndButtonLayout = (LinearLayout) findViewById(R.id.footerAndButtonLayout);

        mShippingGroundImageView = (ImageView) findViewById(R.id.shipping_truckIcon);
        mShippingUPSTwoDayAirImageView = (ImageView) findViewById(R.id.shipping_ups2ndDayAirIcon);
        mShippingUPSNextDayAirImageView = (ImageView) findViewById(R.id.shipping_upsNextDayAirIcon);

        mDefaultGroundTextView = (TextView) findViewById(R.id.defaultGroundmessageTextView);
        mDefaultTwoDayTextView = (TextView) findViewById(R.id.default2dayssageTextView);
        mDefaultNextDayTextView = (TextView) findViewById(R.id.defaultnextDaymessageTextView);
        mShippingTypeHeader = (TextView) findViewById(R.id.shippingtypeHeader);
        mChoosePreferredShippingAddress = (TextView) findViewById(R.id.choosePreferredShippingType);

        mStandardGroundRelativeLayout = (RelativeLayout) findViewById(R.id.groundShippingRelativeLayout);
        mUpsNextDayAirRelativeLayout = (RelativeLayout) findViewById(R.id.upsNextDayAirRelativelayout);
        mUpsTwoDayAirRelativeLayout = (RelativeLayout) findViewById(R.id.ups2ndDayAirRelativeLayout);

        mHazmatTextView = (TextView) findViewById(R.id.hazmatTextView);

        mBackToAddressButton = (ImageButton) findViewById(R.id.backBtn);
        mNextbutton = (ImageButton) findViewById(R.id.giftBoxdetailsBtn);
        // bannerLayout = (LinearLayout) findViewById(R.id.banner);
        // txtBanner = (TextView) findViewById(R.id.txtShippingBanner);
        // viewBanner = (View) findViewById(R.id.viewBanner);
        // view1 = (View) findViewById(R.id.view1);
        // view2 = (View) findViewById(R.id.view2);
        // view3 = (View) findViewById(R.id.view3);
        // 3.2 release
        groundShipping.setTypeface(setHelveticaRegulartTypeFace());
        upsTwoDayAir.setTypeface(setHelveticaRegulartTypeFace());
        upsNextDayAir.setTypeface(setHelveticaRegulartTypeFace());
        txtGroundShipping.setTypeface(setHelveticaRegulartTypeFace());
        txtNextDayAir.setTypeface(setHelveticaRegulartTypeFace());
        txtSecondDayAir.setTypeface(setHelveticaRegulartTypeFace());
        mDefaultGroundTextView.setTypeface(setHelveticaRegulartTypeFace());
        mDefaultTwoDayTextView.setTypeface(setHelveticaRegulartTypeFace());
        mDefaultNextDayTextView.setTypeface(setHelveticaRegulartTypeFace());
        mShippingTypeHeader.setTypeface(setHelveticaRegulartTypeFace());

        mCheckout_shipping = (TextView) findViewById(R.id.checkout_shipping);
        mCheckout_payment = (TextView) findViewById(R.id.checkout_payment);
        mCheckout_review_order = (TextView) findViewById(R.id.checkout_review_order);
        mCheckout_shipping.setBackgroundColor(getResources().getColor(
                R.color.chekout_header_highlighted));
        mCheckout_shipping.setTextColor(getResources().getColor(R.color.white));
        mCheckout_payment.setBackgroundColor(getResources().getColor(
                R.color.olapic_detail_caption));
        mCheckout_review_order.setBackgroundColor(getResources().getColor(
                R.color.olapic_detail_caption));
        mChoosePreferredShippingAddress
                .setTypeface(setHelveticaRegulartTypeFace());

        if (!UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {
            // mNextbutton
            // .setBackgroundResource(R.drawable.checkout_payment_enabled);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mNextbutton.setImageDrawable(getResources().getDrawable(
                        R.drawable.checkout_payment_enabled));
            } else {
                mNextbutton
                        .setBackgroundResource(R.drawable.checkout_payment_enabled);
            }

			/*
             * mNextbutton.setText(getResources().getString(
			 * R.string.shippingMethodPaymentButton));
			 */
        }

        mStandardGroundLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                standardradioButton.setChecked(true);
                standardradioButton.setVisibility(View.VISIBLE);
                upsSeconDayAirRadioButton.setVisibility(View.GONE);
                upsNextDayAirRadioButton.setVisibility(View.GONE);

                if (choice.equalsIgnoreCase("ups_ground")) {
                    shippingMethodforProduct = "ups_ground";
                    mShippingTypeValueTextView.setText(mStandardAmount);
                    try {
                        if (Double.valueOf(mStandardAmount).equals(0.0)) {
                            mShippingTypeValueTextView.setText("FREE");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mshippingAmount = mshippingStandardAmount;
                } else {
                    shippingMethodforProduct = "free_shipping";
                    mshippingAmount = mshippingFreeAmount;
                    mShippingTypeValueTextView.setText(mFreeAmount);
                    try {
                        if (Double.valueOf(mFreeAmount).equals(0.0)) {
                            mShippingTypeValueTextView.setText("FREE");
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                setFooterValues(checkoutShippmentMethodBean);
                mShippingTypeTextView.setText(getResources().getString(
                        R.string.checkout_standard_shipping_type));
                UltaDataCache.getDataCacheInstance().setShippingType(
                        getResources().getString(
                                R.string.checkout_standard_shipping_type));
            }
        });

        mUpsTwoDayAirLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                standardradioButton.setVisibility(View.GONE);
                upsSeconDayAirRadioButton.setChecked(true);
                upsSeconDayAirRadioButton.setVisibility(View.VISIBLE);
                upsNextDayAirRadioButton.setVisibility(View.GONE);
                shippingMethodforProduct = "ups_second_day";
                mshippingAmount = mshippingUpsSeconddayAmount;
                mShippingTypeTextView.setText(getResources().getString(
                        R.string.checkout_standard_Ups_2nd_day_type));
                UltaDataCache.getDataCacheInstance().setShippingType(
                        getResources().getString(
                                R.string.checkout_standard_Ups_2nd_day_type));
                mShippingTypeValueTextView.setText(mUpsTwoDayAirAmount);
                try {
                    if (Double.valueOf(mUpsTwoDayAirAmount).equals(0.0)) {
                        mShippingTypeValueTextView.setText("FREE");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setFooterValues(checkoutShippmentMethodBean);
            }
        });

        mUPSNextDayAirLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                standardradioButton.setVisibility(View.GONE);
                upsSeconDayAirRadioButton.setVisibility(View.GONE);
                upsNextDayAirRadioButton.setChecked(true);
                upsNextDayAirRadioButton.setVisibility(View.VISIBLE);
                shippingMethodforProduct = "ups_next_day";
                mshippingAmount = mshippingUpsNextDayAmount;
                mShippingTypeTextView.setText(getResources().getString(
                        R.string.checkout_standard_Ups_next_day_type));
                UltaDataCache.getDataCacheInstance().setShippingType(
                        getResources().getString(
                                R.string.checkout_standard_Ups_next_day_type));
                mShippingTypeValueTextView.setText(mUpsNextDayAirAmount);
                try {
                    if (Double.valueOf(mUpsNextDayAirAmount).equals(0.0)) {
                        mShippingTypeValueTextView.setText("FREE");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setFooterValues(checkoutShippmentMethodBean);
            }
        });

        mBackToAddressButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });

        mNextbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onFormSubmit();
            }
        });

        mExpandImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (toShowCouponCodeLayout % 2 != 0) {
                    mSubTotalFooterLayout.setVisibility(View.GONE);
                    mShippingTypeFooterLayout.setVisibility(View.GONE);
                    mTaxFooterLayout.setVisibility(View.GONE);
                    mExpandImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.plus));
                    mCouponDiscountLayout.setVisibility(View.GONE);
                    mAdditionalDiscountLayout.setVisibility(View.GONE);
                } else {
                    mSubTotalFooterLayout.setVisibility(View.VISIBLE);
                    mShippingTypeFooterLayout.setVisibility(View.VISIBLE);
                    mTaxFooterLayout.setVisibility(View.VISIBLE);
                    mExpandImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.minus));
                    if (null != mTvCouponDiscountValue.getText().toString().trim() && !mTvCouponDiscountValue.getText().toString().trim().isEmpty()) {
                        mCouponDiscountLayout.setVisibility(View.VISIBLE);
                    }
                    if (null != mTvAdditionalDiscountValue.getText().toString().trim() && !mTvAdditionalDiscountValue.getText().toString().trim().isEmpty()) {
                        mAdditionalDiscountLayout.setVisibility(View.VISIBLE);
                    }

                }
                toShowCouponCodeLayout++;
            }
        });

    }

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
     */
    @Override
    public void onDoneClicked() {
        onFormSubmit();
    }

    /**
     * The Class ShippingMethodHandler.
     */
    public class ShippingMethodHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            // Logger.Log("<UserCreatedHandler><handleMessage><getErrorMessage>>"
            // + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(ShippingMethodActivity.this);
                    // shippingOptionsActivity.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // shippingOptionsActivity.leaveAction();
                    mProgressDialog.dismiss();
                } else {
                    try {
                        // shippingOptionsActivity
                        // .reportError(
                        // WebserviceConstants.FORM_EXCEPTION_OCCURED,
                        // WebserviceConstants.DYN_ERRCODE_SHIPPING_METHOD_ACTIVITY);
                        // shippingOptionsActivity.leaveAction();
                        notifyUser(getErrorMessage(),
                                ShippingMethodActivity.this);
                        setError(ShippingMethodActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                    loadingLayout.setVisibility(View.GONE);
                    titleBarButton.setVisibility(View.VISIBLE);
                    // formLayout.setVisibility(View.VISIBLE);
                    mProgressDialog.dismiss();
                }
            } else {
                Logger.Log("<BillingAddress><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                /**/
                ultaBean = (CheckoutShippmentMethodBean) getResponseBean();
                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty())) {
                    mProgressDialog.dismiss();
                    if (errors.get(0).contains("error pricing the order")) {
                        mProgressDialog.dismiss();
                        try {
                            // shippingOptionsActivity
                            // .reportError(
                            // "Address details are entered incorrectly",
                            // WebserviceConstants.DYN_ERRCODE_SHIPPING_METHOD_ACTIVITY);
                            // shippingOptionsActivity.leaveAction();
                            notifyUser(
                                    "Please enter the address details correctly and try again",
                                    ShippingMethodActivity.this);
                            setError(ShippingMethodActivity.this,
                                    "Please enter the address details correctly and try again");
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                        // finish();
                    } else {
                        mProgressDialog.dismiss();
                        try {
                            // shippingOptionsActivity
                            // .reportError(
                            // errors.get(0),
                            // WebserviceConstants.DYN_ERRCODE_SHIPPING_METHOD_ACTIVITY);
                            // shippingOptionsActivity.leaveAction();
                            notifyUser(errors.get(0),
                                    ShippingMethodActivity.this);
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                    }
                    loadingLayout.setVisibility(View.GONE);
                    titleBarButton.setVisibility(View.VISIBLE);
                    // formLayout.setVisibility(View.VISIBLE);
                } else {

                    Logger.Log(">>>>>>"
                            + ultaBean.getComponent().getCart()
                            .getHardGoodShippingGroups().get(0)
                            .getShippingMethod());
                    // shippingOptionsActivity
                    // .reportEvent("Shipping Address & method successfully verified");
                    // shippingOptionsActivity.leaveAction();
                    if (UltaDataCache.getDataCacheInstance().isOnlyEgiftCard()) {
                        mProgressDialog.dismiss();
                        Intent goToPaymentPage = new Intent(
                                ShippingMethodActivity.this,
                                PaymentMethodActivity.class);
                        goToPaymentPage.putExtra("checkoutshipmentBean",
                                ultaBean);
                        startActivity(goToPaymentPage);
                    } else {
                        mProgressDialog.dismiss();
                        if (UltaDataCache.getDataCacheInstance()
                                .isGiftTheOrder()) {
                            Intent goToGiftOptions = new Intent(
                                    ShippingMethodActivity.this,
                                    GiftOptionActivity.class);
                            goToGiftOptions.putExtra("checkoutGiftOptionsBean",
                                    ultaBean);
                            startActivity(goToGiftOptions);
                        } else {
                            /*
                             * Intent goToPaymentPage = new Intent(
							 * ShippingMethodActivity.this,
							 * PaymentMethodActivity.class);
							 * goToPaymentPage.putExtra("checkoutshipmentBean",
							 * ultaBean); startActivity(goToPaymentPage);
							 */
                            // loadingLayout.setVisibility(View.VISIBLE);
                            invokeApplyGiftOption();
                        }
                    }
                }
            }
        }
    }

    public void setFooterValues(
            CheckoutShippmentMethodBean checkoutShippmentMethodBean) {
        try {
            if (null != checkoutShippmentMethodBean) {
                if (null != checkoutShippmentMethodBean.getComponent()) {
                    if (null != checkoutShippmentMethodBean.getComponent()
                            .getCart()) {
                        if (null != checkoutShippmentMethodBean.getComponent()
                                .getCart().getOrderDetails()) {

                            double shippingAmount = checkoutShippmentMethodBean
                                    .getComponent().getCart().getOrderDetails()
                                    .getShipping();
                            double subTotal = checkoutShippmentMethodBean
                                    .getComponent().getCart().getOrderDetails()
                                    .getRawSubtotal();
                            Double totalAdjustment = 0.0;
//							List<CheckoutOrderAdjustmentBean> orderAdjustments = checkoutShippmentMethodBean
//									.getComponent().getCart()
//									.getOrderAdjustments();
//							if (null != orderAdjustments
//									&& !orderAdjustments.isEmpty()) {
//								for (int i = 0; i < orderAdjustments.size(); i++) {
//									totalAdjustment = totalAdjustment
//											+ orderAdjustments.get(i)
//													.getTotalAdjustment();
//								}
//								mSubTotalValueTextView
//										.setText("$"
//												+ String.format(
//														"%.2f",
//														Double.valueOf(totalAdjustment)));
//							} else {
                            mSubTotalValueTextView.setText("$"
                                    + String.format("%.2f", (subTotal)));
//							}
                            //tiered price: Additional discount

                            if (null != checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount() &&
                                    !checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
                                mTvAdditionalDiscountValue.setText("-$"
                                        + String.format("%.2f",
                                        Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount())));
                                mAdditionalDiscountLayout.setVisibility(View.VISIBLE);
                            }


                            mTaxValueTextView
                                    .setText("$"
                                            + String.format("%.2f",
                                            checkoutShippmentMethodBean
                                                    .getComponent()
                                                    .getCart()
                                                    .getOrderDetails()
                                                    .getTax()));

                            mShippingTypeValueTextView.setText("$"
                                    + String.format("%.2f", mshippingAmount));
                            try {
                                if (Double.valueOf(mshippingAmount).equals(0.0)) {
                                    mShippingTypeValueTextView.setText("FREE");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            double total = checkoutShippmentMethodBean
                                    .getComponent().getCart().getOrderDetails()
                                    .getTotal();
                            // if (shippingAmount != 0.00
                            // && isShippingChanged) {
                            total = total - shippingAmount + mshippingAmount;
                            // }

                            mTotalValueTextView.setText("$"
                                    + String.format("%.2f", total));

                            mSubTotalFooterLayout.setVisibility(View.VISIBLE);
                            mShippingTypeFooterLayout
                                    .setVisibility(View.VISIBLE);
                            mTaxFooterLayout.setVisibility(View.VISIBLE);
                            mExpandImageView.setImageDrawable(getResources()
                                    .getDrawable(R.drawable.minus));
                        }

                        //Coupon Value
                        if (null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount() &&
                                null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getOrderDiscount() &&
                                null != checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment()) {
                            if (Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getOrderDiscount()).equals(0.0)) {
                                mCouponDiscountLayout.setVisibility(View.GONE);
                            } else {
                                mTvCouponDiscountValue.setText("-$"
                                        + String.format(
                                        "%.2f",
                                        Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment())));
                                mCouponDiscountLayout.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Invoke apply gift option.
     */
    private void invokeApplyGiftOption() {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.GIFT_OPTION_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateGiftOptionPrameters());
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        GiftOptionsHandler giftOptionsHandler = new GiftOptionsHandler();
        invokerParams.setUltaHandler(giftOptionsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<GiftOptionsActivity><invokeGiftOptionService><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Populate gift option prameters.
     *
     * @return the map
     */
    private Map<String, String> populateGiftOptionPrameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        String gftWrap, gftNote;

        gftWrap = "false";
        gftNote = "false";

        Logger.Log(">>>>>>>>>>GiftWrap<<<<<<" + gftWrap);
        Logger.Log(">>>>>>>>>>GiftNote<<<<<<" + gftNote);

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("giftNoteSelected", gftNote);
        urlParams.put("giftWrapSelected", gftWrap);
        urlParams.put("giftMessage", "");

        return urlParams;
    }

    /**
     * The Class GiftOptionsHandler.
     */
    public class GiftOptionsHandler extends UltaHandler {

        /*
         * (non-Javadoc)
         *
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<GiftOptionsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), ShippingMethodActivity.this);
                    setError(ShippingMethodActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
                // loadingLayout.setVisibility(View.GONE);
                // titleBarButton.setVisibility(View.VISIBLE);
            } else {
                Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                AddToCartBean ultaBeanAddTocart = (AddToCartBean) getResponseBean();

                List<String> errors = ultaBeanAddTocart.getErrorInfos();
                if (null != errors && !(errors.isEmpty()))
                    try {
                        notifyUser(errors.get(0), ShippingMethodActivity.this);
                        setError(ShippingMethodActivity.this, errors.get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                else { // 3.2Release

                    if (ultaBeanAddTocart.getComponent().getCart()
                            .getOrderDetails().getTotal() != 0) {
                        total = ultaBeanAddTocart.getComponent().getCart()
                                .getOrderDetails().getTotal();
                    }
                    RedeemLevelPointsBean redeemPointLvel = null;
                    ArrayList<RedeemPointBean> arryRedeemPoints = null;

                    if (ultaBeanAddTocart.getComponent().getRedeemLevels() != null) {
                        redeemPointLvel = new RedeemLevelPointsBean();
                        redeemPointLvel = (RedeemLevelPointsBean) ultaBeanAddTocart
                                .getComponent().getRedeemLevels();
                        arryRedeemPoints = new ArrayList<RedeemPointBean>();
                        arryRedeemPoints = redeemPointLvel.getRedeemLevels();
                    }
                    // loadingLayout.setVisibility(View.VISIBLE);
                    mProgressDialog.dismiss();
                    Intent goToPaymentPage = new Intent(
                            ShippingMethodActivity.this,
                            PaymentMethodActivity.class);
                    goToPaymentPage.putExtra("checkoutshipmentBean", ultaBean);

                    if (arryRedeemPoints != null
                            && arryRedeemPoints.size() != 0) {
                        goToPaymentPage.putExtra("OrderForRedeemPoint",
                                (Serializable) arryRedeemPoints);
                    }

                    startActivity(goToPaymentPage);
                }
            }
        }
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            // invokeMoveToBilling();
            navigateToBasketOnSessionTimeout(ShippingMethodActivity.this);
        }
    }

    private void creatingPageName() {
        String pageName = "";
        if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
            pageName = WebserviceConstants.CHECKOUT_SHIPPING_METHOD_GUEST_MEMBER;
        } else if (Utility.retrieveBooleanFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
                getApplicationContext())) {
            pageName = WebserviceConstants.CHECKOUT_SHIPPING_METHOD_LOYALITY_MEMBER;
        } else {
            pageName = WebserviceConstants.CHECKOUT_SHIPPING_METHOD_NON_LOYALITY_MEMBER;
        }

        trackAppState(ShippingMethodActivity.this, pageName);

    }
}
