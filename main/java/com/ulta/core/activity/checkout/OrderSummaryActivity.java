/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.checkout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.account.MyOrderHistoryActivity;
import com.ulta.core.activity.account.RegisterDetailsActivity;
import com.ulta.core.activity.account.ShopListActivity;
import com.ulta.core.activity.product.ViewItemsInBasketActivity;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.bean.checkout.CheckoutCommerceItemBean;
import com.ulta.core.bean.checkout.CheckoutLoyaltyPointsPaymentGroupBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.ReviewOrderBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.util.ConversantUtility;
import com.ulta.core.util.OmnitureTracking;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The Class OrderSummaryActivity.
 */
public class OrderSummaryActivity extends UltaBaseActivity {


    private TextView txtEmailId;


    CheckoutShippmentMethodBean checkoutShippmentMethodBean;

    String messageForAnonymousUser;
    private StringBuffer products = new StringBuffer();
    private StringBuffer productsInfo = new StringBuffer();

    private StringBuffer finalEvents = new StringBuffer();

    private ReviewOrderBean reviewOrderBean;
    private TextView mCreditMessage;
    private LinearLayout mLoggedInUserButtonLayout;
    private TextView mEarnRewardsForFutureTextView;
    private LinearLayout mEarnRewardsForFutureLayout;
    private ImageButton mLoggedinOrderHistoryBtn;
    private ImageButton mLoggedinContinueShoppingBtn;

    // Save Guest user details

    //Order Details
    TextView itemsPurchasedTV, orderIDTV, tvorderSubtotal, tvCouponDiscount, tvLoyaltyDiscount, tvShipping, tvTax, tvTotal, tvAdditionalDiscount;
    LinearLayout orderSubtotalLayout, couponDiscountLayout, loyaltyDiscountLayout, shippingLayout, taxLayout, totalLayout, additionalDiscountLayout;


    @Override
    protected void onResume() {
        super.onResume();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.activity.UltaActivity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        setTitle("Order Confirmation");
        if (null != getIntent().getExtras().get("revieworderBean")) {
            reviewOrderBean = (ReviewOrderBean) getIntent().getExtras().get(
                    "revieworderBean");
        }


        trackAppAction(OrderSummaryActivity.this,
                WebserviceConstants.CHECKOUT_STEP_8_EVENT_ACTION);

        trackAppAction(OrderSummaryActivity.this,
                WebserviceConstants.CHECKOUT_STEP_8_VISIT_EVENT_ACTION);

        if (null != UltaDataCache.getDataCacheInstance().getCouponCode()
                && !UltaDataCache.getDataCacheInstance().getCouponCode()
                .equalsIgnoreCase(" ")) {
            trackEvarsUsingActionName(OrderSummaryActivity.this,
                    WebserviceConstants.CHECKOUT_STEP_8_EVENT_ACTION,
                    WebserviceConstants.COUPON_REDEMPTION_KEY, ""
                            + UltaDataCache.getDataCacheInstance()
                            .getCouponCode());
        }

        disableBagIcon();
        UltaDataCache.getDataCacheInstance().setCreditCardDetails(null);
        UltaDataCache.getDataCacheInstance().setBillingAddress(null);
        UltaDataCache.getDataCacheInstance().setGiftCards(null);
        txtEmailId = (TextView) findViewById(R.id.emailId);
        mEarnRewardsForFutureTextView = (TextView) findViewById(R.id.earnrewardsForFuture);
        itemsPurchasedTV = (TextView) findViewById(R.id.itemsPurchasedTV);
        orderIDTV = (TextView) findViewById(R.id.orderIDTV);

        //order details
        tvorderSubtotal = (TextView) findViewById(R.id.tvorderSubtotal);
        tvCouponDiscount = (TextView) findViewById(R.id.tvCouponDiscount);
        tvLoyaltyDiscount = (TextView) findViewById(R.id.tvLoyaltyDiscount);
        tvShipping = (TextView) findViewById(R.id.tvShipping);
        tvTax = (TextView) findViewById(R.id.tvTax);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvAdditionalDiscount = (TextView) findViewById(R.id.tvAdditionalDiscount);

        orderSubtotalLayout = (LinearLayout) findViewById(R.id.orderSubtotalLayout);
        couponDiscountLayout = (LinearLayout) findViewById(R.id.couponDiscountLayout);
        loyaltyDiscountLayout = (LinearLayout) findViewById(R.id.loyaltyDiscountLayout);
        shippingLayout = (LinearLayout) findViewById(R.id.shippingLayout);
        taxLayout = (LinearLayout) findViewById(R.id.taxLayout);
        totalLayout = (LinearLayout) findViewById(R.id.totalLayout);
        additionalDiscountLayout = (LinearLayout) findViewById(R.id.additionalDiscountLayout);

        itemsPurchasedTV.setTypeface(setHelveticaRegulartTypeFace());
        orderIDTV.setTypeface(setHelveticaRegulartTypeFace());
        txtEmailId.setTypeface(setHelveticaRegulartTypeFace());
        mEarnRewardsForFutureTextView
                .setTypeface(setHelveticaRegulartTypeFace(), Typeface.BOLD);
        tvorderSubtotal
                .setTypeface(setHelveticaRegulartTypeFace());
        tvCouponDiscount
                .setTypeface(setHelveticaRegulartTypeFace());
        tvLoyaltyDiscount
                .setTypeface(setHelveticaRegulartTypeFace());
        tvShipping
                .setTypeface(setHelveticaRegulartTypeFace());
        tvTax
                .setTypeface(setHelveticaRegulartTypeFace());
        tvTotal
                .setTypeface(setHelveticaRegulartTypeFace(), Typeface.BOLD);


        mLoggedinOrderHistoryBtn = (ImageButton) findViewById(R.id.loggedInOrderHistory);
        mLoggedinContinueShoppingBtn = (ImageButton) findViewById(R.id.loggedinContinueShopping);

        mLoggedInUserButtonLayout = (LinearLayout) findViewById(R.id.loggedinUsersButtonLayout);
        mCreditMessage = (TextView) findViewById(R.id.creditMessage);
        mEarnRewardsForFutureLayout = (LinearLayout) findViewById(R.id.earn_rewards_for_futureLayout);


        if (null != UltaDataCache.getDataCacheInstance().getQuantity()) {

            SpannableString spannablecontent = new SpannableString(
                    UltaDataCache.getDataCacheInstance().getQuantity()
                            .toString());
            spannablecontent.setSpan(new StyleSpan(
                    android.graphics.Typeface.BOLD), 0, spannablecontent
                    .length(), 0);
            if (UltaDataCache.getDataCacheInstance().getQuantity().equalsIgnoreCase("1")) {
                itemsPurchasedTV.setText(UltaDataCache.getDataCacheInstance().getQuantity()
                        .toString() + " Item purchased");
            } else {
                itemsPurchasedTV.setText(UltaDataCache.getDataCacheInstance().getQuantity()
                        .toString() + " Items purchased");
            }
        }

        if (UltaDataCache.getDataCacheInstance().getHmWithFreeSamplesSelected() != null)
            UltaDataCache.getDataCacheInstance().getHmWithFreeSamplesSelected()
                    .clear();
        checkoutShippmentMethodBean = (CheckoutShippmentMethodBean) getIntent()
                .getExtras().get("revieworder");
        mLoggedinOrderHistoryBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentForOrderHistory = new Intent(
                        OrderSummaryActivity.this, MyOrderHistoryActivity.class);
                startActivity(intentForOrderHistory);
            }
        });
        if (null != checkoutShippmentMethodBean
                && null != checkoutShippmentMethodBean.getComponent()) {
            if (null != checkoutShippmentMethodBean.getComponent().getCart()
                    .getOrderDetails().getId()) {
                orderIDTV.setText("Order #: " + checkoutShippmentMethodBean.getComponent()
                        .getCart().getOrderDetails().getId());

                trackEvarsUsingActionName(OrderSummaryActivity.this,
                        WebserviceConstants.EVENT_PURCHASE_ID,
                        WebserviceConstants.ORDER_ID_KEY,
                        checkoutShippmentMethodBean.getComponent().getCart()
                                .getOrderDetails().getId());

                if (null != checkoutShippmentMethodBean.getComponent()
                        .getEmail())
                    txtEmailId.setText(checkoutShippmentMethodBean
                            .getComponent().getEmail() + ".");

                else
                    txtEmailId.setText("NA");
            } else {

                txtEmailId.setText("NA");
            }
            // Toast.makeText(OrderSummaryActivity.this,
            // "Befor Entering in the loop", 3000).show();
            if (null != checkoutShippmentMethodBean.getComponent()
                    .getBeautyClubNumber()) {
                if (!(UltaDataCache.getDataCacheInstance().isOnlyEgiftCard())) {


                    mLoggedInUserButtonLayout.setVisibility(View.VISIBLE);
                    mCreditMessage.setVisibility(View.VISIBLE);
                    mEarnRewardsForFutureTextView.setVisibility(View.VISIBLE);
                    if (Utility.retrieveBooleanFromSharedPreference(
                            UltaConstants.REWARD_MEMBER,
                            UltaConstants.IS_REWARD_MEMBER,
                            getApplicationContext())) {
                        mEarnRewardsForFutureTextView
                                .setText("You earned "
                                        + checkoutShippmentMethodBean
                                        .getComponent()
                                        .getOrderPoints()
                                        + " Ultamate Rewards Points. Nice!");
                    } else {
                        mEarnRewardsForFutureTextView.setVisibility(View.GONE);
                }
                }
            } else {

                if (null == checkoutShippmentMethodBean.getComponent()
                        .getAnonymousEmailId()
                        || checkoutShippmentMethodBean.getComponent()
                        .getAnonymousEmailId().isEmpty()) {
                    mEarnRewardsForFutureLayout.setVisibility(View.GONE);
                    mLoggedInUserButtonLayout.setVisibility(View.VISIBLE);

                }

            }
            if (null != checkoutShippmentMethodBean.getComponent()
                    .getMessageForAnonymousUsers()
                    && !checkoutShippmentMethodBean.getComponent()
                    .getMessageForAnonymousUsers().isEmpty()) {
                messageForAnonymousUser = checkoutShippmentMethodBean
                        .getComponent().getMessageForAnonymousUsers();


            }
            if (null != messageForAnonymousUser) {
                mCreditMessage.setVisibility(View.VISIBLE);
                mCreditMessage.setText(messageForAnonymousUser);
                mEarnRewardsForFutureLayout.setVisibility(View.VISIBLE);
                mEarnRewardsForFutureTextView.setVisibility(View.GONE);
            }
//            else {
//                mCreditMessage.setVisibility(View.GONE);
//            }
            if (UltaDataCache.getDataCacheInstance().isOrderSubmitted()) {

                if (null != checkoutShippmentMethodBean.getComponent()
                        .getAnonymousEmailId()
                        && !checkoutShippmentMethodBean.getComponent()
                        .getAnonymousEmailId().isEmpty())
                    txtEmailId.setText(checkoutShippmentMethodBean
                            .getComponent().getAnonymousEmailId() + ".");

                if (null != checkoutShippmentMethodBean.getComponent()
                        .getMessageForAnonymousUsers()
                        && !checkoutShippmentMethodBean.getComponent()
                        .getMessageForAnonymousUsers().isEmpty()) {
                    messageForAnonymousUser = checkoutShippmentMethodBean
                            .getComponent().getMessageForAnonymousUsers();


                }
                if (null != messageForAnonymousUser) {
                    mCreditMessage.setVisibility(View.VISIBLE);
                    mCreditMessage.setText(messageForAnonymousUser);
                }
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                    mLoggedinOrderHistoryBtn.setImageResource(R.drawable.create_account);
                } else {
                    mLoggedinOrderHistoryBtn.setBackgroundResource(R.drawable.create_account);
                }

                mLoggedinOrderHistoryBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent goForRegistration = new Intent(
                                OrderSummaryActivity.this,
                                RegisterDetailsActivity.class);
                        goForRegistration.putExtra("revieworder",
                                checkoutShippmentMethodBean);
                        startActivity(goForRegistration);
                    }
                });
            } else {
                mLoggedinOrderHistoryBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intentForOrderHistory = new Intent(
                                OrderSummaryActivity.this,
                                MyOrderHistoryActivity.class);
                        startActivity(intentForOrderHistory);
                    }
                });
            }
        }


        mLoggedinContinueShoppingBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent continueShopping = new Intent(OrderSummaryActivity.this,
                        ShopListActivity.class);
                startActivity(continueShopping);
            }
        });

        createProductsForOmniture();
        creatingPageName();
        if (null != reviewOrderBean) {
            setOrderDeatails(reviewOrderBean);
        }

        //GMOB-3500 Conversant tag
        String orderTotal = String.format(
                "%.2f", Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTotalNew()));

        ConversantUtility.orderConfirmation(checkoutShippmentMethodBean.getComponent()
                .getCart().getOrderDetails().getId(), getSkuDetail(), orderTotal);

        //Show checkout message
        if (null != checkoutShippmentMethodBean
                && null != checkoutShippmentMethodBean.getComponent()) {
            if (null != checkoutShippmentMethodBean.getComponent().getCart()
                    .getOrderDetails().getOrderMessages()&&!checkoutShippmentMethodBean.getComponent().getCart()
                    .getOrderDetails().getOrderMessages().isEmpty()) {
                List<String> orderMessages=checkoutShippmentMethodBean.getComponent().getCart()
                        .getOrderDetails().getOrderMessages();
                for(int i=0;i<orderMessages.size();i++)
                {
                    mCreditMessage.append("\n"+orderMessages.get(i).toString());
                }
            }

            }

    }


    //Get list of sku in order sku;qty;amount|sku;qty;amount
    public StringBuffer getSkuDetail() {
        List<CheckoutCommerceItemBean> commerceItems;
        productsInfo.append("");
        if (null != reviewOrderBean) {
            if (null != reviewOrderBean.getComponent()) {
                if (null != reviewOrderBean.getComponent()
                        .getPaymentDetails()) {
                    if (null != reviewOrderBean.getComponent()
                            .getPaymentDetails().getCommerceItems()) {
                        commerceItems = reviewOrderBean.getComponent()
                                .getPaymentDetails().getCommerceItems();
                        for (int i = 0; i < commerceItems.size(); i++) {

                            productsInfo.append(commerceItems.get(i)
                                    .getCatalogRefId());
                            productsInfo.append(";");
                           /* sku quantity
                            productsInfo.append(commerceItems.get(i)
                                    .getQuantity());
                            productsInfo.append(";");*/
                            productsInfo.append(String.format(
                                    "%.2f", Double.valueOf(commerceItems.get(i)
                                            .getAmount())));
                            if (i != commerceItems.size() - 1) //avoid adding "|" at last product
                                productsInfo.append("|");

                        }


                    }
                }
            }
        }
        return productsInfo;
    }

    private void setOrderDeatails(ReviewOrderBean reviewOrderBean) {
        if (null != reviewOrderBean && null != reviewOrderBean.getComponent() && null != reviewOrderBean.getComponent().getPaymentDetails()
                ) {

            if (null != reviewOrderBean.getComponent().getPaymentDetails().getOrderDetails()) {
                //Subtotal details
                tvorderSubtotal.setText("$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails().getOrderDetails()
                                .getRawSubtotal())));
                orderSubtotalLayout.setVisibility(View.VISIBLE);

                //Shipping Details
                tvShipping.setText("$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails().getOrderDetails()
                                .getShipping())));
                if (Double.valueOf(reviewOrderBean.getComponent()
                        .getPaymentDetails().getOrderDetails()
                        .getShipping()).equals(0.0)) {
                    tvShipping.setText("FREE");
                }
                shippingLayout.setVisibility(View.VISIBLE);

                //tax  Details
                tvTax.setText("$"
                        + String.format("%.2f", Double.valueOf(reviewOrderBean
                        .getComponent().getPaymentDetails().getOrderDetails()
                        .getTax())));
                taxLayout.setVisibility(View.VISIBLE);

                //tiered price: Additional discount

                if (null != reviewOrderBean.getComponent()
                        .getPaymentDetails().getOrderDetails().getTieredDiscountAmount() && !reviewOrderBean.getComponent()
                        .getPaymentDetails().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
                    tvAdditionalDiscount.setText("-$"
                            + String.format("%.2f",
                            Double.valueOf(reviewOrderBean.getComponent()
                                    .getPaymentDetails().getOrderDetails().getTieredDiscountAmount())));
                    additionalDiscountLayout.setVisibility(View.VISIBLE);
                }

            }

            //Coupon Details
            displayCouponApplied(reviewOrderBean);

            //Loyalty details
            double amountAdjusted = 0;
            if (null != reviewOrderBean.getComponent().getPaymentDetails()
                    .getLoyaltyPointsPaymentGroups()
                    && !reviewOrderBean.getComponent().getPaymentDetails()
                    .getLoyaltyPointsPaymentGroups().isEmpty()) {
                if (reviewOrderBean.getComponent().getPaymentDetails()
                        .getLoyaltyPointsPaymentGroups().size() != 0) {
                    for (int i = 0; i < reviewOrderBean.getComponent()
                            .getPaymentDetails().getLoyaltyPointsPaymentGroups()
                            .size(); i++) {
                        CheckoutLoyaltyPointsPaymentGroupBean checkOutLoyality = reviewOrderBean.getComponent()
                                .getPaymentDetails()
                                .getLoyaltyPointsPaymentGroups().get(i);
                        amountAdjusted += Double.valueOf(checkOutLoyality
                                .getAmount());
                    }
                }
            }
            if (amountAdjusted != 0) {
                loyaltyDiscountLayout.setVisibility(View.VISIBLE);
                tvLoyaltyDiscount.setText("-$"
                        + String.format("%.2f", amountAdjusted));

            }

            //Total
            if (amountAdjusted != 0 && null != reviewOrderBean.getComponent()
                    .getPaymentDetails().getOrderDetails()) {
                tvTotal.setText("$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails().getOrderDetails()
                                .getTotal())
                                - amountAdjusted));
            } else {
                tvTotal.setText("$"
                        + String.format(
                        "%.2f",
                        Double.valueOf(reviewOrderBean.getComponent()
                                .getPaymentDetails().getOrderDetails()
                                .getTotal())));
            }
            totalLayout.setVisibility(View.VISIBLE);


        }
    }

    private void displayCouponApplied(ReviewOrderBean reviewOrderBean) {
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
                                    couponDiscountLayout.setVisibility(View.GONE);
                                } else {
                                    tvCouponDiscount.setText("-$"
                                            + String.format(
                                            "%.2f",
                                            Double.valueOf(reviewOrderBean.getComponent()
                                                    .getPaymentDetails()
                                                    .getCouponDiscount().getTotalAdjustment())));
                                    couponDiscountLayout.setVisibility(View.VISIBLE);
                                }

                            }

//                            if (null != reviewOrderBean.getComponent()
//                                    .getPaymentDetails().getCouponDiscount()
//                                    .isShippingFree()) {
//                                couponDiscountLayout.setVisibility(View.VISIBLE);
//                                // if isShippingFree is true then shipping
//                                // coupon with shipping free offer is applied
//                                if (reviewOrderBean.getComponent()
//                                        .getPaymentDetails()
//                                        .getCouponDiscount().isShippingFree()
//                                        .equalsIgnoreCase("true")) {
//                                    couponDiscountLayout.setVisibility(View.GONE);
//
//                                }
//                                // if isShippingFree is false then shipping
//                                // coupon with percent off offer is applied
//                                else {
//
//                                    tvCouponDiscount
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
//                                    couponDiscountLayout
//                                            .setVisibility(View.VISIBLE);
//                                    tvCouponDiscount
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
                                        couponDiscountLayout
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

                                        tvCouponDiscount.setText(couponAmount);
                                    }

                                }

                            }
                        }

                    }
                }
            }

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    public void createProductsForOmniture() {
        List<CheckoutCommerceItemBean> commerceItems;

        products.append(";");
        finalEvents.append("purchase,event69,event60");

        if (UltaDataCache.getDataCacheInstance().isFreeSampleSelected()) {
            finalEvents.append(",event43");
        }

        try {
            if (null != reviewOrderBean) {
                if (null != reviewOrderBean.getComponent()) {
                    if (null != reviewOrderBean.getComponent()
                            .getPaymentDetails()) {
                        if (null != reviewOrderBean.getComponent()
                                .getPaymentDetails().getCommerceItems()) {
                            commerceItems = reviewOrderBean.getComponent()
                                    .getPaymentDetails().getCommerceItems();
                            for (int i = 0; i < commerceItems.size(); i++) {
                                products.append("");
                                products.append(commerceItems.get(i)
                                        .getCatalogRefId());
                                products.append(";");
                                products.append(commerceItems.get(i)
                                        .getQuantity());
                                products.append(";");
                                products.append(commerceItems.get(i)
                                        .getAmount());
                                products.append(",;");

                            }
                            if (null != UltaDataCache.getDataCacheInstance()
                                    .getCouponCode()) {
                                products.append(",;;;;event11=");
                                finalEvents.append(",event11");

                                if (null != reviewOrderBean.getComponent()
                                        .getPaymentDetails()
                                        .getCouponDiscount()) {
                                    if (null != reviewOrderBean.getComponent()
                                            .getPaymentDetails()
                                            .getCouponDiscount()
                                            .getTotalAdjustment()) {
                                        products.append(reviewOrderBean
                                                .getComponent()
                                                .getPaymentDetails()
                                                .getCouponDiscount()
                                                .getTotalAdjustment());
                                    } else {
                                        if (0.00 != UltaDataCache
                                                .getDataCacheInstance()
                                                .getCouponAmount()) {
                                            products.append(UltaDataCache
                                                    .getDataCacheInstance()
                                                    .getCouponAmount());
                                        } else {
                                            products.append("0.00");
                                        }
                                    }
                                } else {
                                    if (0.00 != UltaDataCache
                                            .getDataCacheInstance()
                                            .getCouponAmount()) {
                                        products.append(UltaDataCache
                                                .getDataCacheInstance()
                                                .getCouponAmount());
                                    } else {
                                        products.append("0.00");
                                    }
                                }

                                products.append(";eVar11=");
                                products.append(UltaDataCache
                                        .getDataCacheInstance().getCouponCode());
                            }

                            if (null != checkoutShippmentMethodBean
                                    .getComponent().getOrderPoints()) {
                                products.append(",;Loyalty points");
                                products.append(";;;event38=");
                                finalEvents.append(",event38");
                                products.append(checkoutShippmentMethodBean
                                        .getComponent().getOrderPoints());

                            }

                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails().getOrderDetails()) {
                                products.append(",;Shipping;;;event9=");
                                finalEvents.append(",event9");
                                products.append(reviewOrderBean.getComponent()
                                        .getPaymentDetails().getOrderDetails()
                                        .getShipping());
                            }
                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails().getOrderDetails()) {
                                products.append(",;Tax;;;event10=");
                                finalEvents.append(",event10");
                                products.append(reviewOrderBean.getComponent()
                                        .getPaymentDetails().getOrderDetails()
                                        .getTax());
                            }
                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails().getOrderDetails()) {
                                products.append(",;Gift Wrap;;;event23=");
                                finalEvents.append(",event23");
                                if (reviewOrderBean.getComponent()
                                        .getPaymentDetails().getOrderDetails()
                                        .isContainsGiftWrap()) {
                                    products.append("3.99");
                                } else {
                                    products.append("");
                                }
                            }


                            if (null != reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getGiftCardPaymentGroups()
                                    && reviewOrderBean.getComponent()
                                    .getPaymentDetails()
                                    .getGiftCardPaymentGroups().size() != 0) {
                                if (null != reviewOrderBean.getComponent()
                                        .getPaymentDetails()
                                        .getGiftCardPaymentGroups().get(0)) {
                                    products.append(",;GiftCard;;;event35=");
                                    finalEvents.append(",event35");
                                    products.append(reviewOrderBean
                                            .getComponent().getPaymentDetails()
                                            .getGiftCardPaymentGroups().get(0)
                                            .getAmount());
                                }
                            }

                        }

                    }
                }
                UltaDataCache.getDataCacheInstance().setProducts(
                        products.toString());
                if (null != finalEvents) {
                    UltaDataCache.getDataCacheInstance().setFinalEvents(
                            finalEvents.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void creatingPageName() {
        String categoryname = "";
        if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
            categoryname = WebserviceConstants.ORDERCONFIRMATION_GUEST_PAGE;
        } else if (Utility.retrieveBooleanFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
                getApplicationContext())) {
            categoryname = WebserviceConstants.ORDERCONFIRMATION_LOYALITY_PAGE;
        } else {
            categoryname = WebserviceConstants.ORDERCONFIRMATION_NON_LOYALITY_PAGE;
        }
        trackAppState(this, categoryname);
        OmnitureTracking.startActivity(this);

        Map<String, Object> omnitureData = new HashMap<String, Object>();
        omnitureData.put("&&products", UltaDataCache.getDataCacheInstance()
                .getProducts());
        omnitureData.put("&&events", UltaDataCache.getDataCacheInstance()
                .getFinalEvents());
        omnitureData.put(WebserviceConstants.COUPON_CODE_KEY, UltaDataCache
                .getDataCacheInstance().getCouponCode());
        OmnitureTracking.setEvars(WebserviceConstants.ORDERS_ACTION,
                omnitureData);
        OmnitureTracking.stopActivity();

    }

    @Override
    public void onBackPressed() {
        Intent backToBasket = new Intent(OrderSummaryActivity.this,
                ViewItemsInBasketActivity.class);
        backToBasket.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backToBasket);

    }

}
