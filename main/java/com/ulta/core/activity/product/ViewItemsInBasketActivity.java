/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.product;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adobe.mobile.Analytics;
import com.androidquery.AQuery;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.LoginActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.checkout.AddShippingAddressGuestUserActivity;
import com.ulta.core.activity.checkout.AddShippingAddressLogginUserActivity;
import com.ulta.core.activity.checkout.ChooseFreeGiftActivity;
import com.ulta.core.activity.checkout.PayPalWebviewActivity;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.CartBean;
import com.ulta.core.bean.product.CommerceItemBean;
import com.ulta.core.bean.product.FreeGiftDetailBean;
import com.ulta.core.bean.product.OrderAdjustmentBean;
import com.ulta.core.bean.product.OrderDetailBean;
import com.ulta.core.bean.product.ViewCartBean;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.OmnitureTracking;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import static com.ulta.core.conf.UltaConstants.APPLYING_COUPON_CODE_PROGRESS_TEXT;
import static com.ulta.core.conf.UltaConstants.DELETING_PROGRESS_TEXT;
import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.conf.UltaConstants.REMOVING_COUPON_CODE_PROGRESS_TEXT;
import static com.ulta.core.conf.UltaConstants.UPDATING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.displayUserErrorMessage;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class ViewItemsInBasketActivity.
 */

public class ViewItemsInBasketActivity extends UltaBaseActivity implements
        OnSessionTimeOut {
    /**
     * The Constant REQ_CODE_LOGIN.
     */
    private Button btnContinueShopping;
    private static final int REQ_CODE_LOGIN = 2;
    static final Integer[] ProductItems = new Integer[]{1, 2, 3};
    private LinearLayout llInflateCartItems;
    private static final int REQUEST_CODE_FREE_GIFT = 90;
    private EditText txtEnterCouponCode;
    private Button btnApplyCouponCode, btnRemoveCouponCode;
    private ImageButton btnExpressCheckoutAtTop, btnNormalCheckout;
    /* Variable to know whether paypal button is pressed or not */
    boolean paypalbtnPressed = false;
    private TextView tvHeaderNumberOfProducts, tvPriceTotal,
            tvPromotionalSuggestions, tvShippingChargesValue,
            tvShippingCharges;
    private TextView mCouponAmountTextView;
    private List<CommerceItemBean> listOfCommerceItemBean;
    private List<CommerceItemBean> listOfElectronicGiftCardCommerceItemBean;
    private HashMap<String, String> hmPrmIDandGiftSkuId = new HashMap<String, String>();
    private HashMap<String, String> hmPromSkuIdandPrID = new HashMap<String, String>();
    List<FreeGiftDetailBean> listOfFreeGifts;
    private float totalPrice = 0.0f;
    private String tokenId = null;
    @SuppressWarnings("unused")
    private int viewCount = 0;
    private boolean onlyGiftCard = true;
    private int countItem;
    private int quantityCount;
    private AddToCartBean addToCartBean;
    private Double couponAmount = 0.0;

    /**
     * The form layout.
     */
    LinearLayout mainLayout;

    private LinearLayout mCheckoutButtonsLayout;

    /**
     * The loading layout.
     */
    public static FrameLayout loadingDialog;

    FrameLayout basketEmptyLayout;
    private ImageView emptyBagImageView;
    private boolean isActivityOnScreen = false;
    private ProgressDialog pd;
    // UemAction basketPopulationAction;
    // UemAction checkoutFlowAction;
    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

    boolean isOnCreateCalled = false;

    private double orderTotal;

    private TextView mItemNumberItems;
    private TextView mApplyCouponCodeTextView;
    private TextView mCartSubTotalText;
    private ImageView mExpandImageView;
    private LinearLayout mCartContinueShoppingLayout, giftBoxAndNoteLayout;

    private RelativeLayout mCouponCodeLayout;
    private TextView couponCodeAndPromotion;
    private LinearLayout mCouponCodeEditTextLayout;
    private int toShowCouponCodeLayout = 1;
    /* private Switch mGiftThisOrderSwitch; */
    private CheckedTextView giftThisOrderCheckedTextView;
    private StringBuffer products = new StringBuffer();

    //Additional Discount Tiered Price Break Promo

    private LinearLayout lytAdditionalDiscount;
    private View additionalDiscountView;
    private TextView additionalDiscountValue;

    @Override
    public void onLogout() {
        if (isActivityOnScreen) {
            super.onLogout();
        }

    }

    @Override
    protected void onPause() {
        isActivityOnScreen = false;
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn",
                isUltaCustomer(ViewItemsInBasketActivity.this));
        editor.putBoolean("onPauseCalled", true);
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        isActivityOnScreen = true;
        if (!isOnCreateCalled) {
            boolean wasLoggedIn = false;
            boolean isCurrentlyLoggedIn = isUltaCustomer(ViewItemsInBasketActivity.this);
            SharedPreferences preferences = getPreferences(MODE_PRIVATE);
            wasLoggedIn = preferences.getBoolean("isLoggedIn", false);
            boolean onPausedCalled = preferences.getBoolean("onPauseCalled",
                    false);
            if (onPausedCalled) {
                if (wasLoggedIn && isCurrentlyLoggedIn) {
                    refreshBasket();
                } else if (wasLoggedIn && !isCurrentlyLoggedIn) {
                    refreshBasket();
                } else if (!wasLoggedIn && isCurrentlyLoggedIn) {
                    refreshBasket();
                } else if (!wasLoggedIn && !isCurrentlyLoggedIn) {
                    refreshBasket();
                }
            }
        }

        if (UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {
            giftThisOrderCheckedTextView.setChecked(true);
        } else {
            giftThisOrderCheckedTextView.setChecked(false);
        }
        isOnCreateCalled = false;
        // UltaDataCache.getDataCacheInstance().setMovingBackInChekout(false);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart_items);
        setActivity(ViewItemsInBasketActivity.this);
        setTitle("Bag");
        trackAppState(this, WebserviceConstants.SHOPPING_BAG);
        displayCheckoutButton();
        disableBagIcon();

        trackAppAction(ViewItemsInBasketActivity.this,
                WebserviceConstants.EVENT_SC_OPEN);
        /*
         * trackAppAction(ViewItemsInBasketActivity.this,
		 * WebserviceConstants.EVENT_SC_VIEW);
		 */
        // basketPopulationAction = UemAction
        // .enterAction(WebserviceConstants.ACTION_POPULATING_BASKET);
        loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
        mainLayout = (LinearLayout) findViewById(R.id.basketMainLayout);
        basketEmptyLayout = (FrameLayout) findViewById(R.id.emptyBasketLayout);
        emptyBagImageView = (ImageView) findViewById(R.id.emptyBagImageView);
        tvShippingChargesValue = (TextView) findViewById(R.id.ShhippingChargesValue);
        llInflateCartItems = (LinearLayout) findViewById(R.id.llInflateCartItems);
        txtEnterCouponCode = (EditText) findViewById(R.id.txtEnterCouponCode);
        mCheckoutButtonsLayout = (LinearLayout) findViewById(R.id.layoutCheckoutButtons);
        giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
        btnApplyCouponCode = (Button) findViewById(R.id.btnApplyCouponCode);
        btnRemoveCouponCode = (Button) findViewById(R.id.btnRemoveCouponCode);
        btnContinueShopping = (Button) findViewById(R.id.btnContinueShopping);
        btnContinueShopping.setPadding(20, 20, 20, 20);
        btnExpressCheckoutAtTop = (ImageButton) findViewById(R.id.btnExpressCheckout_top);
        btnNormalCheckout = (ImageButton) findViewById(R.id.btnNormalCheckout);

        //Additional Discount Tiered Price Break Promo

        lytAdditionalDiscount = (LinearLayout) findViewById(R.id.lytAdditionalDiscount);
        additionalDiscountView = (View) findViewById(R.id.additionalDiscountView);
        additionalDiscountValue = (TextView) findViewById(R.id.additionalDiscountValue);


        mItemNumberItems = (TextView) findViewById(R.id.itemNumberItems);
        mApplyCouponCodeTextView = (TextView) findViewById(R.id.applyCouponCodeTextView);
        mCouponAmountTextView = (TextView) findViewById(R.id.couponAmountTextView);
        couponCodeAndPromotion = (TextView) findViewById(R.id.couponCodeAndPromotion);
        mCouponCodeLayout = (RelativeLayout) findViewById(R.id.couponCodeLayout);
        mCouponCodeEditTextLayout = (LinearLayout) findViewById(R.id.couponCodeEditTextLayout);
        giftThisOrderCheckedTextView = (CheckedTextView) findViewById(R.id.giftThisOrderCheckbox);
        Drawable drawable = getResources().getDrawable(
                (R.drawable.custom_btn_radio));
        drawable.setBounds(0, 0, 72, 72);

        giftThisOrderCheckedTextView.setCompoundDrawables(null, null, drawable,
                null);
        giftThisOrderCheckedTextView.setTypeface(setHelveticaRegulartTypeFace());
        mCartSubTotalText = (TextView) findViewById(R.id.cartSubTotalText);
        mExpandImageView = (ImageView) findViewById(R.id.expandImageView);

        mCartContinueShoppingLayout = (LinearLayout) findViewById(R.id.cartContinueShoppingLayout);

        mCartSubTotalText.setTypeface(setHelveticaRegulartTypeFace());
        mApplyCouponCodeTextView.setTypeface(setHelveticaRegulartTypeFace());
        couponCodeAndPromotion.setTypeface(setHelveticaRegulartTypeFace());
        mCouponAmountTextView.setTypeface(setHelveticaRegulartTypeFace());
        mItemNumberItems.setTypeface(setHelveticaRegulartTypeFace());
        tvShippingChargesValue.setTypeface(setHelveticaRegulartTypeFace());
        tvPriceTotal = (TextView) findViewById(R.id.tvPriceTotal);
        tvPromotionalSuggestions = (TextView) findViewById(R.id.tvPromotionalSuggestions);
        tvShippingCharges = (TextView) findViewById(R.id.shippingCharges);

        tvShippingCharges.setTypeface(setHelveticaRegulartTypeFace());

        if (null != UltaDataCache.getDataCacheInstance()
                .getHmWithFreeGiftsReverse()
                && null != UltaDataCache.getDataCacheInstance()
                .getHmWithPromoAndFreeGifts()) {

            hmPrmIDandGiftSkuId = UltaDataCache.getDataCacheInstance()
                    .getHmWithPromoAndFreeGifts();
            hmPromSkuIdandPrID = UltaDataCache.getDataCacheInstance()
                    .getHmWithFreeGiftsReverse();
        }

        tvPriceTotal.setTypeface(setHelveticaRegulartTypeFace());
        tvPromotionalSuggestions.setTypeface(setHelveticaRegulartTypeFace());

        pd = new ProgressDialog(ViewItemsInBasketActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setCancelable(false);
        loadingDialog.setVisibility(View.VISIBLE);
        isOnCreateCalled = true;
        mainLayout.setVisibility(View.INVISIBLE);
        mCheckoutButtonsLayout.setVisibility(View.INVISIBLE);
        basketEmptyLayout.setVisibility(View.GONE);
        fnInvokeRetrieveBasketItems();

        btnExpressCheckoutAtTop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                UltaDataCache.getDataCacheInstance().setOrderTotal(
                        String.format("%.2f", Double.valueOf(orderTotal)));
                // checkoutFlowAction = UemAction
                // .enterAction(WebserviceConstants.ACTION_EXPRESS_CHECKOUT_INVOCATION);
                // checkoutFlowAction
                // .reportEvent("Express checkout button is clicked");
                pd.setMessage(LOADING_PROGRESS_TEXT);
                pd.show();
                UltaDataCache.getDataCacheInstance().setExpressCheckout(true);
                if (couponAmount == orderTotal) {
                    couponAmount = 0.00;
                    UltaDataCache.getDataCacheInstance().setCouponAmount(
                            couponAmount);
                } else {
                    UltaDataCache.getDataCacheInstance().setCouponAmount(
                            couponAmount);
                }
                invokeCheckoutService();
            }
        });
        btnNormalCheckout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                UltaDataCache.getDataCacheInstance().setOrderTotal(
                        String.format("%.2f", Double.valueOf(orderTotal)));

                if (null != listOfCommerceItemBean
                        && !listOfCommerceItemBean.isEmpty()) {
                    // checkoutFlowAction = UemAction
                    // .enterAction(WebserviceConstants.ACTION_NORAML_CHECKOUT_INVOCATION);
                    // checkoutFlowAction
                    // .reportEvent("Normal checkout button is clicked");
                    pd.setMessage(LOADING_PROGRESS_TEXT);
                    pd.show();
                    UltaDataCache.getDataCacheInstance().setExpressCheckout(
                            false);
                    if (couponAmount == orderTotal) {
                        couponAmount = 0.00;
                        UltaDataCache.getDataCacheInstance().setCouponAmount(
                                couponAmount);
                    } else {
                        UltaDataCache.getDataCacheInstance().setCouponAmount(
                                couponAmount);
                    }
                    checkForLoginBeforeCheckout();
                }
            }
        });
        btnContinueShopping.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intentForShop = new Intent(
                        ViewItemsInBasketActivity.this, ShopListActivity.class);
                startActivity(intentForShop);
            }
        });

        btnApplyCouponCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtEnterCouponCode.getText().toString().equals("")) {
                    /*
                     * AlertDialog alertDialog = new AlertDialog.Builder(
					 * ViewItemsInBasketActivity.this).create();
					 * alertDialog.setTitle("Alert");
					 * alertDialog.setMessage("Please enter the Coupon Code");
					 * alertDialog.setButton("OK", new
					 * DialogInterface.OnClickListener() { public void
					 * onClick(DialogInterface dialog, int which) { } });
					 * 
					 * alertDialog.show();
					 */

                    final Dialog alertDialog = showAlertDialog(
                            ViewItemsInBasketActivity.this, "Alert",
                            "Please enter the Coupon Code", "Ok", "");
                    alertDialog.show();

                    mDisagreeButton.setVisibility(View.GONE);
                    mAgreeButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    pd.setMessage(APPLYING_COUPON_CODE_PROGRESS_TEXT);
                    pd.show();
                    fnInvokeApplyCouponCode(txtEnterCouponCode.getText()
                            .toString());
                }
            }
        });

        btnRemoveCouponCode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                pd.setMessage(REMOVING_COUPON_CODE_PROGRESS_TEXT);
                pd.show();
                fnInvokeRemoveCouponCode();
            }

        });

        mCouponCodeLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (toShowCouponCodeLayout % 2 != 0) {
                    mCouponCodeEditTextLayout.setVisibility(View.VISIBLE);
                    mExpandImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.minus));
                } else {
                    mCouponCodeEditTextLayout.setVisibility(View.GONE);
                    mExpandImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.plus));
                }
                toShowCouponCodeLayout++;
            }
        });

        giftThisOrderCheckedTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (giftThisOrderCheckedTextView.isChecked()) {
                    UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
                    giftThisOrderCheckedTextView.setChecked(false);
                } else {

                    UltaDataCache.getDataCacheInstance().setGiftTheOrder(true);
                    giftThisOrderCheckedTextView.setChecked(true);
                }

            }
        });

        mCartContinueShoppingLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentForShop = new Intent(
                        ViewItemsInBasketActivity.this, ShopListActivity.class);
                startActivity(intentForShop);

            }
        });

        txtEnterCouponCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String s = txtEnterCouponCode.getText().toString();
                if (!s.equals(s.toUpperCase(Locale.ENGLISH))) {
                    s = s.toUpperCase(Locale.ENGLISH);
                    txtEnterCouponCode.setText(s);
                    txtEnterCouponCode.setSelection(txtEnterCouponCode.length());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_LOGIN:
                if (RESULT_OK == resultCode
                        && UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                    Logger.Log(">>>>>>>>>>> refreshing basket");
                    refreshBasket();
                    invokeCheckoutService();
                } else if (RESULT_CANCELED == requestCode) {
                    Toast.makeText(ViewItemsInBasketActivity.this,
                            "Please Sign in to continue", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case REQUEST_CODE_FREE_GIFT:
                if (RESULT_OK == resultCode) {
                    refreshBasket();
                    if (null != data.getExtras()) {
                        if (null != data.getExtras().getString("ParentProductId")
                                && null != data.getExtras().getString(
                                "FreeGiftSkuId")) {
                            hmPrmIDandGiftSkuId
                                    .put(data.getExtras().getString("PromotionId"),
                                            data.getExtras().getString(
                                                    "FreeGiftSkuId"));
                            hmPromSkuIdandPrID.put(
                                    data.getExtras().getString("PromotionId"),
                                    data.getExtras().getString("ParentProductId"));
                            UltaDataCache
                                    .getDataCacheInstance()
                                    .setHmWithPromoAndFreeGifts(hmPrmIDandGiftSkuId);
                            UltaDataCache.getDataCacheInstance()
                                    .setHmWithFreeGiftsReverse(hmPromSkuIdandPrID);
                        }

                    }
                }

                break;
            default:
                break;
        }
    }

    private void refreshBasket() {
        pd.setMessage(LOADING_PROGRESS_TEXT);
        try {
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fnInvokeRetrieveBasketItems();
    }

    private int getCountOfItemsInBAsket() {
        int quantityCount = 0;
        if (null != listOfCommerceItemBean) {
            for (int loop = 0; loop < listOfCommerceItemBean.size(); loop++) {
                CommerceItemBean commerceItemBean = listOfCommerceItemBean
                        .get(loop);
                quantityCount = quantityCount + commerceItemBean.getQuantity();
            }
        }
        return quantityCount;

    }

    private void updateTieredBreakUpPrice(OrderDetailBean orderDetailBean) {
        if (null != orderDetailBean) {

            //Tiered Price Break Promo Additional Discount
            if (null != orderDetailBean.getTieredDiscountAmount() && !orderDetailBean.getTieredDiscountAmount().isEmpty()) {
                additionalDiscountValue.setText("-$"
                        + String.format("%.2f",
                        Double.valueOf(orderDetailBean.getTieredDiscountAmount())));
                lytAdditionalDiscount.setVisibility(View.VISIBLE);
                additionalDiscountView.setVisibility(View.VISIBLE);
            } else {
                lytAdditionalDiscount.setVisibility(View.GONE);
                additionalDiscountView.setVisibility(View.GONE);
            }
        }

    }

    //    @SuppressWarnings("static-access")
    public void inflateCartItemsToLayout() {

        llInflateCartItems.removeAllViews();
        countItem = 0;
        quantityCount = 0;
        Logger.Log("<<count1" + countItem);
        Logger.Log("<<quant1" + quantityCount);
        basketEmptyLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        mCheckoutButtonsLayout.setVisibility(View.VISIBLE);
        if (listOfCommerceItemBean.isEmpty()) {

            if (haveInternet()) {
                checkDensityAndSetImage(emptyBagImageView,
                        WebserviceConstants.EMPTY_BAG, R.drawable.empty_bag,
                        "Basket", null, true);
            } else {
                emptyBagImageView.setImageResource(R.drawable.empty_bag);
            }
            UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
            giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
            giftBoxAndNoteLayout.setVisibility(View.GONE);
            basketEmptyLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.INVISIBLE);
            mCheckoutButtonsLayout.setVisibility(View.INVISIBLE);
        }

        for (int loop = 0; loop < listOfCommerceItemBean.size(); loop++) {
            final CommerceItemBean commerceItemBean;

            commerceItemBean = listOfCommerceItemBean.get(loop);
            createProductsForOmniture(commerceItemBean);
            final ArrayList<String> listOfQuantities = new ArrayList<String>();
            int maxQuantityInt = 10;
            if (null != commerceItemBean.getMaxQty()) {
                maxQuantityInt = Integer.parseInt(commerceItemBean.getMaxQty());
            }
            if (maxQuantityInt == 0) {
                maxQuantityInt = 10;
            }
            if (maxQuantityInt > 10 || maxQuantityInt < 0) {
                maxQuantityInt = 10;
            }
            for (int loop1 = 1; loop1 <= maxQuantityInt; loop1++) {
                listOfQuantities.add("" + loop1);
            }

            LayoutInflater inflater = (LayoutInflater) this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater
                    .inflate(R.layout.inflate_view_cart_items, null);

            ImageView imgItemDisplay = (ImageView) view
                    .findViewById(R.id.imgItemDisplay);
            TextView tvBrand = (TextView) view.findViewById(R.id.tvBrand);
            TextView tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
            TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
            TextView basketHazmatTextView = (TextView) view
                    .findViewById(R.id.basketHazmatTextView);
            final TextView tvPriceXQuantity = (TextView) view
                    .findViewById(R.id.tvPriceXQuantity);
            final TextView tvStrikedPriceXQuantity = (TextView) view
                    .findViewById(R.id.tvStrikedPriceXQuantity);
            final TextView tvPriceAccToQuantity = (TextView) view
                    .findViewById(R.id.tvPriceAccToQuantity);
            final TextView spQuantity = (TextView) view
                    .findViewById(R.id.spQuantity);

            if (null != commerceItemBean) {
                if (null != commerceItemBean.getIsGiftWrapItem()) {
                    if (commerceItemBean.getIsGiftWrapItem().equalsIgnoreCase(
                            "true")) {
                        giftThisOrderCheckedTextView.setChecked(true);
                        UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                true);
                        view.setVisibility(View.GONE);
                        LinearLayout giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
                        TextView giftBoxAndNoteTextViewValue = (TextView) findViewById(R.id.giftBoxAndNoteValue);
                        TextView giftBoxAndNoteTextView = (TextView) findViewById(R.id.giftBoxNoteTextView);
                        View seperatingItemsAndGiftBoxAndNote = (View) findViewById(R.id.seperatingItemsAndGiftBoxAndNote);
                        giftBoxAndNoteLayout.setVisibility(View.VISIBLE);
                        giftBoxAndNoteTextViewValue.setText("$"
                                + commerceItemBean.getAmount());
                        giftBoxAndNoteTextView
                                .setTypeface(setHelveticaRegulartTypeFace());
                        seperatingItemsAndGiftBoxAndNote
                                .setVisibility(View.VISIBLE);
                        // break;

                    } else {
                        UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                false);
                        giftThisOrderCheckedTextView.setChecked(false);
                    }
                }
            }

            tvPriceAccToQuantity.setTypeface(setHelveticaRegulartTypeFace());
            tvPriceXQuantity.setTypeface(setHelveticaRegulartTypeFace());
            tvStrikedPriceXQuantity.setTypeface(setHelveticaRegulartTypeFace());
            spQuantity.setTypeface(setHelveticaRegulartTypeFace());
            tvItemName.setTypeface(setHelveticaRegulartTypeFace());
            tvBrand.setTypeface(setHelveticaRegulartTypeFace(), Typeface.BOLD);
            // change here
            final ImageView btnRemove = (ImageView) view
                    .findViewById(R.id.btnRemove);
            final LinearLayout removeButtonlayout = (LinearLayout) view
                    .findViewById(R.id.removeButtonlayout);
            RelativeLayout freeGiftRelativeLayout = (RelativeLayout) view
                    .findViewById(R.id.btnRedeem);
            TextView tvPromotionalAdBug = (TextView) view
                    .findViewById(R.id.tvPromotionalAdBug);
            TextView tvFeature = (TextView) view.findViewById(R.id.tvFeature);
            TextView tvItemOutOfStock = (TextView) view
                    .findViewById(R.id.tvItemOutOfStock);

            TextView tvItemNumber = (TextView) view
                    .findViewById(R.id.tvItemNumber);
            TextView tvFeatureText = (TextView) view
                    .findViewById(R.id.tvFeatureText);
            tvFeature.setTypeface(setHelveticaRegulartTypeFace());
            tvFeatureText.setTypeface(setHelveticaRegulartTypeFace());

            if (null != commerceItemBean) {
                if (commerceItemBean.isOutOfStock())

                {
                    tvItemOutOfStock.setVisibility(TextView.VISIBLE);
                    spQuantity.setVisibility(TextView.GONE);
                }
            }

            if (null != commerceItemBean) {
                if (null != commerceItemBean.getId()) {
                    tvItemNumber.setVisibility(View.GONE);
                    tvItemNumber.setText("Item# "
                            + commerceItemBean.getCatalogRefId());
                }
            }

            if (null != commerceItemBean) {
                if (null != commerceItemBean.getHazmatCode()) {
                    if (commerceItemBean.getHazmatCode().equalsIgnoreCase("H")) {
                        basketHazmatTextView.setVisibility(View.VISIBLE);
                    }
                }
            }

            if (null != commerceItemBean.getPromotions()
                    && commerceItemBean.getPromotions().size() != 0
                    && !commerceItemBean.isGWP()) {
                freeGiftRelativeLayout.setVisibility(View.VISIBLE);
                if (null != commerceItemBean.getPromotions().get(0)
                        .getId()) {
                    tvPromotionalAdBug.setVisibility(View.VISIBLE);
                    tvPromotionalAdBug.setText(commerceItemBean.getPromotions()
                            .get(0).getOfferDesc());
                } else {
                    freeGiftRelativeLayout.setVisibility(View.INVISIBLE);

                }
            } else {
                tvPromotionalAdBug.setVisibility(View.GONE);
                freeGiftRelativeLayout.setVisibility(View.INVISIBLE);
            }

            if (null != commerceItemBean.getOfferDesc()
                    && !commerceItemBean.getOfferDesc().equals(
                    "Special Free Gift with Purchase")) {
                tvPromotionalAdBug.setVisibility(View.VISIBLE);
                tvPromotionalAdBug.setText(commerceItemBean.getOfferDesc());
            }

            view.setId(loop);
            btnRemove.setTag(loop);

            spQuantity.setText(Integer.valueOf(commerceItemBean.getQuantity())
                    .toString());
            spQuantity.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    showQuantityDialog(listOfQuantities, commerceItemBean);

                }
            });
            if (commerceItemBean.getQuantity() >= 1
                    && commerceItemBean.getIsFreeSample().equalsIgnoreCase(
                    "false")) {
                if (null != commerceItemBean.getSalePrice()) {
                    if ((commerceItemBean.getSalePrice().equals("0"))
                            && (String.format("%.2f", Double
                            .valueOf(commerceItemBean.getAmount()))
                            .equals("0")))
                        tvPriceXQuantity.setText(" X "
                                + "$"
                                + String.format("%.2f", Double
                                .valueOf(commerceItemBean
                                        .getSalePrice())));
                    else if (!(commerceItemBean.getSalePrice().equals("0"))) {
                        tvPriceXQuantity.setText(" X "
                                + "$"
                                + String.format("%.2f", Double
                                .valueOf(commerceItemBean
                                        .getSalePrice())));
                        tvStrikedPriceXQuantity.setText("$"
                                + String.format("%.2f", Double
                                .valueOf(commerceItemBean
                                        .getListPrice())));
                        tvStrikedPriceXQuantity
                                .setPaintFlags(tvStrikedPriceXQuantity
                                        .getPaintFlags()
                                        | Paint.STRIKE_THRU_TEXT_FLAG);
                        tvStrikedPriceXQuantity.setVisibility(View.VISIBLE);
                    } else
                        tvPriceXQuantity.setText(" X "
                                + "$"
                                + String.format("%.2f", Double
                                .valueOf(commerceItemBean
                                        .getListPrice())));
                }
            }
            tvBrand.setText(commerceItemBean.getBrandName());
            tvItemName.setText(commerceItemBean.getDisplayName());

            if (null != commerceItemBean.getEmailAddress()) {
                tvEmailId.setText(commerceItemBean.getEmailAddress());
            }
            if (commerceItemBean.isGWP()) {
                if (commerceItemBean.getAmount() != 0.0) {
                    tvPromotionalAdBug.setVisibility(View.VISIBLE);
                    tvPromotionalAdBug.setText("1 For Free,remaining For $"
                            + commerceItemBean.getAmount());
                } else {
                    tvPriceAccToQuantity.setText("FREE");
                }
                tvPriceXQuantity.setVisibility(View.GONE);
            } else if (null != commerceItemBean.getIsElectronicGiftCard()
                    && commerceItemBean.getIsElectronicGiftCard()
                    .equalsIgnoreCase("true")) {
                spQuantity.setVisibility(view.GONE);
            } else if (null != commerceItemBean.getIsFreeSample()
                    && commerceItemBean.getIsFreeSample().equalsIgnoreCase(
                    "true")) {
                tvPriceAccToQuantity.setText("FREE");
                //  tvPriceAccToQuantity.setTextColor(color.roseBudCherry);
                spQuantity.setVisibility(View.GONE);
            } else if (null != commerceItemBean.getIsGiftWrapItem()
                    && commerceItemBean.getIsGiftWrapItem().equalsIgnoreCase(
                    "true")) {
                spQuantity.setVisibility(View.GONE);
            }

            if (null != commerceItemBean.getFeatureType()) {
                tvFeature.setVisibility(View.VISIBLE);
                tvFeature.setText(commerceItemBean.getFeatureType());
                tvFeatureText.setVisibility(View.GONE);

            } else {
                tvFeature.setVisibility(View.GONE);
                tvFeatureText.setVisibility(View.GONE);
            }

            if (!String.format("%.2f",
                    Double.valueOf(commerceItemBean.getAmount()))
                    .equals("0.00")) {
                tvPriceAccToQuantity.setText("$"
                        + String.format("%.2f",
                        Double.valueOf(commerceItemBean.getAmount())));
            } else {
                tvPriceAccToQuantity.setText("FREE");
                //  tvPriceAccToQuantity.setTextColor(color.roseBudCherry);
            }
            totalPrice = totalPrice + (float) (commerceItemBean.getListPrice());
            quantityCount = quantityCount + commerceItemBean.getQuantity();
            countItem++;

            Logger.Log("<<count++" + countItem);
            Logger.Log("<<quant++" + quantityCount);
            tvHeaderNumberOfProducts = (TextView) findViewById(R.id.tvHeaderNumberOfProducts);
            tvHeaderNumberOfProducts
                    .setTypeface(setHelveticaRegulartTypeFace());
            tvHeaderNumberOfProducts.setText("" + quantityCount + "");
            setItemCountInBasket(quantityCount);

            new AQuery(imgItemDisplay).image(
                    commerceItemBean.getSmallImageUrl(), true, false, 200,
                    R.drawable.dummy_product, null, AQuery.FADE_IN);
            removeButtonlayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pd = new ProgressDialog(ViewItemsInBasketActivity.this);
                    pd.setMessage(DELETING_PROGRESS_TEXT);
                    setProgressDialogLoadingColor(pd);
                    pd.show();
                    fnInvokeRemoveItem(commerceItemBean);
                }
            });
            btnRemove.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    pd = new ProgressDialog(ViewItemsInBasketActivity.this);
                    pd.setMessage(DELETING_PROGRESS_TEXT);
                    setProgressDialogLoadingColor(pd);
                    pd.show();
                    fnInvokeRemoveItem(commerceItemBean);
                }
            });

            freeGiftRelativeLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != commerceItemBean.getPromotions()
                            && !commerceItemBean.getPromotions().isEmpty()) {

                        Intent intentForChooseFreeGiftActivity = new Intent(
                                ViewItemsInBasketActivity.this,
                                ChooseFreeGiftActivity.class);
                        intentForChooseFreeGiftActivity.putExtra(
                                "promotionsSize", commerceItemBean
                                        .getPromotions().size());
                        intentForChooseFreeGiftActivity.putExtra("giftList",
                                commerceItemBean);
                        intentForChooseFreeGiftActivity.putExtra("pdtName",
                                commerceItemBean.getDisplayName());
                        startActivityForResult(intentForChooseFreeGiftActivity,
                                REQUEST_CODE_FREE_GIFT);

                    }
                }
            });
            viewCount++;
            llInflateCartItems.addView(view);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewItemsInBasketActivity.this,
                            UltaProductDetailsActivity.class);
                    intent.putExtra("idFromBasket",
                            commerceItemBean.getProductId());
                    if (null != commerceItemBean.getCatalogRefId()) {
                        intent.putExtra("skuid",
                                commerceItemBean.getCatalogRefId());
                        startActivity(intent);
                    }

                }
            });
        }

        // trackEvarsUsingActionName(this, WebserviceConstants.EVENT_SC_VIEW,
        // WebserviceConstants.PRODUCTS_KEY, products.toString());

        totalPrice = 0.0f;

    }

    public void createProductsForOmniture(CommerceItemBean commerceItemBean) {

        if (null != commerceItemBean.getProductId()) {
            products.append(commerceItemBean.getProductId());
        }

    }

    private void invokeCheckoutService() {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        if (UltaDataCache.getDataCacheInstance().isExpressCheckout()) {
            invokerParams
                    .setServiceToInvoke(WebserviceConstants.PAYPAL_CHECKOUT_SERVICE);
        } else {
            invokerParams
                    .setServiceToInvoke(WebserviceConstants.CHECKOUT_SERVICE);
        }
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateShippmentParameters());
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        CheckoutHandler userCreationHandler = new CheckoutHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);

        }
    }

    private boolean checkForLoginBeforeCheckout() {
        if (isUltaCustomer(ViewItemsInBasketActivity.this)
                || UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
            invokeCheckoutService();
        } else {
            Logger.Log(">>>>>>>>take user to Sign in page");
            Intent intentForLogin = new Intent(ViewItemsInBasketActivity.this,
                    LoginActivity.class);
            intentForLogin.putExtra("origin", "basket");
            intentForLogin.putExtra("fromCehckout", 2);
            startActivity(intentForLogin);
        }
        return false;
    }

    /**
     * Check for login.
     *
     * @return true, if successful
     */
    @SuppressWarnings("unused")
    private boolean checkForLogin() {
        if (isUltaCustomer(ViewItemsInBasketActivity.this)) {
            Intent goToPaymentMethod = new Intent(
                    ViewItemsInBasketActivity.this, FreeSamplesActivity.class);
            startActivity(goToPaymentMethod);
        } else {
            Logger.Log(">>>>>>>>take user to Sign in page");
            Intent intentForLogin = new Intent(ViewItemsInBasketActivity.this,
                    LoginActivity.class);
            intentForLogin.putExtra("origin", "basket");
            intentForLogin.putExtra("fromCehckout", 1);
            startActivity(intentForLogin);
        }
        return false;
    }

    @Deprecated
    private void errorHandling() {
        for (int i = 0; i < addToCartBean.getErrorInfos().size(); i++) {
            if (null != addToCartBean.getComponent()
                    && null != addToCartBean.getComponent().getCart()) {
                if (null != addToCartBean.getComponent().getCart()
                        .getCommerceItems()) {
                    for (int j = 0; j < addToCartBean.getComponent().getCart()
                            .getCommerceItems().size(); j++) {
                        String errorToSplit = addToCartBean.getErrorInfos()
                                .get(i).replaceAll(" ", "-");
                        if (addToCartBean.getErrorInfos().get(i)
                                .startsWith("The item")
                                && errorToSplit != null
                                && errorToSplit.trim().length() > 1) {
                            String[] initialSortHolder = null;
                            initialSortHolder = errorToSplit.split("-");
                            Logger.Log(">>>>>>>>>>> initialSortHolder "
                                    + initialSortHolder[2]);
                            if (initialSortHolder[2].equals(addToCartBean
                                    .getComponent().getCart()
                                    .getCommerceItems().get(j)
                                    .getCatalogRefId())) {
                                addToCartBean.getComponent().getCart()
                                        .getCommerceItems().get(j)
                                        .setOutOfStock(true);
                            }
                        }

                    }
                } else {
                    notifyUser("There are no items to purchase in the order",
                            ViewItemsInBasketActivity.this);
                }
            }
        }

    }

    private Map<String, String> populateShippmentParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("isReqFromNewApp", "true");
        return urlParams;
    }

    public class CheckoutHandler extends UltaHandler {
        public void handleMessage(Message msg) {

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    // checkoutFlowAction.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // checkoutFlowAction.leaveAction();
                    askRelogin(ViewItemsInBasketActivity.this);
                } else {
                    try {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        // checkoutFlowAction
                        // .reportError(
                        // getErrorMessage(),
                        // WebserviceConstants.DYN_ERRCODE_VIEW_ITENS_IN_BASKET_ACTIVITY);
                        // checkoutFlowAction.leaveAction();
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                ViewItemsInBasketActivity.this);
                        setError(ViewItemsInBasketActivity.this,
                                getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                try {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                trackAppAction(ViewItemsInBasketActivity.this,
                        WebserviceConstants.EVENT_SC_CHECKOUT);
                addToCartBean = (AddToCartBean) getResponseBean();
                if (null != addToCartBean
                        && null != addToCartBean.getErrorInfos()) {
                    errorHandling();
                    try {
                        // checkoutFlowAction
                        // .reportError(
                        // WebserviceConstants.FORM_EXCEPTION_OCCURED,
                        // WebserviceConstants.DYN_ERRCODE_VIEW_ITENS_IN_BASKET_ACTIVITY);
                        // checkoutFlowAction.leaveAction();
                        displayUserErrorMessage(null, addToCartBean
                                        .getErrorInfos().get(0),
                                ViewItemsInBasketActivity.this, null);
                        setError(ViewItemsInBasketActivity.this, addToCartBean
                                .getErrorInfos().get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                } else {
                    ArrayList<String> freeSamplesList = new ArrayList<String>();
                    for (int i = 0; i < listOfCommerceItemBean.size(); i++) {
                        if (listOfCommerceItemBean.get(i).getIsFreeSample()
                                .equals("true")) {
                            freeSamplesList.add(listOfCommerceItemBean.get(i)
                                    .getCatalogRefId());
                        }
                    }
                    for (int j = 0; j < listOfCommerceItemBean.size(); j++) {
                        if (listOfCommerceItemBean.get(j)
                                .getIsElectronicGiftCard()
                                .equalsIgnoreCase("true")
                                || listOfCommerceItemBean.get(j)
                                .getIsGiftCard()
                                .equalsIgnoreCase("true")) {
                            onlyGiftCard = true;
                        } else {
                            onlyGiftCard = false;
                            break;
                        }

                    }
                    setItemCountInBasket(getCountOfItemsInBAsket());
                    Logger.Log(">>>>>>>>>>>>onlyGiftCard " + onlyGiftCard);

                    if (null != addToCartBean
                            && null != addToCartBean.getComponent()
                            && null != addToCartBean.getComponent()
                            .getTokenId()) {
                        tokenId = addToCartBean.getComponent().getTokenId();
                    }
                    if (onlyGiftCard) {
                        if (UltaDataCache.getDataCacheInstance()
                                .isExpressCheckout()) {
                            // UltaDataCache.getDataCacheInstance()
                            // .setMovingBackInChekout(false);
                            UltaDataCache.getDataCacheInstance()
                                    .setOnlyEgiftCard(true);

                            Intent gotoPayaplWebView = new Intent(
                                    ViewItemsInBasketActivity.this,
                                    PayPalWebviewActivity.class);
                            Log.e("PayPal", "TokenId from web service: "
                                    + tokenId);
                            gotoPayaplWebView.putExtra("token", tokenId);
                            startActivity(gotoPayaplWebView);
                        } else if (UltaDataCache.getDataCacheInstance()
                                .isAnonymousCheckout()) {
                            // UltaDataCache.getDataCacheInstance()
                            // .setMovingBackInChekout(false);
                            UltaDataCache.getDataCacheInstance()
                                    .setOnlyEgiftCard(true);
                            UltaDataCache.getDataCacheInstance()
                                    .setExpressCheckout(false);
                            /*
                             * Intent enterShippingAddress = new Intent(
							 * ViewItemsInBasketActivity.this,
							 * AddNewShippingAddressActivity.class);
							 */
                            Intent enterShippingAddress = new Intent(
                                    ViewItemsInBasketActivity.this,
                                    AddShippingAddressGuestUserActivity.class);
                            startActivity(enterShippingAddress);
                        } else {
                            Logger.Log(">>>>>>>>>>>>onlyGiftCard "
                                    + onlyGiftCard);
                            // UltaDataCache.getDataCacheInstance()
                            // .setMovingBackInChekout(false);
                            UltaDataCache.getDataCacheInstance()
                                    .setOnlyEgiftCard(true);
                            UltaDataCache.getDataCacheInstance()
                                    .setExpressCheckout(false);
                            // Intent goToShippingMethod = new Intent(
                            // ViewItemsInBasketActivity.this,
                            // ShippingAdressActivity.class);
                            // startActivity(goToShippingMethod);

                            Intent goToShippingMethod = new Intent(
                                    ViewItemsInBasketActivity.this,
                                    AddShippingAddressLogginUserActivity.class);
                            startActivity(goToShippingMethod);
                        }
                    } else {
                        Logger.Log(">>>>>>>>>>>>not onlyGiftCard "
                                + onlyGiftCard);
                        UltaDataCache.getDataCacheInstance().setOnlyEgiftCard(
                                false);
                        Intent goToShippingMethod = new Intent(
                                ViewItemsInBasketActivity.this,
                                FreeSamplesActivity.class);
                        goToShippingMethod.putExtra("FreeSamplesList",
                                freeSamplesList);
                        goToShippingMethod.putExtra("token", tokenId);
                        startActivity(goToShippingMethod);
                    }
                }
                /*if (null != addToCartBean) {
                    populatePage(addToCartBean);
				}*/
                // checkoutFlowAction.leaveAction();
            }
        }
    }

    private void updatePromoMessage(OrderDetailBean orderDetailBean) {
        if (null != orderDetailBean.getShippingMessage()) {
            tvPromotionalSuggestions.setText(orderDetailBean.getShippingMessage());
        }
    }

    private void fnInvokeRetrieveBasketItems() {
        InvokerParams<ViewCartBean> invokerParams = new InvokerParams<ViewCartBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.GETMOBILECART_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateRetrievBasketItemsHandlerParameters());
        invokerParams.setUltaBeanClazz(ViewCartBean.class);
        RetrieveBasketItemsHandler retrieveBasketItemsHandler = new RetrieveBasketItemsHandler();
        invokerParams.setUltaHandler(retrieveBasketItemsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeRetrieveBasketItems()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> fnPopulateRetrievBasketItemsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    public class RetrieveBasketItemsHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            try {
                loadingDialog.setVisibility(View.GONE);

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (null != getErrorMessage()) {

                try {
                    // basketPopulationAction
                    // .reportError(
                    // getErrorMessage(),
                    // WebserviceConstants.DYN_ERRCODE_VIEW_ITENS_IN_BASKET_ACTIVITY);
                    // basketPopulationAction.leaveAction();
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            ViewItemsInBasketActivity.this);
                    setError(ViewItemsInBasketActivity.this, getErrorMessage());

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Log.d("start", "stary");
                ViewCartBean viewCartBean = (ViewCartBean) getResponseBean();

                if (null != viewCartBean) {
                    if (null != viewCartBean.getErrorInfos()) {
                        setError(ViewItemsInBasketActivity.this, viewCartBean
                                .getErrorInfos().get(0));
                    }
                    displayCouponApplied(viewCartBean);
                    Double totalAdjustment = 0.0;
                    List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                            .getCart().getOrderAdjustments();
//                    if (null != orderAdjustments && !orderAdjustments.isEmpty()) {
//
//                        for (int i = 0; i < orderAdjustments.size(); i++) {
//                            totalAdjustment = totalAdjustment
//                                    + orderAdjustments.get(i)
//                                    .getTotalAdjustment();
//                        }
//                        tvPriceTotal.setText("$"
//                                + String.format("%.2f",
//                                Double.valueOf(totalAdjustment)));
//                        orderTotal = totalAdjustment;
//                        couponAmount = viewCartBean.getCart().getOrderDetails()
//                                .getRawSubtotal()
//                                - orderTotal;
//                    } else {
                    orderTotal = viewCartBean.getCart().getOrderDetails()
                            .getRawSubtotal();
                    tvPriceTotal.setText("$"
                            + String.format("%.2f",
                            Double.valueOf(orderTotal)));
                    couponAmount = orderTotal;
//                    }

                    CartBean cartBean = viewCartBean.getCart();
                    Log.d("getCart()", ""+cartBean);
                    if (null != cartBean) {
                        listOfCommerceItemBean = cartBean.getCommerceItems();
                        Log.d("listOfCommerceItemBean", ""+listOfCommerceItemBean);
                        if (null != listOfCommerceItemBean && listOfCommerceItemBean.size() > 0) {

                            /********************************************************************************************/
                            listOfElectronicGiftCardCommerceItemBean = cartBean
                                    .getElectronicGiftCardCommerceItems();
                            Log.d("ITEMS>>", ""+listOfElectronicGiftCardCommerceItemBean);
                            if (null != listOfCommerceItemBean
                                    && !listOfCommerceItemBean.isEmpty()) {
                                if (null != listOfElectronicGiftCardCommerceItemBean
                                        && !listOfElectronicGiftCardCommerceItemBean
                                        .isEmpty()) {
                                    listOfCommerceItemBean
                                            .addAll(listOfElectronicGiftCardCommerceItemBean);

                                }
                                onlyGiftCard = false;
                                for (int j = 0; j < listOfCommerceItemBean.size(); j++) {

                                }

                            } else if (null != listOfElectronicGiftCardCommerceItemBean
                                    && !listOfElectronicGiftCardCommerceItemBean
                                    .isEmpty()) {
                                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                                onlyGiftCard = true;
                                btnExpressCheckoutAtTop
                                        .setVisibility(View.INVISIBLE);
                            }
                            OrderDetailBean orderDetailBean = cartBean
                                    .getOrderDetails();

                            StringBuffer productString = new StringBuffer();
                            if (null != listOfCommerceItemBean) {
                                for (int i = 0; i < listOfCommerceItemBean.size(); i++) {
                                    productString.append(";");
                                    productString.append(listOfCommerceItemBean
                                            .get(i).getCatalogRefId());
                                }
                            } else {
                                productString = null;
                            }

                            if (null != productString && !productString.equals(" ")) {

                                OmnitureTracking
                                        .startActivity(ViewItemsInBasketActivity.this);

                                Map<String, Object> omnitureData = new HashMap<String, Object>();
                                omnitureData.put(WebserviceConstants.PRODUCTS_KEY,
                                        productString.toString());
                                omnitureData.put(WebserviceConstants.EVENT_KEY,
                                        "scView");

                                Analytics.trackAction(
                                        WebserviceConstants.EVENT_SC_VIEW,
                                        omnitureData);
                                OmnitureTracking.stopActivity();

							/*
                             * trackEvarsUsingActionName(
							 * ViewItemsInBasketActivity.this,
							 * WebserviceConstants.EVENT_SC_VIEW,
							 * WebserviceConstants.PRODUCTS_KEY,
							 * productString.toString());
							 */
                            }

                            if (null != orderDetailBean) {

                                updateTieredBreakUpPrice(orderDetailBean);
                                Double freeShippingAmount = orderDetailBean
                                        .getFreeShippingAmount();
                                // Double subtotalForFreeShipCheck = orderDetailBean
                                // .getSubtotalForFreeShipCheck();
                                Double subtotalForFreeShipCheck = calculateSubTotalForFreeShipCheck(cartBean);

                                calculateSubTotalForFreeShipCheck(cartBean);

                                Double shippingAmount = orderDetailBean
                                        .getShipping();
                                if (!(UltaDataCache.getDataCacheInstance()
                                        .isErrorHappened())) {
                                    if (null != orderDetailBean.getCouponCode()) {
                                        txtEnterCouponCode.setText(orderDetailBean
                                                .getCouponCode());
                                        btnApplyCouponCode.setVisibility(View.GONE);
                                        btnRemoveCouponCode
                                                .setVisibility(View.VISIBLE);
                                    }
                                }
                                updatePromoMessage(orderDetailBean);
                                tvShippingCharges
                                        .setText("Standard Ground Shipping");
                                if (subtotalForFreeShipCheck >= freeShippingAmount) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else if (onlyGiftCard) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else if (freeShippingAmount == 0.0
                                        || shippingAmount == 0.0) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else {
                                    if (shippingAmount == 16.95) {
                                        tvShippingCharges
                                                .setText("UPS Next Day Air");
                                        tvShippingChargesValue.setText("$"
                                                + shippingAmount);
                                    } else if (shippingAmount == 9.95) {
                                        tvShippingCharges
                                                .setText("UPS 2nd Day Air");
                                        tvShippingChargesValue.setText("$"
                                                + shippingAmount);
                                    } else if (shippingAmount == 5.95) {
                                        tvShippingCharges
                                                .setText("Standard Ground Shipping");
                                        tvShippingChargesValue.setText("$"
                                                + shippingAmount);
                                    }

                                }

                            }

                            setItemCountInBasket(getCountOfItemsInBAsket());
                            if (null != listOfCommerceItemBean) {
                                giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
                                giftBoxAndNoteLayout.setVisibility(View.GONE);
                                inflateCartItemsToLayout();

                            } else {
                                disableDone();
                                mainLayout.setVisibility(View.INVISIBLE);
                                mCheckoutButtonsLayout
                                        .setVisibility(View.INVISIBLE);
                                if (haveInternet()) {
                                    checkDensityAndSetImage(emptyBagImageView,
                                            WebserviceConstants.EMPTY_BAG,
                                            R.drawable.empty_bag, "Basket", null, false);
                                } else {
                                    emptyBagImageView
                                            .setImageResource(R.drawable.empty_bag);
                                }
                                UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                        false);
                                giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
                                giftBoxAndNoteLayout.setVisibility(View.GONE);
                                basketEmptyLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            SharedPreferences staySignedInSharedPref = getSharedPreferences(
                                    WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

                            boolean isStaySignedIn = staySignedInSharedPref
                                    .getBoolean(
                                            WebserviceConstants.IS_STAY_SIGNED_IN,
                                            false);
                            SharedPreferences basketPreferences = getSharedPreferences(
                                    WebserviceConstants.COUNTS_PREFS_NAME, MODE_PRIVATE);
                            int storedBasketCount = basketPreferences.getInt(
                                    WebserviceConstants.BASKET_COUNT, 0);


                            if (isStaySignedIn && storedBasketCount > 0 ) {
                                //to clear the existing session
                                Utility.saveCookie(WebserviceConstants.SESSION_ID_COOKIE, null);
                                askRelogin(ViewItemsInBasketActivity.this);
                            } else {

                                disableDone();
                                setItemCountInBasket(0);
                                mainLayout.setVisibility(View.INVISIBLE);
                                mCheckoutButtonsLayout
                                        .setVisibility(View.INVISIBLE);
                                if (haveInternet()) {
                                    checkDensityAndSetImage(emptyBagImageView,
                                            WebserviceConstants.EMPTY_BAG,
                                            R.drawable.empty_bag, "Basket", null, false);
                                } else {
                                    emptyBagImageView
                                            .setImageResource(R.drawable.empty_bag);
                                }
                                UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                        false);
                                giftBoxAndNoteLayout = (LinearLayout) findViewById(R.id.lytGiftBoxAndNote);
                                giftBoxAndNoteLayout.setVisibility(View.GONE);
                                basketEmptyLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                } else {

                }
                // basketPopulationAction.leaveAction();

            }
        }
    }

    @Override
    public void onBackPressed() {
        if (loadingDialog.getVisibility() == View.GONE) {
            finish();
        }
    }

    private void fnInvokeUpdateQuantity(String relationshipId, String quantity,
                                        String displayName) {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.UPDATE_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnPopulateUpdateQuantityParameters(
                relationshipId, quantity));
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        UpdateQuantityHandler updateQuantityHandler = new UpdateQuantityHandler(
                displayName);
        invokerParams.setUltaHandler(updateQuantityHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeRetrieveBasketItems()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> fnPopulateUpdateQuantityParameters(
            String relationshipId, String quantity) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put(relationshipId, quantity);
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    public class UpdateQuantityHandler extends UltaHandler {
        @SuppressWarnings("unused")
        private String displayName;

        public UpdateQuantityHandler(String displayName) {
            this.displayName = displayName;
        }

        public void handleMessage(Message msg) {

            Logger.Log("<update qty Handler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            ViewItemsInBasketActivity.this);

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<update qty Handler><handleMessage><getErrorMessage>>"
                        + (getResponseBean()));
                AddToCartBean viewCartBean = (AddToCartBean) getResponseBean();

                if (null != viewCartBean) {
                    if (viewCartBean.getErrorInfos() != null) {

                        List<String> CouponError = viewCartBean.getErrorInfos();

                        try {
                            notifyUser(CouponError.get(0),
                                    ViewItemsInBasketActivity.this);
                            setError(ViewItemsInBasketActivity.this,
                                    CouponError.get(0));
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }

                    } else {
                        if (null != viewCartBean) {
                            Double totalAdjustment = 0.0;
                            List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();
//                            if (null != orderAdjustments
//                                    && !orderAdjustments.isEmpty()) {
//
//                                for (int i = 0; i < orderAdjustments.size(); i++) {
//                                    totalAdjustment = totalAdjustment
//                                            + orderAdjustments.get(i)
//                                            .getTotalAdjustment();
//                                }
//                                tvPriceTotal
//                                        .setText("$"
//                                                + String.format(
//                                                "%.2f",
//                                                Double.valueOf(totalAdjustment)));
//                                orderTotal = totalAdjustment;
//                            } else {
                            orderTotal = viewCartBean.getComponent()
                                    .getCart().getOrderDetails()
                                    .getRawSubtotal();
                            tvPriceTotal.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(orderTotal)));
//                            }
                            CartBean cartBean = viewCartBean.getComponent()
                                    .getCart();
                            listOfCommerceItemBean = cartBean
                                    .getCommerceItems();
                            /********************************************************************************************/

                            listOfElectronicGiftCardCommerceItemBean = cartBean
                                    .getElectronicGiftCardCommerceItems();
                            if (null != listOfCommerceItemBean
                                    && !listOfCommerceItemBean.isEmpty()) {
                                if (null != listOfElectronicGiftCardCommerceItemBean
                                        && !listOfElectronicGiftCardCommerceItemBean
                                        .isEmpty()) {
                                    listOfCommerceItemBean
                                            .addAll(listOfElectronicGiftCardCommerceItemBean);

                                }
                                onlyGiftCard = false;
                            } else if (null != listOfElectronicGiftCardCommerceItemBean
                                    && !listOfElectronicGiftCardCommerceItemBean
                                    .isEmpty()) {
                                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                                onlyGiftCard = true;
                            }

                            OrderDetailBean orderDetailBean = cartBean
                                    .getOrderDetails();
                            if (null != orderDetailBean) {
                                updateTieredBreakUpPrice(orderDetailBean);
                                updatePromoMessage(orderDetailBean);
                                Double freeShippingAmount = orderDetailBean
                                        .getFreeShippingAmount();
                                Double subtotalForFreeShipCheck = orderDetailBean
                                        .getSubtotalForFreeShipCheck();
                                Double shippingAmount = orderDetailBean
                                        .getShipping();
                                tvShippingCharges
                                        .setText("Standard Ground Shipping:");
                                if (subtotalForFreeShipCheck >= freeShippingAmount) {
                                    tvShippingChargesValue.setText("FREE*");

                                } else if (onlyGiftCard) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else if (freeShippingAmount == 0.0
                                        || shippingAmount == 0.0) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else {
                                    if (shippingAmount == 16.95) {
                                        tvShippingCharges
                                                .setText("UPS Next Day Air:");
                                    } else if (shippingAmount == 9.95) {
                                        tvShippingCharges
                                                .setText("UPS 2nd Day Air:");
                                    } else if (shippingAmount == 5.95) {
                                        tvShippingCharges
                                                .setText("Standard Ground Shipping:");
                                    }
                                    tvShippingChargesValue.setText("$"
                                            + shippingAmount);
                                }
                            }
                            setItemCountInBasket(getCountOfItemsInBAsket());
                            if (null != listOfCommerceItemBean) {
                                inflateCartItemsToLayout();

                            }
                        }
                    }
                }
            }
        }
    }

    private void populatePage(AddToCartBean addToCartBean) {
        // checkoutFlowAction.leaveAction();
        Logger.Log(">>>>>>>>>populatePage");
        if (null != addToCartBean) {
            Double totalAdjustment = 0.0;
            List<OrderAdjustmentBean> orderAdjustments = addToCartBean
                    .getComponent().getCart().getOrderAdjustments();

//            if (null != orderAdjustments && !orderAdjustments.isEmpty()) {
//
//                for (int i = 0; i < orderAdjustments.size(); i++) {
//                    totalAdjustment = totalAdjustment
//                            + orderAdjustments.get(i).getTotalAdjustment();
//                }
//                tvPriceTotal
//                        .setText("$"
//                                + String.format("%.2f",
//                                Double.valueOf(totalAdjustment)));
//                orderTotal = totalAdjustment;
//            } else {
            orderTotal = addToCartBean.getComponent().getCart()
                    .getOrderDetails().getRawSubtotal();
            tvPriceTotal.setText("$"
                    + String.format("%.2f", Double.valueOf(orderTotal)));
//            }
            CartBean cartBean = addToCartBean.getComponent().getCart();
            listOfCommerceItemBean = cartBean.getCommerceItems();
            /********************************************************************************************/

            listOfElectronicGiftCardCommerceItemBean = cartBean
                    .getElectronicGiftCardCommerceItems();
            if (null != listOfCommerceItemBean
                    && !listOfCommerceItemBean.isEmpty()) {
                if (null != listOfElectronicGiftCardCommerceItemBean
                        && !listOfElectronicGiftCardCommerceItemBean.isEmpty()) {
                    listOfCommerceItemBean
                            .addAll(listOfElectronicGiftCardCommerceItemBean);

                }
                onlyGiftCard = false;
            } else if (null != listOfElectronicGiftCardCommerceItemBean
                    && !listOfElectronicGiftCardCommerceItemBean.isEmpty()) {
                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                onlyGiftCard = true;
            }

            OrderDetailBean orderDetailBean = cartBean.getOrderDetails();
            if (null != orderDetailBean) {
                updatePromoMessage(orderDetailBean);
                Double freeShippingAmount = orderDetailBean
                        .getFreeShippingAmount();
                // Double subtotalForFreeShipCheck = orderDetailBean
                // .getSubtotalForFreeShipCheck();

                Double subtotalForFreeShipCheck = calculateSubTotalForFreeShipCheck(cartBean);
                Double shippingAmount = orderDetailBean.getShipping();
                tvShippingCharges.setText("Standard Ground Shipping:");
                if (subtotalForFreeShipCheck >= freeShippingAmount) {
                    tvShippingChargesValue.setText("FREE*");

                } else if (onlyGiftCard) {
                    tvShippingChargesValue.setText("FREE*");
                } else if (freeShippingAmount == 0.0 || shippingAmount == 0.0) {
                    tvShippingChargesValue.setText("FREE*");
                } else {
                    if (shippingAmount == 16.95) {
                        tvShippingCharges.setText("UPS Next Day Air:");
                    } else if (shippingAmount == 9.95) {
                        tvShippingCharges.setText("UPS 2nd Day Air:");
                    } else if (shippingAmount == 5.95) {
                        tvShippingCharges.setText("Standard Ground Shipping:");
                    }
                    tvShippingChargesValue.setText(" " + shippingAmount);
                }
            }
            setItemCountInBasket(getCountOfItemsInBAsket());
            if (null != listOfCommerceItemBean) {
                inflateCartItemsToLayout();

            }
        } else {

        }

    }

    private void fnInvokeRemoveItem(CommerceItemBean commerceBeanRemove) {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.REMOVEITEM_FROM_ORDERBY_RELATIONSHIPID_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateRemoveItemParameters(commerceBeanRemove));
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        RemoveItemHandler removeItemHandler = new RemoveItemHandler(
                commerceBeanRemove);
        invokerParams.setUltaHandler(removeItemHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeRemoveItem()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> fnPopulateRemoveItemParameters(
            CommerceItemBean commerceItemBean) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("removalRelationshipIdProxy",
                commerceItemBean.getRelationshipId());
        // Added for 4.2 release (sending sku id to remove service)
        urlParams.put("removeSkuId", commerceItemBean.getCatalogRefId());
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    public class RemoveItemHandler extends UltaHandler {
        private CommerceItemBean commerceBeanRemove;

        public RemoveItemHandler(CommerceItemBean commerceBeanRemove) {
            this.commerceBeanRemove = commerceBeanRemove;
        }

        public void handleMessage(Message msg) {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            ViewItemsInBasketActivity.this);
                    setError(ViewItemsInBasketActivity.this, getErrorMessage());

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                UltaDataCache.getDataCacheInstance()
                        .setAnonymousCheckout(false);
                if (null != commerceBeanRemove.getProductId()) {

                    if (null != commerceBeanRemove.getId()) {
                        String productString = ";"
                                + commerceBeanRemove.getCatalogRefId();

                        OmnitureTracking
                                .startActivity(ViewItemsInBasketActivity.this);

                        Map<String, Object> omnitureData = new HashMap<String, Object>();
                        omnitureData.put(WebserviceConstants.PRODUCTS_KEY,
                                productString.toString());
                        omnitureData.put(WebserviceConstants.EVENT_KEY,
                                "scRemove");

                        Analytics.trackAction(
                                WebserviceConstants.EVENT_SC_REMOVE,
                                omnitureData);
                        OmnitureTracking.stopActivity();
                        /*
                         * trackEvarsUsingActionName(
						 * ViewItemsInBasketActivity.this,
						 * WebserviceConstants.EVENT_SC_REMOVE,
						 * WebserviceConstants.PRODUCTS_KEY, productString);
						 */
                    }

                    if (null != commerceBeanRemove.getPromotions()
                            && commerceBeanRemove.getPromotions().size() != 0) {
                        for (int i = 0; i < commerceBeanRemove.getPromotions()
                                .size(); i++) {
                            if (commerceBeanRemove.getPromotions().get(i)
                                    .getOfferDesc() != null
                                    && commerceBeanRemove
                                    .getPromotions()
                                    .get(i)
                                    .getOfferDesc()
                                    .equals("Special Free Gift with Purchase")) {
                                if (null != UltaDataCache
                                        .getDataCacheInstance()
                                        .getHmWithFreeGiftsReverse()) {
                                    hmPromSkuIdandPrID = UltaDataCache
                                            .getDataCacheInstance()
                                            .getHmWithFreeGiftsReverse();

                                    String promoList[] = new String[hmPromSkuIdandPrID
                                            .size()];
                                    int count = 0;
                                    for (Entry<String, String> entry : hmPromSkuIdandPrID
                                            .entrySet()) {
                                        if (entry.getKey().equals(
                                                commerceBeanRemove
                                                        .getPromotions().get(i)
                                                        .getId())) {
                                            promoList[count] = entry.getKey();
                                            count++;
                                        }

                                    }
                                    for (int j = 0; j < count; j++) {
                                        UltaDataCache.getDataCacheInstance()
                                                .getHmWithFreeGiftsReverse()
                                                .remove(promoList[j]);
                                        UltaDataCache.getDataCacheInstance()
                                                .getHmWithPromoAndFreeGifts()
                                                .remove(promoList[j]);
                                    }
                                }
                            }
                        }
                    } else {
                        if (null != UltaDataCache.getDataCacheInstance()
                                .getHmWithFreeGiftsReverse()) {
                            hmPromSkuIdandPrID = UltaDataCache
                                    .getDataCacheInstance()
                                    .getHmWithFreeGiftsReverse();
                            if (null != UltaDataCache.getDataCacheInstance()
                                    .getHmWithPromoAndFreeGifts()) {
                                hmPrmIDandGiftSkuId = UltaDataCache
                                        .getDataCacheInstance()
                                        .getHmWithPromoAndFreeGifts();
                                String promoList[] = new String[hmPrmIDandGiftSkuId
                                        .size()];
                                int count = 0;
                                for (Entry<String, String> entry : hmPrmIDandGiftSkuId
                                        .entrySet()) {
                                    if (entry.getValue().equals(
                                            commerceBeanRemove
                                                    .getCatalogRefId())) {
                                        promoList[count] = entry.getKey();
                                        count++;
                                    }

                                }

                                for (int j = 0; j < count; j++) {
                                    UltaDataCache.getDataCacheInstance()
                                            .getHmWithFreeGiftsReverse()
                                            .remove(promoList[j]);
                                    UltaDataCache.getDataCacheInstance()
                                            .getHmWithPromoAndFreeGifts()
                                            .remove(promoList[j]);
                                }

                            }
                        }
                    }
                }
                Logger.Log("<RemoveItemHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                AddToCartBean viewCartBean = (AddToCartBean) getResponseBean();

                if (null != viewCartBean) {
                    if (viewCartBean.getErrorInfos() != null) {
                        List<String> CouponError = viewCartBean.getErrorInfos();
                        try {
                            notifyUser(CouponError.get(0),
                                    ViewItemsInBasketActivity.this);
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }

                    } else {
                        if (null != viewCartBean) {

                            Toast.makeText(
                                    ViewItemsInBasketActivity.this,
                                    commerceBeanRemove.getDisplayName()
                                            + "  is Successfully Removed from the cart",
                                    Toast.LENGTH_SHORT).show();
                            Double totalAdjustment = 0.0;
                            List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();
//                            if (null != orderAdjustments
//                                    && !orderAdjustments.isEmpty()) {
//
//                                for (int i = 0; i < orderAdjustments.size(); i++) {
//                                    totalAdjustment = totalAdjustment
//                                            + orderAdjustments.get(i)
//                                            .getTotalAdjustment();
//                                }
//                                tvPriceTotal
//                                        .setText("$"
//                                                + String.format(
//                                                "%.2f",
//                                                Double.valueOf(totalAdjustment)));
//                                orderTotal = totalAdjustment;
//                            } else {
                            orderTotal = viewCartBean.getComponent()
                                    .getCart().getOrderDetails()
                                    .getRawSubtotal();
                            tvPriceTotal.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(orderTotal)));
//                            }
                            CartBean cartBean = viewCartBean.getComponent()
                                    .getCart();
                            listOfCommerceItemBean = cartBean
                                    .getCommerceItems();
                            /********************************************************************************************/
                            listOfElectronicGiftCardCommerceItemBean = cartBean
                                    .getElectronicGiftCardCommerceItems();
                            if (null != listOfCommerceItemBean
                                    && !listOfCommerceItemBean.isEmpty()) {
                                if (null != listOfElectronicGiftCardCommerceItemBean
                                        && !listOfElectronicGiftCardCommerceItemBean
                                        .isEmpty()) {
                                    listOfCommerceItemBean
                                            .addAll(listOfElectronicGiftCardCommerceItemBean);

                                }
                                onlyGiftCard = false;
                            } else if (null != listOfElectronicGiftCardCommerceItemBean
                                    && !listOfElectronicGiftCardCommerceItemBean
                                    .isEmpty()) {
                                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                                onlyGiftCard = true;
                                btnExpressCheckoutAtTop
                                        .setVisibility(View.INVISIBLE);
                            }

                            OrderDetailBean orderDetailBean = cartBean
                                    .getOrderDetails();
                            if (null != orderDetailBean) {
                                updateTieredBreakUpPrice(orderDetailBean);
                                updatePromoMessage(orderDetailBean);
                                Double freeShippingAmount = orderDetailBean
                                        .getFreeShippingAmount();
                                // Double subtotalForFreeShipCheck =
                                // orderDetailBean
                                // .getSubtotalForFreeShipCheck();
                                Double subtotalForFreeShipCheck = calculateSubTotalForFreeShipCheck(cartBean);
                                Double shippingAmount = orderDetailBean
                                        .getShipping();
                                tvShippingCharges
                                        .setText("Standard Ground Shipping:");
                                if (subtotalForFreeShipCheck >= freeShippingAmount) {
                                    tvShippingChargesValue.setText("FREE*");

                                } else if (onlyGiftCard) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else if (freeShippingAmount == 0.0
                                        || shippingAmount == 0.0) {
                                    tvShippingChargesValue.setText("FREE*");
                                } else {
                                    if (shippingAmount == 16.95) {
                                        tvShippingCharges
                                                .setText("UPS Next Day Air:");
                                    } else if (shippingAmount == 9.95) {
                                        tvShippingCharges
                                                .setText("UPS 2nd Day Air:");
                                    } else if (shippingAmount == 5.95) {
                                        tvShippingCharges
                                                .setText("Standard Ground Shipping:");
                                    }
                                    tvShippingChargesValue.setText("$"
                                            + shippingAmount);
                                }
                            }
                            setItemCountInBasket(getCountOfItemsInBAsket());

                            if (null != listOfCommerceItemBean) {

                                inflateCartItemsToLayout();
                            } else if (countItem == 1) {
                                if (haveInternet()) {
                                    checkDensityAndSetImage(emptyBagImageView,
                                            WebserviceConstants.EMPTY_BAG,
                                            R.drawable.empty_bag, "Basket", null, false);
                                } else {
                                    emptyBagImageView
                                            .setImageResource(R.drawable.empty_bag);
                                }
                                UltaDataCache.getDataCacheInstance().setGiftTheOrder(false);
                                basketEmptyLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.INVISIBLE);
                                mCheckoutButtonsLayout
                                        .setVisibility(View.INVISIBLE);
                                llInflateCartItems.removeViewAt(0);
                                tvPriceTotal.setText(" ");
                                setItemCountInBasket(0);
                            }
                            if (null == listOfCommerceItemBean) {
                                refreshBasket();
                            }
                        }
                    }
                }
            }
        }
    }

    private void fnInvokeApplyCouponCode(String couponCode) {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.UPDATE_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams
                .setUrlParameters(fnPopulateApplyCouponCodeParameters(couponCode));
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        ApplyCouponCodeHandler applyCouponCodeHandler = new ApplyCouponCodeHandler();
        invokerParams.setUltaHandler(applyCouponCodeHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeApplyCouponCode()><UltaException>>"
                    + ultaException);
        }
    }

    private void fnInvokeRemoveCouponCode() {
        InvokerParams<AddToCartBean> invokerParams = new InvokerParams<AddToCartBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.REMOVE_COUPON);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(fnPopulateRemoveCouponCodeParameters());
        invokerParams.setUltaBeanClazz(AddToCartBean.class);
        RemoveCouponCodeHandler removeCouponCodeHandler = new RemoveCouponCodeHandler();
        invokerParams.setUltaHandler(removeCouponCodeHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<ViewItemsInBasketActivity><fnInvokeApplyCouponCode()><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> fnPopulateRemoveCouponCodeParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        return urlParams;
    }

    private Map<String, String> fnPopulateApplyCouponCodeParameters(
            String couponCode) {
        if (!couponCode.matches("[a-zA-Z.? ]*")) {
            try {
                couponCode = URLEncoder.encode(couponCode, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("couponCode", couponCode);
        return urlParams;

    }

    public class ApplyCouponCodeHandler extends UltaHandler {
        public void handleMessage(Message msg) {
            Logger.Log("<ApplyCouponCodeHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            txtEnterCouponCode.setText("");

            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            ViewItemsInBasketActivity.this);
                    UltaDataCache.getDataCacheInstance().setErrorHappened(true);
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }

            } else {

                Logger.Log("<ApplyCouponCodeHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                AddToCartBean viewCartBean = (AddToCartBean) getResponseBean();
                if (null != viewCartBean) {
                    if (viewCartBean.getErrorInfos() != null) {
                        List<String> CouponError = viewCartBean.getErrorInfos();
                        UltaDataCache.getDataCacheInstance().setErrorHappened(
                                true);
                        try {
                            notifyUser(CouponError.get(0),
                                    ViewItemsInBasketActivity.this);
                            setError(ViewItemsInBasketActivity.this,
                                    CouponError.get(0));
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }

                    } else {
                        if (null != viewCartBean) {
                            txtEnterCouponCode.setEnabled(false);
                            if (null != viewCartBean.getComponent().getCart()
                                    .getOrderDetails().getCouponCode()) {
                                UltaDataCache.getDataCacheInstance()
                                        .setErrorHappened(false);
                                txtEnterCouponCode.setText(viewCartBean
                                        .getComponent().getCart()
                                        .getOrderDetails().getCouponCode());
                                UltaDataCache.getDataCacheInstance()
                                        .setCouponCode(
                                                viewCartBean.getComponent()
                                                        .getCart()
                                                        .getOrderDetails()
                                                        .getCouponCode());
                            }
                            mCouponCodeEditTextLayout
                                    .setVisibility(View.VISIBLE);
                            btnApplyCouponCode.setVisibility(View.GONE);
                            btnRemoveCouponCode.setVisibility(View.VISIBLE);
                            Toast.makeText(ViewItemsInBasketActivity.this,
                                    "Coupon code applied successfully",
                                    Toast.LENGTH_SHORT).show();
                            Double totalAdjustment = 0.0;
                            List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();
//                            if (null != orderAdjustments
//                                    && !orderAdjustments.isEmpty()) {
//                                for (int i = 0; i < orderAdjustments.size(); i++) {
//                                    totalAdjustment = totalAdjustment
//                                            + orderAdjustments.get(i)
//                                            .getTotalAdjustment();
//                                }
//                                tvPriceTotal
//                                        .setText("$"
//                                                + String.format(
//                                                "%.2f",
//                                                Double.valueOf(totalAdjustment)));
//                                orderTotal = totalAdjustment;
//                            } else {
                            orderTotal = viewCartBean.getComponent()
                                    .getCart().getOrderDetails()
                                    .getRawSubtotal();
                            tvPriceTotal.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(orderTotal)));
//                            }

                            CartBean cartBean = viewCartBean.getComponent()
                                    .getCart();
                            listOfCommerceItemBean = cartBean
                                    .getCommerceItems();
                            /********************************************************************************************/
                            listOfElectronicGiftCardCommerceItemBean = cartBean
                                    .getElectronicGiftCardCommerceItems();
                            if (null != listOfCommerceItemBean
                                    && !listOfCommerceItemBean.isEmpty()) {
                                if (null != listOfElectronicGiftCardCommerceItemBean
                                        && !listOfElectronicGiftCardCommerceItemBean
                                        .isEmpty()) {
                                    listOfCommerceItemBean
                                            .addAll(listOfElectronicGiftCardCommerceItemBean);
                                }

                            } else if (null != listOfElectronicGiftCardCommerceItemBean
                                    && !listOfElectronicGiftCardCommerceItemBean
                                    .isEmpty()) {
                                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                            }

                            if (null != listOfCommerceItemBean) {
                                inflateCartItemsToLayout();
                                setItemCountInBasket(getCountOfItemsInBAsket());
                                refreshBasket();
                            } else {
                                if (haveInternet()) {
                                    checkDensityAndSetImage(emptyBagImageView,
                                            WebserviceConstants.EMPTY_BAG,
                                            R.drawable.empty_bag, "Basket", null, false);
                                } else {
                                    emptyBagImageView
                                            .setImageResource(R.drawable.empty_bag);
                                }
                                UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                        false);
                                basketEmptyLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    if (haveInternet()) {
                        checkDensityAndSetImage(emptyBagImageView,
                                WebserviceConstants.EMPTY_BAG,
                                R.drawable.empty_bag, "Basket", null, false);
                    } else {
                        emptyBagImageView
                                .setImageResource(R.drawable.empty_bag);
                    }
                    UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                            false);
                    basketEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public class RemoveCouponCodeHandler extends UltaHandler {
        /**
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RemoveCouponCodeHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            txtEnterCouponCode.setText("");

            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            ViewItemsInBasketActivity.this);

                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }

            } else {

                Logger.Log("<RemoveCouponCodeHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                AddToCartBean viewCartBean = (AddToCartBean) getResponseBean();
                if (null != viewCartBean) {
                    if (viewCartBean.getErrorInfos() != null) {
                        List<String> CouponError = viewCartBean.getErrorInfos();
                        try {
                            notifyUser(CouponError.get(0),
                                    ViewItemsInBasketActivity.this);
                            setError(ViewItemsInBasketActivity.this,
                                    CouponError.get(0));
                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }

                    } else {
                        if (null != viewCartBean) {
                            mCouponCodeEditTextLayout
                                    .setVisibility(View.VISIBLE);
                            txtEnterCouponCode.setEnabled(true);
                            txtEnterCouponCode.setVisibility(View.VISIBLE);
                            btnApplyCouponCode.setVisibility(View.VISIBLE);
                            btnRemoveCouponCode.setVisibility(View.GONE);
                            mApplyCouponCodeTextView
                                    .setText("Apply Coupon Code");
                            mCouponAmountTextView.setVisibility(View.GONE);
                            couponCodeAndPromotion.setVisibility(View.GONE);
                            mExpandImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(ViewItemsInBasketActivity.this,
                                    "Coupon code removed successfully",
                                    Toast.LENGTH_SHORT).show();
                            UltaDataCache.getDataCacheInstance().setCouponCode(
                                    " ");
                            Double totalAdjustment = 0.0;
                            List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();
//                            if (null != orderAdjustments
//                                    && !orderAdjustments.isEmpty()) {
//                                for (int i = 0; i < orderAdjustments.size(); i++) {
//                                    totalAdjustment = totalAdjustment
//                                            + orderAdjustments.get(i)
//                                            .getTotalAdjustment();
//                                }
//                                tvPriceTotal
//                                        .setText("$"
//                                                + String.format(
//                                                "%.2f",
//                                                Double.valueOf(totalAdjustment)));
//                                orderTotal = totalAdjustment;
//                            } else {
                            orderTotal = viewCartBean.getComponent()
                                    .getCart().getOrderDetails()
                                    .getRawSubtotal();
                            tvPriceTotal.setText("$"
                                    + String.format("%.2f",
                                    Double.valueOf(orderTotal)));
//                            }

                            CartBean cartBean = viewCartBean.getComponent()
                                    .getCart();
                            listOfCommerceItemBean = cartBean
                                    .getCommerceItems();
                            /********************************************************************************************/
                            listOfElectronicGiftCardCommerceItemBean = cartBean
                                    .getElectronicGiftCardCommerceItems();
                            if (null != listOfCommerceItemBean
                                    && !listOfCommerceItemBean.isEmpty()) {
                                if (null != listOfElectronicGiftCardCommerceItemBean
                                        && !listOfElectronicGiftCardCommerceItemBean
                                        .isEmpty()) {
                                    listOfCommerceItemBean
                                            .addAll(listOfElectronicGiftCardCommerceItemBean);
                                }

                            } else if (null != listOfElectronicGiftCardCommerceItemBean
                                    && !listOfElectronicGiftCardCommerceItemBean
                                    .isEmpty()) {
                                listOfCommerceItemBean = listOfElectronicGiftCardCommerceItemBean;
                            }

                            if (null != listOfCommerceItemBean) {
                                inflateCartItemsToLayout();
                                setItemCountInBasket(getCountOfItemsInBAsket());
                                refreshBasket();
                            } else {
                                if (haveInternet()) {
                                    checkDensityAndSetImage(emptyBagImageView,
                                            WebserviceConstants.EMPTY_BAG,
                                            R.drawable.empty_bag, "Basket", null, false);
                                } else {
                                    emptyBagImageView
                                            .setImageResource(R.drawable.empty_bag);
                                }
                                basketEmptyLayout.setVisibility(View.VISIBLE);
                                UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                                        false);
                            }
                        }
                    }
                } else {
                    if (haveInternet()) {
                        checkDensityAndSetImage(emptyBagImageView,
                                WebserviceConstants.EMPTY_BAG,
                                R.drawable.empty_bag, "Basket", null, false);
                    } else {
                        emptyBagImageView
                                .setImageResource(R.drawable.empty_bag);
                    }
                    UltaDataCache.getDataCacheInstance().setGiftTheOrder(
                            false);
                    basketEmptyLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /**
     * Method to show sort options.
     */
    public void showQuantityDialog(ArrayList<String> quantities,
                                   final CommerceItemBean commerceItemBean) {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.qty_list_item, quantities);
        final AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Quantity");
        b.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int quantity = which + 1;
                String strQuantity = Integer.valueOf(quantity).toString();
                pd.setMessage(UPDATING_PROGRESS_TEXT);
                pd.show();
                fnInvokeUpdateQuantity(commerceItemBean.getRelationshipId(),
                        strQuantity, commerceItemBean.getDisplayName());

            }
        });
        AlertDialog quantityDialog = b.create();
        quantityDialog.show();
    }

    private void displayCouponApplied(ViewCartBean viewCartBean) {
        boolean isCouponApplied = false;
        String couponAmount = "";
        if (null != viewCartBean) {

            if (null != viewCartBean.getCart()) {
                if (null != viewCartBean.getCart().getCouponDiscount()
                        && null != viewCartBean.getCart().getCouponDiscount()
                        .getTotalAdjustment()) {
                    couponAmount = String.format(
                            "%.2f",
                            Double.valueOf(viewCartBean.getCart()
                                    .getCouponDiscount().getTotalAdjustment()));
                    couponAmount = "- $" + couponAmount;

                    if (null != viewCartBean.getCart()
                            .getCouponDiscount().getCouponCode()) {

                        String promoDescription = viewCartBean.getCart()
                                .getCouponDiscount().getCouponDescription();
                        String couponPromoMessage = "";

                        couponPromoMessage = "Coupon code "
                                + viewCartBean.getCart()
                                .getCouponDiscount()
                                .getCouponCode()
                                + " has been applied.";
                        if (null != viewCartBean.getCart().getCouponDiscount().getOrderDiscount() &&
                                !Double.valueOf(viewCartBean.getCart().getCouponDiscount().getOrderDiscount()).equals(0.0)) {
                            couponPromoMessage = "Coupon code "
                                    + viewCartBean.getCart()
                                    .getCouponDiscount()
                                    .getCouponCode()
                                    + " has been applied to the order total.";
                            isCouponApplied = true;

                        } else if (null != viewCartBean.getCart().getCouponDiscount().getItemDiscount() &&
                                !Double.valueOf(viewCartBean.getCart().getCouponDiscount().getItemDiscount()).equals(0.0)) {
                            couponPromoMessage = "Coupon code "
                                    + viewCartBean.getCart()
                                    .getCouponDiscount()
                                    .getCouponCode()
                                    + " has been applied to the order subtotal.";
                            isCouponApplied = true;
                        } else if (null != viewCartBean.getCart().getCouponDiscount().getShippingDiscount()) {
                            couponPromoMessage = "Coupon code "
                                    + viewCartBean.getCart()
                                    .getCouponDiscount()
                                    .getCouponCode()
                                    + " has been applied to the order total.";
                            isCouponApplied = true;
                        }

                        if (null != promoDescription && !promoDescription.startsWith("null")) {
                            couponPromoMessage = couponPromoMessage + "\n" + promoDescription;
                        }

                        if (isCouponApplied) {
                            mApplyCouponCodeTextView.setText("Coupon Discount Amount");
                            mCouponAmountTextView.setText(couponAmount);
                            mCouponAmountTextView.setVisibility(View.VISIBLE);
                            couponCodeAndPromotion.setText(couponPromoMessage);
                            couponCodeAndPromotion.setVisibility(View.VISIBLE);

                            btnApplyCouponCode.setVisibility(View.GONE);
                            btnRemoveCouponCode.setVisibility(View.VISIBLE);
                            txtEnterCouponCode.setText(viewCartBean.getCart()
                                    .getCouponDiscount()
                                    .getCouponCode());
                            txtEnterCouponCode.setEnabled(false);
                            mExpandImageView.setVisibility(View.GONE);
                            mCouponCodeEditTextLayout.setVisibility(View.VISIBLE);
                        }

                    }

                } else if (null != viewCartBean.getCart().getOrderAdjustments()) {
                    List<OrderAdjustmentBean> orderAdjustments = viewCartBean
                            .getCart().getOrderAdjustments();
                    for (int i = 0; i < orderAdjustments.size(); i++) {
                        if (i == 1) {
                            couponAmount = String.format("%.2f", viewCartBean
                                    .getCart().getOrderAdjustments().get(i)
                                    .getTotalAdjustment());
                            couponAmount = "-$" + couponAmount.substring(1);

                        }
                    }
                    if (null != viewCartBean.getCart().getOrderDetails()) {
                        if (null != viewCartBean.getCart()
                                .getCouponDiscount().getCouponCode()) {
                            if (isCouponApplied) {
                                mApplyCouponCodeTextView
                                        .setText("Coupon Discount Amount ");
                                mCouponAmountTextView.setText(couponAmount);
                                mCouponAmountTextView.setVisibility(View.VISIBLE);
                                mExpandImageView.setVisibility(View.GONE);
                                mCouponCodeEditTextLayout
                                        .setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

        }

    }

    private Double calculateSubTotalForFreeShipCheck(CartBean cartBean) {

        Double subtotalForFreeShipCheck = 0.00;
        if (null != cartBean) {

            if (null != cartBean.getOrderAdjustments()) {
                List<OrderAdjustmentBean> orderAdjustments = cartBean
                        .getOrderAdjustments();
                for (int i = 0; i < orderAdjustments.size(); i++) {
                    subtotalForFreeShipCheck = subtotalForFreeShipCheck
                            + cartBean.getOrderAdjustments().get(i)
                            .getTotalAdjustment();
                }
            }
        }

        return subtotalForFreeShipCheck;
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            // invokeCheckoutService();
            navigateToBasketOnSessionTimeout(ViewItemsInBasketActivity.this);
        } else {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

}
