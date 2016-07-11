/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.checkout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.UltaBean;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.checkout.CheckoutCartBean;
import com.ulta.core.bean.checkout.CheckoutGiftCardPaymentGroupBean;
import com.ulta.core.bean.checkout.CheckoutOrderAdjustmentBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
import com.ulta.core.bean.checkout.ReviewOrderBean;
import com.ulta.core.bean.product.AddToCartBean;
import com.ulta.core.bean.product.OrderAdjustmentBean;
import com.ulta.core.bean.product.RedeemLevelPointsBean;
import com.ulta.core.bean.product.RedeemPointBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.fragments.checkout.PaymentCheckBoxFragment;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class PaymentMethodActivity.
 */
@SuppressWarnings("unused")
public class PaymentMethodActivity extends UltaBaseActivity implements
        OnDoneClickedListener, OnClickListener, OnSessionTimeOut, TextWatcher {

    /**
     * The form layout.
     */
    LinearLayout formLayout;// whole layout

    /**
     * The loading layout.
     */
    FrameLayout loadingLayout;// progress showing layout
    InputMethodManager inputManager;
    /**
     * menu options layout for credit card,gift card,redeem,paypal
     */
    LinearLayout mEnterGiftCard, mPaypalLayout, mCreditCardLayout,
            mRedeemPointsLayout;

    /**
     * Counter value to check whether the layout is visible or not
     */
    int creditCounter = 0, giftCounter = 0, redeemsCounter = 0;

    /**
     * Credit Card
     */
    LinearLayout mCredit_card_details_layout;// layout for both saved and new
    // credit card
    LinearLayout mNew_credit_card_details;// layout for new credit card
    LinearLayout creditFragment;// layout containing populated saved credit card
    // details
    EditText edtSecurityCode;// security code for saved credit card details
    PaymentCheckBoxFragment addressFragment;// fragment in which saved credit card
    // details are populated
    private List<PaymentDetailsBean> creditCardsList;// List which contains
    // saved credit card
    // details
    boolean hasCreditCardSaved = false;// check whether any saved address is
    // present
    // private UemAction
    // fetchCreditCardsAction;// UemAction
    // for fetching saved
    // credit card
    EditText edtCardHolderName, edtCardSecurityCode, edtCardNumber,
            edtIssueNumber;// edit text for new credit card
    /**
     * The spinner expiry year,type and month for credit card.
     */
    Spinner spinnerExpiryMonth, spinnerExpiryYear;

    String cardType = "Select Card Type", cardHolderName, cardCreditNumber,
            issueDate, securityCode, issueNumber,
            cardExpiryMonth = "Select Expiry Month",
            cardExpiryYear = "Select Expiry Year";
    int cardTypeLocation = 0, expiryMonthLocation = 0, expiryYearLocation = 0;// position
    // of
    // selected
    // value
    // in
    // spinners
    private String[] anArrayOfStrings1;// for month spinner
    private String[] anArrayOfStrings2;// for year spinner
    private ArrayList<String> result1, result2;// array containing months and
    // year
    RelativeLayout mPay_with_new_card_layout;// layout containing pay with new
    // credit card switch
    private Switch mPay_with_new_card_switch;// pay with new credit card switch
    // if saved one exists

    // private UemAction verifyCrediCardAction;// Uem Action for credit card
    /**
     * Redeem Points
     */
    ArrayList<RedeemPointBean> redeemLevelPoints = new ArrayList<RedeemPointBean>();// bean
    // containing
    // the
    // array
    // of
    // redeem
    // points
    String redeemPointvalue;// selected redeem value
    private TextView mTotalRedeemPoints;// showing total redeem points
    private TextView mNo_redeempoints;// If no redeem points available
    private TextView mChangeRedeemTv;// contains selected redeem point
    LinearLayout mRedeemPointsDetailsLayout;// layout on click show dialog of
    // redeem points
    int checkedId = -1;// checked position of redeem point
    /**
     * Gift Card
     */
    EditText edtGiftCardNumber, edtGiftCardPin;// edit text for gift card number
    // and pin
    private boolean mGiftcardEntered = false;// check whether gift card entered
    // or not
    private String giftCardNumber, giftCardPin;// entered gift card number and
    // pin
    LinearLayout mGiftCardExpandedLayout;// layout for entering gift card
    // details
    // private UemAction
    // giftCardCheckoutAction;//
    // UemAction for applying gift card

    /**
     * Payment
     */
    ProgressDialog pd;// progress dialog
    boolean giftCardEntry = false, creditCardEntry = false,
            redeemEntry = false;// check if gift card,credit card,redeem is
    // entered or not
    PaymentMethodBean paymentMethodBean;// Payment method bean

    ImageButton mReviewOrderLayout;// Layout for navigating to review order page
    ImageButton mGiftBoxDetails;
    TextView mCheckout_payment, mCheckout_shipping, mCheckout_review_order;// header
    // tab
    boolean isGiftCardInsufficient = false;
    /**
     * The hash map.
     */
    HashMap<String, String> hashMap = new HashMap<String, String>();

    /**
     * The default credit card id.
     */
    private String defaultCreditCardId;
    private String addCard = "false";
    private int id;
    boolean onPause = false;
    boolean edit = false;
    RadioGroup rdGroup;
    TextView txt;
    /**
     * The payment method map.
     */
    HashMap<String, String> paymentMethodMap = new HashMap<String, String>();
    HashMap<String, String> paymentMethod = new HashMap<String, String>();
    private String billingPhone = "000-000-0000";
    /**
     * The formdata.
     */
    HashMap<String, String> formdata = new HashMap<String, String>();
    boolean isEnteringNewCard = false;

    private LinearLayout mlinearSecurityCode;
    private CheckoutShippmentMethodBean mCheckoutShipmentBean;
    private AddToCartBean mAddtoCartBean;
    private UltaBean mUltaBean;
    private double mGiftPrice = 0.00;
    private boolean isGiftEnabled = false;
    private boolean isFromPaypal = false;
    private String mPaymentType;

    private boolean mInvokePaypalService;
    private boolean mInvokePreffredPaymentMethodDetails;
    private boolean mInvokeApplyGiftCard;
    private boolean mInvokePaymentService;

    private int MY_SCAN_REQUEST_CODE = 100;
    ArrayList<HashMap<String, Object>> list;

    /**
     * The edt promo code.
     */

    // Save Guest user details
    private SharedPreferences guestUserSharedPreferences;
    private Editor guestUserPreferenceEditor;

    private TextView savedCardSecurityErrorText, nameOnCardErrorText,
            numberOnCardErrorText, expiryMonthErrorText,
            expiryYearErrorText, creditSecurityErrorText,
            giftcardNumberErrorText, giftCardPinErrorText;
    private Drawable originalDrawable;


    private Boolean isCVVRequired = true, isExpirationDateRequired = true;
    private LinearLayout mExpiryLayout, msecurityCodeLayout;
    private CreditCardInfoBean identifiedUserEnteredCardDetails = null;
    private LinearLayout mAcceptedCardTypesLayout, mCardImageLayout;

    @Override
    protected void onResume() {
        super.onResume();
        if (onPause) {
            loadingLayout.setVisibility(View.GONE);
            formLayout.setVisibility(View.VISIBLE);
        }
        if (!UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {
            // mGiftBoxDetails
            // .setBackgroundResource(R.drawable.checkout_shippingmethod_back_enabled);
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mGiftBoxDetails.setImageDrawable(getResources().getDrawable(
                        R.drawable.checkout_shippingmethod_back_enabled));
            } else {
                mGiftBoxDetails
                        .setBackgroundResource(R.drawable.checkout_shippingmethod_back_enabled);
            }

        }
        trackAppAction(PaymentMethodActivity.this,
                WebserviceConstants.CHECKOUT_STEP_5_EVENT_ACTION);

        trackAppAction(PaymentMethodActivity.this,
                WebserviceConstants.CHECKOUT_STEP_5_VISIT_EVENT_ACTION);
        // getRedeemPoints();
        if (null != UltaDataCache.getDataCacheInstance().getRedeemLevelPoints()) {
            redeemLevelPoints = UltaDataCache.getDataCacheInstance()
                    .getRedeemLevelPoints();
            setRedeemPoints(redeemLevelPoints);

        } else {
            invokeApplyGiftOption();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        onPause = true;
        mPaymentType = "";
        edtGiftCardPin.setText("");
        edtSecurityCode.setText("");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        setTitle("Payment Method");
        setActivity(PaymentMethodActivity.this);
        // Initializing progress dialog
        pd = new ProgressDialog(PaymentMethodActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage("Loading...");
        pd.setCancelable(false);

        if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
            trackAppState(this, WebserviceConstants.PAYMENT_GUEST_PAGE);
        } else if (null != UltaDataCache.getDataCacheInstance()
                .getRewardsBalancePoints()) {
            trackAppState(this, WebserviceConstants.PAYMENT_LOYALITY_PAGE);
        } else {
            trackAppState(this, WebserviceConstants.PAYMENT_NON_LOYALITY_PAGE);
        }
        // setting the header to indicate payment page
        mCheckout_payment = (TextView) findViewById(R.id.checkout_payment);
        mCheckout_payment.setBackgroundColor(getResources().getColor(
                R.color.chekout_header_highlighted));
        mCheckout_review_order = (TextView) findViewById(R.id.checkout_review_order);
        mCheckout_shipping = (TextView) findViewById(R.id.checkout_shipping);
        Drawable drawable = getResources().getDrawable(
                R.drawable.tick_completed);
        mCheckout_shipping.setCompoundDrawablesWithIntrinsicBounds(drawable,
                null, null, null);
        mCheckout_shipping.setBackgroundColor(getResources().getColor(
                R.color.olapic_detail_caption));
        mCheckout_review_order.setBackgroundColor(getResources().getColor(
                R.color.olapic_detail_caption));
        formdata = UltaDataCache.getDataCacheInstance().getShippingAddress();
        if (formdata != null && null != formdata.get("phone")) {
            billingPhone = formdata.get("phone");
            if (billingPhone.length() == 10) {
                billingPhone = Utility.formatPhoneNumber(billingPhone);
            }
        }

        initFooterViews();
        initviews();
        // setFooterValues(checkoutShippmentMethodBean);
        // Getting redeem points if any
        getRedeemPoints();

        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().getSerializable(
                    "checkoutAddtoCartBean")) {
                mAddtoCartBean = (AddToCartBean) getIntent().getExtras()
                        .getSerializable("checkoutAddtoCartBean");
                setFooterValues(mAddtoCartBean);
            }
            if (null != getIntent().getExtras().getSerializable(
                    "checkoutshipmentBean")) {
                mCheckoutShipmentBean = (CheckoutShippmentMethodBean) getIntent()
                        .getExtras().getSerializable("checkoutshipmentBean");
                setFooterValues(mCheckoutShipmentBean);
            }
        }


        result1 = new ArrayList<String>();
        result1.add("Select Expiry Month");
        result1.add("January");
        result1.add("February");
        result1.add("March");
        result1.add("April");
        result1.add("May");
        result1.add("June");
        result1.add("July");
        result1.add("August");
        result1.add("September");
        result1.add("October");
        result1.add("November");
        result1.add("December");

        anArrayOfStrings1 = new String[result1.size()];
        result1.toArray(anArrayOfStrings1);
        MySpinnerAdapter<CharSequence> spinnerSrrayAdapter = new MySpinnerAdapter<CharSequence>(
                PaymentMethodActivity.this, anArrayOfStrings1);

        spinnerExpiryMonth.setAdapter(spinnerSrrayAdapter);

        spinnerExpiryMonth
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parentView,
                                               View view, int pos, long id) {
                        try {
                            ((TextView) parentView.getChildAt(0))
                                    .setTextColor(getResources().getColor(
                                            R.color.black));
                            ((TextView) parentView.getChildAt(0)).setTextSize(12);
                            ((TextView) parentView.getChildAt(0)).setPadding(5, 0,
                                    0, 0);
                            expiryMonthErrorText.setVisibility(View.GONE);
                            if (pos != 0) {
                                /* isSpinnerSelectedMonth = true; */
                                expiryMonthLocation = pos;
                                if (Integer.valueOf(pos) < 10) {
                                    cardExpiryMonth = "0"
                                            + Integer.valueOf(pos).toString();
                                } else
                                    cardExpiryMonth = Integer.valueOf(pos)
                                            .toString();
                            } else {
                                expiryMonthLocation = pos;
                                cardExpiryMonth = "Select Expiry Month";
                            }
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }

                        Logger.Log(">>>Expiry Month :" + expiryMonthLocation);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(PaymentMethodActivity.this, "nothing",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        result2 = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        String monthName = new SimpleDateFormat("MMM").format(calendar
                .getTime());
        result2.add("Select Expiry Year");
        for (int i = year; i <= year + 12; i++) {
            result2.add("" + i);
        }
        anArrayOfStrings2 = new String[result2.size()];
        result2.toArray(anArrayOfStrings2);
        MySpinnerAdapter1<CharSequence> spinnerSrrayAdapter1 = new MySpinnerAdapter1<CharSequence>(
                PaymentMethodActivity.this, anArrayOfStrings2);

        spinnerExpiryYear.setAdapter(spinnerSrrayAdapter1);

        spinnerExpiryYear
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parentView,
                                               View view, int pos, long id) {
                        try {
                            ((TextView) parentView.getChildAt(0))
                                    .setTextColor(getResources().getColor(
                                            R.color.black));
                            ((TextView) parentView.getChildAt(0)).setTextSize(12);
                            ((TextView) parentView.getChildAt(0)).setPadding(5, 0,
                                    0, 0);
                            expiryYearErrorText.setVisibility(View.GONE);
                            if (pos != 0) {
                                /* isSpinnerSelectedYear = true; */
                                cardExpiryYear = parentView.getItemAtPosition(pos)
                                        .toString();
                                expiryYearLocation = pos;
                            } else {
                                cardExpiryYear = "Select Expiry Year";
                                expiryYearLocation = pos;
                            }

                            Logger.Log(">>>Expiry Year :" + expiryYearLocation);
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        if (!UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {

            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mGiftBoxDetails.setImageDrawable(getResources().getDrawable(
                        R.drawable.checkout_shippingmethod_back_enabled));
            } else {
                mGiftBoxDetails
                        .setBackgroundResource(R.drawable.checkout_shippingmethod_back_enabled);
            }

        }

        spinnerExpiryMonth.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        spinnerExpiryYear.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });
        creatingPageName();
        invokePreffredPaymentMethodDetails();
    }

    /**
     * Get redeem points if available for the user and set in dialog for redeem
     * change click
     */
    @SuppressWarnings("unchecked")
    public void getRedeemPoints() {
        if (getIntent().getExtras() != null
                && !getIntent().getExtras().isEmpty()) {
            if (getIntent().getExtras().get("OrderForRedeemPoint") != null) {
                UltaDataCache.getDataCacheInstance().setRedeemPoints(true);
                redeemLevelPoints = (ArrayList<RedeemPointBean>) getIntent()
                        .getExtras().getSerializable("OrderForRedeemPoint");
                UltaDataCache.getDataCacheInstance().setRedeemLevelPoints(
                        redeemLevelPoints);
                mTotalRedeemPoints.setText("You have "
                        + redeemLevelPoints.get(redeemLevelPoints.size() - 1)
                        .getPoints() + " points");
            }

        }

        if (redeemLevelPoints != null && redeemLevelPoints.size() != 0) {
            redeemEntry = true;
            LinearLayout lytRedeemPoint = (LinearLayout) findViewById(R.id.redeemLayout);
            lytRedeemPoint.setVisibility(View.VISIBLE);

            lytRedeemPoint.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            PaymentMethodActivity.this);
                    alert.setTitle("Redeem Points");
                    ScrollView scrView = new ScrollView(
                            PaymentMethodActivity.this);
                    LinearLayout lytScr = new LinearLayout(
                            PaymentMethodActivity.this);
                    scrView.addView(lytScr);
                    rdGroup = new RadioGroup(PaymentMethodActivity.this);
                    for (int i = 0; i < redeemLevelPoints.size(); i++) {
                        RedeemPointBean redeemPoinsBean = new RedeemPointBean();
                        redeemPoinsBean = redeemLevelPoints.get(i);
                        RadioButton rbButton = new RadioButton(
                                PaymentMethodActivity.this);
                        rbButton.setText("\t" + redeemPoinsBean.getPoints()
                                + ".0 \t points =  $"
                                + redeemPoinsBean.getValue());
                        rbButton.setId(i);
                        rdGroup.addView(rbButton);

                    }

                    if (checkedId != -1) {
                        rdGroup.check(checkedId);
                    }

                    lytScr.addView(rdGroup);
                    alert.setView(scrView);
                    alert.setPositiveButton("Reset",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (redeemPointvalue != null)
                                        paymentMethodMap
                                                .remove("redeemPointValue");
                                    redeemPointvalue = null;
                                    checkedId = -1;
                                    mChangeRedeemTv.setText("0 points($0)");
                                    dialog.dismiss();
                                }
                            });
                    alert.setNegativeButton("Done",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    checkedId = rdGroup
                                            .getCheckedRadioButtonId();
                                    if (checkedId != -1) {
                                        RedeemPointBean re = new RedeemPointBean();
                                        re = redeemLevelPoints.get(checkedId);
                                        redeemPointvalue = re.getValue();

                                        if (redeemPointvalue != null)
                                            paymentMethodMap.put(
                                                    "redeemPointValue",
                                                    redeemPointvalue);
                                        mChangeRedeemTv.setText(re.getPoints()
                                                + "points($" + redeemPointvalue
                                                + ")");

                                    }
                                }
                            });
                    alert.create().show();
                }
            });

        }
    }

    private void setRedeemPoints(
            final ArrayList<RedeemPointBean> redeemLevelPoints) {
        if (redeemLevelPoints != null && redeemLevelPoints.size() != 0) {
            redeemEntry = true;
            LinearLayout lytRedeemPoint = (LinearLayout) findViewById(R.id.redeemLayout);
            lytRedeemPoint.setVisibility(View.VISIBLE);
            mTotalRedeemPoints.setText("You have "
                    + redeemLevelPoints.get(redeemLevelPoints.size() - 1)
                    .getPoints() + " points");
            lytRedeemPoint.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(
                            PaymentMethodActivity.this);
                    alert.setTitle("Redeem Points");
                    ScrollView scrView = new ScrollView(
                            PaymentMethodActivity.this);
                    LinearLayout lytScr = new LinearLayout(
                            PaymentMethodActivity.this);
                    scrView.addView(lytScr);
                    rdGroup = new RadioGroup(PaymentMethodActivity.this);
                    for (int i = 0; i < redeemLevelPoints.size(); i++) {
                        RedeemPointBean redeemPoinsBean = new RedeemPointBean();
                        redeemPoinsBean = redeemLevelPoints.get(i);
                        RadioButton rbButton = new RadioButton(
                                PaymentMethodActivity.this);
                        rbButton.setText("\t" + redeemPoinsBean.getPoints()
                                + ".0 \t points =  $"
                                + String.format(
                                "%.2f",
                                Double.valueOf(Double.parseDouble(redeemPoinsBean.getValue()))));
                        rbButton.setId(i);
                        rdGroup.addView(rbButton);


                    }

                    if (checkedId != -1) {
                        rdGroup.check(checkedId);
                    }

                    lytScr.addView(rdGroup);
                    alert.setView(scrView);
                    alert.setPositiveButton("Reset",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (redeemPointvalue != null)
                                        paymentMethodMap
                                                .remove("redeemPointValue");
                                    redeemPointvalue = null;
                                    checkedId = -1;
                                    mChangeRedeemTv.setText("0 points($0)");
                                    dialog.dismiss();
                                }
                            });
                    alert.setNegativeButton("Done",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    createPaymentTypeString("Redeem Points");
                                    checkedId = rdGroup
                                            .getCheckedRadioButtonId();
                                    if (checkedId != -1) {
                                        RedeemPointBean re = new RedeemPointBean();
                                        re = redeemLevelPoints.get(checkedId);
                                        redeemPointvalue = re.getValue();

                                        if (redeemPointvalue != null)
                                            paymentMethodMap.put(
                                                    "redeemPointValue",
                                                    redeemPointvalue);
                                        mChangeRedeemTv.setText(re.getPoints()
                                                + "points($" + redeemPointvalue
                                                + ")");

                                    }
                                }
                            });
                    alert.create().show();
                }
            });

        }
    }

    private void addAcceptedCardImages() {
        mCardImageLayout.removeAllViews();
        CreditCardInfoBean singleCardInfo = null;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo && null != singleCardInfo.getCardImage()) {
                        ImageView cardImage = new ImageView(PaymentMethodActivity.this);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                FrameLayout.LayoutParams.WRAP_CONTENT);
                        cardImage.setLayoutParams(params);
                        cardImage.setPadding(0, 0, 20, 0);
                        new AQuery(cardImage).image(singleCardInfo.getCardImage(), true, false, 200,
                                R.drawable.creditcard_default, null, AQuery.FADE_IN);
                        mCardImageLayout.addView(cardImage);

                    }
                }
            }
        }
    }

    private void initviews() {

        //Payment new card

        mExpiryLayout = (LinearLayout) findViewById(R.id.ExpiryLayout);
        mAcceptedCardTypesLayout = (LinearLayout) findViewById(R.id.acceptedCardTypesLayout);
        mCardImageLayout = (LinearLayout) findViewById(R.id.cardImageLayout);
        addAcceptedCardImages();
        msecurityCodeLayout = (LinearLayout) findViewById(R.id.securityCodeLayout);

        guestUserSharedPreferences = getSharedPreferences(
                WebserviceConstants.GUEST_USER_SHAREDPREF, MODE_PRIVATE);
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        mReviewOrderLayout = (ImageButton) findViewById(R.id.reviewOrderLayout);
        mReviewOrderLayout.setOnClickListener(this);

        mGiftBoxDetails = (ImageButton) findViewById(R.id.giftBoxDetails);
        mGiftBoxDetails.setOnClickListener(this);
        // gift card

        edtGiftCardNumber = (EditText) findViewById(R.id.giftCardNumber);
        edtGiftCardPin = (EditText) findViewById(R.id.giftCardPin);

        loadingLayout = (FrameLayout) findViewById(R.id.loadingDialog);
        formLayout = (LinearLayout) findViewById(R.id.linearLayout1);
        mPaypalLayout = (LinearLayout) findViewById(R.id.paypalLayout);
        mPaypalLayout.setOnClickListener(this);
        mGiftCardExpandedLayout = (LinearLayout) findViewById(R.id.giftCardExpandedLayout);

        // Gift card menu
        mEnterGiftCard = (LinearLayout) findViewById(R.id.giftCardLayout);
        mEnterGiftCard.setOnClickListener(this);

        mCreditCardLayout = (LinearLayout) findViewById(R.id.creditCardLayout);
        mCreditCardLayout.setOnClickListener(this);
        mCredit_card_details_layout = (LinearLayout) findViewById(R.id.credit_card_details_layout);
        creditFragment = (LinearLayout) findViewById(R.id.linearCreditFragment);
        mRedeemPointsLayout = (LinearLayout) findViewById(R.id.redeemPointsLayout);
        mRedeemPointsLayout.setOnClickListener(this);
        if (Utility.retrieveBooleanFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
                getApplicationContext())) {
            mRedeemPointsLayout.setVisibility(View.VISIBLE);
        } else {
            mRedeemPointsLayout.setVisibility(View.GONE);
        }
        mRedeemPointsDetailsLayout = (LinearLayout) findViewById(R.id.redeemPointsDetailsLayout);
        mTotalRedeemPoints = (TextView) findViewById(R.id.totalRedeemPoints);
        mNo_redeempoints = (TextView) findViewById(R.id.no_redeempoints);
        mChangeRedeemTv = (TextView) findViewById(R.id.changeRedeemTv);
        addressFragment = (PaymentCheckBoxFragment) getSupportFragmentManager()
                .findFragmentById(R.id.creditCardFragment);
        edtSecurityCode = (EditText) findViewById(R.id.edtSecurityCode);
        mNew_credit_card_details = (LinearLayout) findViewById(R.id.linearLayout14);
        mPay_with_new_card_layout = (RelativeLayout) findViewById(R.id.pay_with_new_card_layout);

        mlinearSecurityCode = (LinearLayout) findViewById(R.id.linearSecurityCode);
        // Add new card switch/toggle
        mPay_with_new_card_switch = (Switch) findViewById(R.id.pay_with_new_card_switch);
        mPay_with_new_card_switch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean arg1) {
                        if (arg0.isChecked()) {
                            isEnteringNewCard = true;
                            mNew_credit_card_details
                                    .setVisibility(View.VISIBLE);
                            creditFragment.setVisibility(View.GONE);

                        } else {
                            isEnteringNewCard = false;
                            edtCardHolderName.setText("");
                            edtCardNumber.setText("");
                            edtCardSecurityCode.setText("");
                            MySpinnerAdapter<CharSequence> spinnerSrrayAdapter = new MySpinnerAdapter<CharSequence>(
                                    PaymentMethodActivity.this,
                                    anArrayOfStrings1);
                            spinnerExpiryMonth.setAdapter(spinnerSrrayAdapter);
                            MySpinnerAdapter1<CharSequence> spinnerSrrayAdapter1 = new MySpinnerAdapter1<CharSequence>(
                                    PaymentMethodActivity.this,
                                    anArrayOfStrings2);
                            spinnerExpiryYear.setAdapter(spinnerSrrayAdapter1);
                            mNew_credit_card_details.setVisibility(View.GONE);
                            creditFragment.setVisibility(View.VISIBLE);
                        }
                    }
                });

        mExpandImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (toShowCouponCodeLayout % 2 != 0) {
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
                } else {
                    mSubTotalFooterLayout.setVisibility(View.GONE);
                    mShippingTypeFooterLayout.setVisibility(View.GONE);
                    mTaxFooterLayout.setVisibility(View.GONE);
//                    mGiftBoxAndNoteFooterLayout.setVisibility(View.GONE);
                    mCouponDiscountLayout.setVisibility(View.GONE);
                    mAdditionalDiscountLayout.setVisibility(View.GONE);
                    mExpandImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.plus));
                }
                toShowCouponCodeLayout++;
            }
        });

        edtCardHolderName = (EditText) findViewById(R.id.cardHolderName);
        edtCardNumber = (EditText) findViewById(R.id.NumberOfCard);
        edtCardSecurityCode = (EditText) findViewById(R.id.creditsecurityCode);
        spinnerExpiryMonth = (Spinner) findViewById(R.id.month);
        spinnerExpiryYear = (Spinner) findViewById(R.id.year);
        // Error Text View
        originalDrawable = edtCardHolderName.getBackground();
        savedCardSecurityErrorText = (TextView) findViewById(R.id.savedCardSecurityErrorText);
        nameOnCardErrorText = (TextView) findViewById(R.id.nameOnCardErrorText);
        numberOnCardErrorText = (TextView) findViewById(R.id.numberOnCardErrorText);
        expiryMonthErrorText = (TextView) findViewById(R.id.expiryMonthErrorText);
        expiryYearErrorText = (TextView) findViewById(R.id.expiryYearErrorText);
        creditSecurityErrorText = (TextView) findViewById(R.id.creditSecurityErrorText);
        giftcardNumberErrorText = (TextView) findViewById(R.id.giftcardNumberErrorText);
        giftCardPinErrorText = (TextView) findViewById(R.id.giftCardPinErrorText);

        edtSecurityCode.addTextChangedListener(this);
        edtCardHolderName.addTextChangedListener(this);
        edtCardNumber.addTextChangedListener(this);
        edtCardSecurityCode.addTextChangedListener(this);
        edtGiftCardNumber.addTextChangedListener(this);
        edtGiftCardPin.addTextChangedListener(this);

        list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("Name", "Select Card Type");
        map.put("Icon", "");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "VISA");
        map.put("Icon", R.drawable.visa);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "MASTER CARD");
        map.put("Icon", R.drawable.mastercard);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "DISCOVER");
        map.put("Icon", R.drawable.discover);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "AMERICAN EXPRESS");
        map.put("Icon", R.drawable.americanexpress);
        list.add(map);

        edtCardHolderName
                .setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView arg0, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideKeyboard();
                            edtCardHolderName.clearFocus();
                        }
                        return true;
                    }
                });

        edtCardNumber.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard();
                    edtCardNumber.clearFocus();
                    spinnerExpiryMonth.requestFocus();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            spinnerExpiryMonth.performClick();
                        }
                    }, 200);

                }
                return true;
            }
        });
    }

    public void hideSoftKeyboard(Activity context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null)
            inputManager.hideSoftInputFromWindow(context.getWindow()
                    .getDecorView().getApplicationWindowToken(), 0);
        context.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onDoneClicked() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // click of review order
            case R.id.reviewOrderLayout:
                onFormSubmit();
                break;
            // click on paypal
            case R.id.paypalLayout:
                trackEvarsUsingActionName(PaymentMethodActivity.this,
                        WebserviceConstants.CHECKOUT_STEP_5_EVENT_ACTION,
                        WebserviceConstants.PAYMENT_TYPE_KEY, "PayPal");
                pd = new ProgressDialog(PaymentMethodActivity.this);
                setProgressDialogLoadingColor(pd);
                // pd.setMessage(UltaConstants.LOADING_PROGRESS_TEXT);
                pd.setMessage("Loading PayPal...");
                pd.setCancelable(false);
                pd.show();
                UltaDataCache.getDataCacheInstance().setPayPalPayment(true);
                if (giftCardValidation()) {
                    // User entered the gift card
                    if (mGiftcardEntered) {
                        isFromPaypal = true;
                        pd.show();
                        invokeApplyGiftCard();
                    } else {
                        invokePaypalService();
                    }
                } else {
                    pd.dismiss();
                }

                break;
            // click on gift card
            case R.id.giftCardLayout:
                giftCounter++;
                if (giftCounter % 2 == 0) {
                    mGiftCardExpandedLayout.setVisibility(View.GONE);

                } else {
                    createPaymentTypeString("GiftCard");
                    mGiftCardExpandedLayout.setVisibility(View.VISIBLE);
                    mCredit_card_details_layout.setVisibility(View.GONE);
                    mRedeemPointsDetailsLayout.setVisibility(View.GONE);
                    creditCounter = 0;
                    redeemsCounter = 0;
                }

                break;
            // click on credit card
            case R.id.creditCardLayout:
                creditCounter++;

                if (creditCounter % 2 == 0) {
                    mCredit_card_details_layout.setVisibility(View.GONE);

                } else {

                    if (!isUltaCustomer(PaymentMethodActivity.this)) {
                        mlinearSecurityCode.setVisibility(View.GONE);
                    } else if (!hasCreditCardSaved) {
                        mlinearSecurityCode.setVisibility(View.GONE);
                    }

                    mCredit_card_details_layout.setVisibility(View.VISIBLE);
                    mGiftCardExpandedLayout.setVisibility(View.GONE);
                    mRedeemPointsDetailsLayout.setVisibility(View.GONE);
                    redeemsCounter = 0;
                    giftCounter = 0;

                    if (hasCreditCardSaved && !isEnteringNewCard) {
                        creditFragment.setVisibility(View.VISIBLE);
                        mPay_with_new_card_layout.setVisibility(View.VISIBLE);
                    } else if (hasCreditCardSaved && isEnteringNewCard) {
                        mPay_with_new_card_layout.setVisibility(View.VISIBLE);
                        mNew_credit_card_details.setVisibility(View.VISIBLE);
                    } else {
                        // JIRA 3286
                        isEnteringNewCard = true;
                        mNew_credit_card_details.setVisibility(View.VISIBLE);
                    }

                }

                break;
            // click on redeem
            case R.id.redeemPointsLayout:
                redeemsCounter++;
                if (redeemsCounter % 2 == 0) {
                    mRedeemPointsDetailsLayout.setVisibility(View.GONE);
                    mNo_redeempoints.setVisibility(View.GONE);
                } else {
                    mCredit_card_details_layout.setVisibility(View.GONE);
                    mGiftCardExpandedLayout.setVisibility(View.GONE);
                    giftCounter = 0;
                    creditCounter = 0;
                    if (redeemLevelPoints != null && redeemLevelPoints.size() != 0) {

                        mRedeemPointsDetailsLayout.setVisibility(View.VISIBLE);
                    } else {
                        mNo_redeempoints.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.giftBoxDetails:
                finish();
                break;

        }
    }

    public void getSavedCardDetails() {
        if (null != creditCardsList && creditCardsList.size() != 0) {
            int idCreditCard = addressFragment.getCheckedId() - 100;
            if (idCreditCard >= 0) {
                cardHolderName = creditCardsList.get(idCreditCard)
                        .getNameOnCard();
                cardCreditNumber = creditCardsList.get(idCreditCard)
                        .getCreditCardNumber();
                cardType = creditCardsList.get(idCreditCard)
                        .getCreditCardType();
                cardExpiryMonth = creditCardsList.get(idCreditCard)
                        .getExpirationMonth();
                cardExpiryYear = creditCardsList.get(idCreditCard)
                        .getExpirationYear();
                securityCode = edtSecurityCode.getText().toString().trim();
            }

        }
    }

    public void getNewCardDetails() {
        cardHolderName = edtCardHolderName.getText().toString().trim();
        cardCreditNumber = edtCardNumber.getText().toString().trim();
        if (cardCreditNumber.contains("*")) {
            if (null != UltaDataCache.getDataCacheInstance()
                    .getCreditCardDetails().get("cardnumber")) {
                cardCreditNumber = UltaDataCache.getDataCacheInstance()
                        .getCreditCardDetails().get("cardnumber");
            }
        }
        cardExpiryMonth = spinnerExpiryMonth.getSelectedItem().toString()
                .trim();
        if (cardExpiryMonth.equalsIgnoreCase("Select Expiry Month")) {

        } else {
            cardExpiryMonth = "" + spinnerExpiryMonth.getSelectedItemPosition();
            if (Integer.valueOf(cardExpiryMonth) < 10) {
                cardExpiryMonth = "0"
                        + Integer.valueOf(cardExpiryMonth).toString();
            } else
                cardExpiryMonth = Integer.valueOf(cardExpiryMonth).toString();
        }
        cardExpiryYear = spinnerExpiryYear.getSelectedItem().toString().trim();
        // cardType = spinnerUserCardType.getSelectedItem().toString().trim();
        securityCode = edtCardSecurityCode.getText().toString().trim();
    }

    /**
     * Submitting the details
     */
    public void onFormSubmit() {
        getAllValues();
        if (giftCardValidation()) {
            // User entered the gift card
            if (mGiftcardEntered) {

                pd.show();
                invokeApplyGiftCard();
            }
            // no gift card
            else {
                if (isEnteringNewCard) {
                    giftCardEntry = false;
                    creditCardEntry = true;
                    getNewCardDetails();
                    if (isCreditCardValidationSuccess(identifiedUserEnteredCardDetails)) {
                        invokePaymentService();
                    }
                } else if (null != creditCardsList
                        && creditCardsList.size() != 0
                        && (addressFragment.getCheckedId() - 100 >= 0)) {
                    giftCardEntry = false;
                    creditCardEntry = true;
                    getSavedCardDetails();
                    if (null != identifyCardType(cardCreditNumber)) {
                        if (identifyCardType(cardCreditNumber).getCardUsesCVV() && edtSecurityCode.getText().toString().trim().length() != 0) {
                            invokePaymentService();
                        } else if (!identifyCardType(cardCreditNumber).getCardUsesCVV()) {
                            int cardMaxlength = 0, cvvMaxLength = 0, cardMinLength = 0;
                            if (null != identifyCardType(cardCreditNumber).getCardMaxCVVLength()) {
                                try {
                                    cardMaxlength = Integer.parseInt(identifyCardType(cardCreditNumber).getCardMaxNumberLength());
                                    cardMinLength = Integer.parseInt(identifyCardType(cardCreditNumber).getCardMinNumberLength());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (cardCreditNumber.length() > cardMaxlength || cardCreditNumber.length() < cardMinLength) {
                                setError(edtCardNumber, numberOnCardErrorText,
                                        WebserviceConstants.INVALID_CREDIT_CARD);
                                edtCardNumber.requestFocus();
                            } else {
                                invokePaymentService();
                            }
                        } else {
                            setError(edtSecurityCode,
                                    savedCardSecurityErrorText,
                                    "Fill the Security Code");
                            makedLayoutVisible(mCredit_card_details_layout,
                                    mCreditCardLayout);
                        }
                    }

                } else if (UltaDataCache.getDataCacheInstance()
                        .isAnonymousCheckout()) {
                    giftCardEntry = false;
                    creditCardEntry = true;
                    if (cardHolderName.length() != 0
                            || cardCreditNumber.length() != 0
                            || securityCode.length() != 0) {
                        if (isCreditCardValidationSuccess(identifiedUserEnteredCardDetails)) {
                            invokePaymentService();
                        }
                    } else {
                        notifyUser("Please select a payment option", this);
                    }
                } else {

                    if (redeemEntry) {
                        String mRedeemValue = mChangeRedeemTv.getText()
                                .toString().trim();
                        if (!mRedeemValue.equals("0 points($0)")) {
                            notifyUser(
                                    "Please enter a valid gift card or credit card",
                                    this);
                        } else {

                            int id = addressFragment.getCheckedId();
                            if (addressFragment.getCheckedId() - 100 >= 0) {
                                setError(edtSecurityCode,
                                        savedCardSecurityErrorText,
                                        "Fill the Security Code");
                                makedLayoutVisible(mCredit_card_details_layout,
                                        mCreditCardLayout);
                            } else {
                                notifyUser("Please select a payment option",
                                        this);
                            }
                        }
                    } else {
                        if (cardHolderName.length() != 0
                                || cardCreditNumber.length() != 0
                                || securityCode.length() != 0) {

                            setError(edtSecurityCode,
                                    savedCardSecurityErrorText,
                                    "Fill the Security Code");
                            makedLayoutVisible(mCredit_card_details_layout,
                                    mCreditCardLayout);
                        } else {
                            notifyUser("Please select a payment option", this);
                        }

                    }

                }

            }

        }

    }

    /**
     * Validation for gift Card
     *
     * @return true/false
     */
    public boolean giftCardValidation() {
        boolean returnBoolean = true;
        mGiftcardEntered = false;
        giftCardNumber = edtGiftCardNumber.getText().toString().trim();
        giftCardPin = edtGiftCardPin.getText().toString().trim();
        if (giftCardNumber.length() != 0 || giftCardPin.length() != 0) {
            mGiftcardEntered = true;
            if (null == giftCardNumber || giftCardNumber.equals(" ")
                    || giftCardNumber.length() == 0) {

                setError(edtGiftCardNumber, giftcardNumberErrorText,
                        "Enter gift card number");
                makedLayoutVisible(mGiftCardExpandedLayout, mEnterGiftCard);
                returnBoolean = false;
            } else if (null == giftCardPin || giftCardPin.equals(" ")
                    || giftCardPin.length() == 0) {

                setError(edtGiftCardPin, giftCardPinErrorText,
                        "Enter gift card pin");
                makedLayoutVisible(mGiftCardExpandedLayout, mEnterGiftCard);
                returnBoolean = false;
            }
        }

        return returnBoolean;
    }

    private void getAllValues() {

        giftCardNumber = edtGiftCardNumber.getText().toString().trim();
        giftCardPin = edtGiftCardPin.getText().toString().trim();

        cardHolderName = edtCardHolderName.getText().toString().trim();
        cardCreditNumber = edtCardNumber.getText().toString().trim();
        if (cardCreditNumber.contains("*")) {
            if (null != UltaDataCache.getDataCacheInstance()
                    .getCreditCardDetails().get("cardnumber")) {
                cardCreditNumber = UltaDataCache.getDataCacheInstance()
                        .getCreditCardDetails().get("cardnumber");
            }
        }
        cardExpiryMonth = spinnerExpiryMonth.getSelectedItem().toString()
                .trim();
        if (cardExpiryMonth.equalsIgnoreCase("Select Expiry Month")) {

        } else {
            cardExpiryMonth = "" + spinnerExpiryMonth.getSelectedItemPosition();
            if (Integer.valueOf(cardExpiryMonth) < 10) {
                cardExpiryMonth = "0"
                        + Integer.valueOf(cardExpiryMonth).toString();
            } else
                cardExpiryMonth = Integer.valueOf(cardExpiryMonth).toString();
        }
        cardExpiryYear = spinnerExpiryYear.getSelectedItem().toString().trim();
        // cardType = spinnerUserCardType.getSelectedItem().toString().trim();
        securityCode = edtCardSecurityCode.getText().toString().trim();

        hashMap.put("cardholdername", cardHolderName);
        hashMap.put("cardnumber", cardCreditNumber);
        hashMap.put("cardtype", cardType);
        hashMap.put("cardexpirymonth", cardExpiryMonth);
        hashMap.put("cardexpiryyear", cardExpiryYear);
        hashMap.put("securitycode", securityCode);
        hashMap.put("cardTypeLocation", "" + cardTypeLocation);
        hashMap.put("expiryMonthLocation", "" + expiryMonthLocation);
        hashMap.put("expiryYearLocation", "" + expiryYearLocation);

        hashMap.put("giftcardNumber", giftCardNumber);

        hashMap.put("giftCardPin", giftCardPin);
        // 3.2 Release
        if (redeemEntry) {
            String mRedeemValue = mChangeRedeemTv.getText().toString().trim();
            if (!mRedeemValue.equals("0 points($0)")) {
                hashMap.put("redeemPointValue", mRedeemValue);
            }
        }

        UltaDataCache.getDataCacheInstance().setCreditCardDetails(hashMap);

        if (cardHolderName.length() != 0 || cardCreditNumber.length() != 0
                || securityCode.length() != 0) {

        } else {

            if (null != creditCardsList && creditCardsList.size() != 0) {
                int idCreditCard = addressFragment.getCheckedId() - 100;
                if (idCreditCard >= 0) {
                    cardHolderName = creditCardsList.get(idCreditCard)
                            .getNameOnCard();
                    cardCreditNumber = creditCardsList.get(idCreditCard)
                            .getCreditCardNumber();
                    cardType = creditCardsList.get(idCreditCard)
                            .getCreditCardType();
                    cardExpiryMonth = creditCardsList.get(idCreditCard)
                            .getExpirationMonth();
                    cardExpiryYear = creditCardsList.get(idCreditCard)
                            .getExpirationYear();
                    securityCode = edtSecurityCode.getText().toString().trim();
                }

            }

        }
        // }

    }

    private void invokePaypalService() {
        InvokerParams<ReviewOrderBean> invokerParams = new InvokerParams<ReviewOrderBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.PAYMENT_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateParametersForPayPalPayment());
        invokerParams.setUltaBeanClazz(ReviewOrderBean.class);
        PaymentPaypalHandler userCreationHandler = new PaymentPaypalHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);
        }
    }

    private Map<String, String> populateParametersForPayPalPayment() {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");

        urlParams.put("billingFirstName", "");
        urlParams.put("billingLastName", "");
        urlParams.put("billingAddress1", "");
        urlParams.put("billingAddress2", "");
        urlParams.put("billingCity", "");
        urlParams.put("billingCountry", "US");
        urlParams.put("billingZip", "");
        urlParams.put("billingPhone", billingPhone);
        urlParams.put("billingState", "");
        if (redeemEntry) {
            String mRedeemValue = mChangeRedeemTv.getText().toString().trim();
            if (!mRedeemValue.equals("0 points($0)")) {
                urlParams.put("loyaltyAmount", redeemPointvalue);
                mPaymentType = "Redeem Points";
            }

        }
        urlParams.put("mapValues.creditCardType", "");
        urlParams.put("mapValues.nameOnCard", "");
        urlParams.put("mapValues.creditCardNumber", "");
        urlParams.put("creditCardVerificationNumber2", "");
        urlParams.put("mapValues.expirationYear", "");
        urlParams.put("mapValues.expirationMonth", "");

        urlParams.put("paymentMethod", "paypal");
        return urlParams;
    }

    public class PaymentPaypalHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    mInvokePaypalService = true;
                    askRelogin(PaymentMethodActivity.this);
                } else {
                    try {
                        pd.dismiss();
                        notifyUser(getErrorMessage(),
                                PaymentMethodActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }

                }
            } else {
                if (null != pd && pd.isShowing()) {
                    pd.dismiss();
                }

                ReviewOrderBean ultaBean = (ReviewOrderBean) getResponseBean();
                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty())) {
                    try {
                        notifyUser(errors.get(0), PaymentMethodActivity.this);
                        setError(PaymentMethodActivity.this, errors.get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                } else {
                    if (ultaBean != null && ultaBean.getComponent() != null) {

                        String tokenId = ultaBean.getComponent().getTokenId();
                        Intent gotoPaypalPayment = new Intent(
                                PaymentMethodActivity.this,
                                PayPalWebviewActivity.class);
                        Log.e("PayPal", "TokenId from web service: " + tokenId);
                        gotoPaypalPayment.putExtra("token", tokenId);
                        startActivity(gotoPaypalPayment);
                    } else {
                        notifyUser(
                                "We're sorry, you can't pay with PayPal right now, please checkout using another payment method",
                                PaymentMethodActivity.this);
                        setError(
                                PaymentMethodActivity.this,
                                "We're sorry, you can't pay with PayPal right now, please checkout using another payment method");
                    }
                }
            }
        }
    }

    private void invokePreffredPaymentMethodDetails() {
        pd.setMessage("Fetching Saved Cards.....");
        pd.show();
        // fetchCreditCardsAction = UemAction
        // .enterAction(WebserviceConstants.ACTION_FETCH_CREDIT_CARD_INVOCATION);
        InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.PAYMENT_METHOD_DETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populatePaymentMethodDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
        RetrievePaymentDetailsHandler retrievePaymentDetailsHandler = new RetrievePaymentDetailsHandler();
        invokerParams.setUltaHandler(retrievePaymentDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);

        }

    }

    private Map<String, String> populatePaymentMethodDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "0");
        return urlParams;
    }

    public class RetrievePaymentDetailsHandler extends UltaHandler {

        public void handleMessage(Message msg) {
            Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    mInvokePreffredPaymentMethodDetails = true;
                    askRelogin(PaymentMethodActivity.this);


                } else {
                    try {
                        if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {

                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                PaymentMethodActivity.this);
                        setError(PaymentMethodActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                    loadingLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);
                }
            } else {
                Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                paymentMethodBean = (PaymentMethodBean) getResponseBean();
                if (null != paymentMethodBean) {
                    Logger.Log("<HomeActivity>" + "BeanPopulated");

                    creditCardsList = paymentMethodBean.getCreditCards();

                    if (null != creditCardsList && creditCardsList.size() != 0) {

                        String name, number, type, nickname, cardImage, creditCardNumber, date = null, details = null;
                        for (int i = 0; i < creditCardsList.size(); i++) {
                            // name=creditCardsList.get(i).getNameOnCard();
                            number = creditCardsList.get(i)
                                    .getCreditCardNumber();
                            creditCardNumber = creditCardsList.get(i)
                                    .getCreditCardNumber();
                            String numberOfCross = "";
                            for (int j = 0; j < number.length() - 4; j++) {
                                numberOfCross = numberOfCross + "x";
                            }
                            number = numberOfCross
                                    + number.substring(number.length() - 4,
                                    number.length());
                            nickname = creditCardsList.get(i).getNickName();
                            type = creditCardsList.get(i).getCreditCardType();
                            if (checkIfExpirationNeeded(type)) {
                                date = number + " - "
                                        + creditCardsList.get(i)
                                        .getExpirationMonth()
                                        + "/"
                                        + creditCardsList.get(i)
                                        .getExpirationYear();
                            } else {
                                date = number;
                            }
                            details = nickname;
                            addressFragment.setCreditCardsList(creditCardsList);
                            if (null != paymentMethodBean
                                    .getDefaultCreditCardId()) {
                                if (paymentMethodBean.getDefaultCreditCardId()
                                        .equals(creditCardsList.get(i).getId())) {
                                    addressFragment.addNewRow(details, date,
                                            true, creditCardNumber);
                                } else {
                                    addressFragment.addNewRow(details, date,
                                            false, creditCardNumber);
                                }
                            } else {
                                addressFragment.addNewRow(details, date, false, creditCardNumber);
                            }

                        }
                        if (creditCardsList != null
                                && !creditCardsList.isEmpty()) {
                            String address = addressFragment
                                    .getSelectedAddress();
                            id = addressFragment.getCheckedId() - 100;
                            if (id >= 0) {
                                String cardnumber = creditCardsList.get(id)
                                        .getCreditCardNumber();
                                String cardtype = creditCardsList.get(id)
                                        .getCreditCardType();
                                if (cardtype.equalsIgnoreCase("americanExpress")) {
                                    InputFilter[] FilterArray = new InputFilter[1];
                                    FilterArray[0] = new InputFilter.LengthFilter(4);
                                    edtSecurityCode.setFilters(FilterArray);
                                } else {
                                    InputFilter[] FilterArray = new InputFilter[1];
                                    FilterArray[0] = new InputFilter.LengthFilter(
                                            3);
                                    edtSecurityCode.setFilters(FilterArray);
                                }
                            }
                            hasCreditCardSaved = true;
                            isEnteringNewCard = false;
                        }

                        loadingLayout.setVisibility(View.GONE);
                        formLayout.setVisibility(View.VISIBLE);

                    } else {
                        creditFragment.setVisibility(View.GONE);

                        loadingLayout.setVisibility(View.GONE);
                        formLayout.setVisibility(View.VISIBLE);
                    }

                    try {
                        if (null != pd && pd.isShowing()) {
                            pd.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (null != pd && pd.isShowing()) {
                            try {
                                pd.dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loadingLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    // 3.2 Release

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

    /**
     * The choice of gift.
     */
    String choiceOfGift = "NoGift";
    /**
     * The check.
     */
    String check = "";

    private Map<String, String> populateGiftOptionPrameters() {
        if (UltaDataCache.getDataCacheInstance().isGiftTheOrder()) {
            choiceOfGift = getIntent().getStringExtra("choiceOfGift");
            if (null != getIntent().getStringExtra("choicemessageOfGift")) {
                check = getIntent().getStringExtra("message");
            }
        }
        Map<String, String> urlParams = new HashMap<String, String>();
        String gftWrap, gftNote;
        if (choiceOfGift.equalsIgnoreCase("NoGift")) {
            gftWrap = "false";
            gftNote = "false";
        } else if (choiceOfGift.equalsIgnoreCase("giftNote")) {
            gftWrap = "false";
            gftNote = "true";
        } else {
            gftWrap = "true";
            gftNote = "true";
        }
        Logger.Log(">>>>>>>>>>GiftWrap<<<<<<" + gftWrap);
        Logger.Log(">>>>>>>>>>GiftNote<<<<<<" + gftNote);

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("giftNoteSelected", gftNote);
        urlParams.put("giftWrapSelected", gftWrap);
        urlParams.put("giftMessage", check);

        return urlParams;
    }

    /**
     * The Class GiftOptionsHandler.
     */
    public class GiftOptionsHandler extends UltaHandler {

        public void handleMessage(Message msg) {
            Logger.Log("<GiftOptionsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                try {
                    notifyUser(getErrorMessage(), PaymentMethodActivity.this);
                    setError(PaymentMethodActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
                loadingLayout.setVisibility(View.GONE);
                formLayout.setVisibility(View.VISIBLE);
            } else {
                Logger.Log("<GiftOptionHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                AddToCartBean ultaBean = (AddToCartBean) getResponseBean();

                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty()))
                    try {
                        notifyUser(errors.get(0), PaymentMethodActivity.this);
                        setError(PaymentMethodActivity.this, errors.get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                else {
                    RedeemLevelPointsBean redeemPointLvel = null;
                    if (null != ultaBean) {
                        if (null != ultaBean.getComponent()) {
                            if (ultaBean.getComponent().getRedeemLevels() != null) {
                                redeemPointLvel = new RedeemLevelPointsBean();
                                redeemPointLvel = (RedeemLevelPointsBean) ultaBean
                                        .getComponent().getRedeemLevels();
                                redeemLevelPoints = redeemPointLvel
                                        .getRedeemLevels();
                                setRedeemPoints(redeemLevelPoints);
                                UltaDataCache
                                        .getDataCacheInstance()
                                        .setRedeemLevelPoints(redeemLevelPoints);
                            }

                        }
                    }
                }
            }
        }
    }

    private boolean isGiftCardValidationSuccess() {
        if (edtGiftCardNumber.getText().toString().length() != 0
                && edtGiftCardPin.getText().toString().length() != 0)
            return true;
        else
            return false;
    }

    /**
     * Gift Card Apply codings
     */
    private void invokeApplyGiftCard() {
        InvokerParams<UltaBean> invokerParams = new InvokerParams<UltaBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.APPLY_GIFT_CARD);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateApplyGiftCardHandlerParameters());
        invokerParams.setUltaBeanClazz(UltaBean.class);
        GiftCardHandler giftCardHandler = new GiftCardHandler();
        invokerParams.setUltaHandler(giftCardHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<UltaHomeActivity><invokeRootCategoryDetails()><UltaException>>"
                    + ultaException);

        }
    }

    private Map<String, String> populateApplyGiftCardHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("paymentMethod", "giftCard");
        urlParams.put("giftCardNumber", giftCardNumber);
        urlParams.put("giftCardPin", giftCardPin);
        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class GiftCardHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @SuppressWarnings("deprecation")
        public void handleMessage(Message msg) {
            Logger.Log("<RetrievePaymentDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    mInvokeApplyGiftCard = true;
                    askRelogin(PaymentMethodActivity.this);
                } else {
                    try {
                        // giftCardCheckoutAction
                        // .reportError(
                        // getErrorMessage(),
                        // WebserviceConstants.DYN_ERRCODE_GIFT_CARD_CHECKOUT_ACTIVITY);
                        // giftCardCheckoutAction.leaveAction();
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                PaymentMethodActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                    loadingLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);
                    if (null != pd && pd.isShowing()) {
                        try {
                            pd.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                UltaBean ultaBean = (UltaBean) getResponseBean();
                List<String> errors = ultaBean.getErrorInfos();
                if (null != errors && !(errors.isEmpty())) {

                    try {

                        final Dialog alertDialog = showAlertDialog(
                                PaymentMethodActivity.this, "Alert",
                                errors.get(0), "Ok", "");
                        alertDialog.show();

                        mDisagreeButton.setVisibility(View.GONE);
                        mAgreeButton.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();
                            }
                        });

                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                    edtSecurityCode.setText("");
                    edtCardSecurityCode.setText("");
                    loadingLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);
                    if (null != pd && pd.isShowing()) {
                        pd.dismiss();
                    }

                } else {
                    creditCardEntry = false;

                    if (isFromPaypal) {
                        invokePaypalService();
                        isFromPaypal = false;
                    } else if (cardHolderName.length() != 0
                            || cardCreditNumber.length() != 0
                            || securityCode.length() != 0) {
                        if (isEnteringNewCard) {
                            if (isCreditCardValidationSuccess(identifiedUserEnteredCardDetails)) {
                                giftCardEntry = true;
                                creditCardEntry = true;
                                invokePaymentService();
                            }
                            if (null != pd && pd.isShowing()) {
                                pd.dismiss();
                            }
                        } else {
                            if (null != creditCardsList
                                    && creditCardsList.size() != 0
                                    && (edtSecurityCode.getText().toString()
                                    .trim().length() != 0) || !checkIfExpirationNeeded(cardType)) {
                                int cardMaxlength = 0, cvvMaxLength = 0, cardMinLength = 0;
                                if (null != identifyCardType(cardCreditNumber).getCardMaxCVVLength()) {
                                    try {
                                        cardMaxlength = Integer.parseInt(identifyCardType(cardCreditNumber).getCardMaxNumberLength());
                                        cardMinLength = Integer.parseInt(identifyCardType(cardCreditNumber).getCardMinNumberLength());
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (cardCreditNumber.length() > cardMaxlength || cardCreditNumber.length() < cardMinLength) {
                                    setError(edtCardNumber, numberOnCardErrorText,
                                            WebserviceConstants.INVALID_CREDIT_CARD);
                                    edtCardNumber.requestFocus();
                                    makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                                } else {
                                    giftCardEntry = true;
                                    creditCardEntry = true;
//                                if (isCreditCardValidationSuccess()) {
                                    invokePaymentService();
                                    if (null != pd && pd.isShowing()) {
                                        pd.dismiss();
                                    }
//                                }
                                    if (null != pd && pd.isShowing()) {
                                        pd.dismiss();
                                    }
                                }
                            } else if (UltaDataCache.getDataCacheInstance()
                                    .isAnonymousCheckout()) {
                                if (cardHolderName.length() != 0
                                        || cardCreditNumber.length() != 0
                                        || securityCode.length() != 0) {
                                    if (isCreditCardValidationSuccess(identifiedUserEnteredCardDetails)) {
                                        giftCardEntry = true;
                                        creditCardEntry = true;
                                        invokePaymentService();
                                    }
                                }
                            } else {
                                giftCardEntry = true;
                                invokePaymentService();
                                if (null != pd && pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                        }
                    } else if (isGiftCardInsufficient) {
                        if (null != creditCardsList
                                && creditCardsList.size() != 0) {
                            int idCreditCard = addressFragment.getCheckedId() - 100;
                            if (idCreditCard >= 0) {
                                cardHolderName = creditCardsList.get(
                                        idCreditCard).getNameOnCard();
                                cardCreditNumber = creditCardsList
                                        .get(idCreditCard)
                                        .getCreditCardNumber();
                                cardType = creditCardsList.get(idCreditCard)
                                        .getCreditCardType();
                                cardExpiryMonth = creditCardsList
                                        .get(idCreditCard).getExpirationMonth();
                                cardExpiryYear = creditCardsList
                                        .get(idCreditCard).getExpirationYear();
                                securityCode = edtSecurityCode.getText()
                                        .toString().trim();
                            }

                        }

                        giftCardEntry = true;
                        creditCardEntry = true;
                        invokePaymentService();

                    } else {
                        giftCardEntry = true;
                        creditCardEntry = false;
                        invokePaymentService();
                    }

                }
            }
        }
    }

    private class MySpinnerAdapter<T> extends ArrayAdapter<T> {
        private Context m_cContext;


        public MySpinnerAdapter(Context ctx, T[] objects) {
            super(ctx, android.R.layout.simple_spinner_item, objects);
            this.m_cContext = ctx;
        }

        boolean firsttime = true;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (firsttime) {
                firsttime = false;
                // just return some empty view
                TextView tv = new TextView(m_cContext);

                if (null != UltaDataCache.getDataCacheInstance()
                        .getCreditCardDetails()) {

                    tv.setText(result1.get(Integer.parseInt(UltaDataCache
                            .getDataCacheInstance().getCreditCardDetails()
                            .get("expiryMonthLocation"))));

                    spinnerExpiryMonth.setSelection(Integer
                            .parseInt(UltaDataCache.getDataCacheInstance()
                                    .getCreditCardDetails()
                                    .get("expiryMonthLocation")));


                } else {
                    tv.setText(result1.get(position));
                    spinnerExpiryMonth.setSelection(position);
                }

                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setPadding(5, 0, 0, 0);
                return tv;
            }
            // let the array adapter takecare this time
            return super.getView(position, convertView, parent);
        }
    }

    private class MySpinnerAdapter1<T> extends ArrayAdapter<T> {
        private Context m_cContext;


        public MySpinnerAdapter1(Context ctx, T[] objects) {
            super(ctx, android.R.layout.simple_spinner_item, objects);
            this.m_cContext = ctx;
        }

        boolean firsttime = true;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (firsttime) {
                firsttime = false;
                // just return some empty view
                TextView tv = new TextView(m_cContext);

                if (null != UltaDataCache.getDataCacheInstance()
                        .getCreditCardDetails()) {
                    tv.setText(result2.get(Integer.parseInt(UltaDataCache
                            .getDataCacheInstance().getCreditCardDetails()
                            .get("expiryYearLocation"))));
                    spinnerExpiryYear.setSelection(Integer
                            .parseInt(UltaDataCache.getDataCacheInstance()
                                    .getCreditCardDetails()
                                    .get("expiryYearLocation")));
                } else {
                    tv.setText(result2.get(position));
                    spinnerExpiryYear.setSelection(position);
                }
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setPadding(5, 0, 0, 0);
                return tv;
            }
            // let the array adapter takecare this time
            return super.getView(position, convertView, parent);
        }
    }

    /**
     * New card validation for 4.2 release
     *
     * @param userEnteredCardDetails on entering card number the card type is identified
     * @return
     */
    private boolean isCreditCardValidationSuccess(CreditCardInfoBean userEnteredCardDetails) {
        //If card type is identified for the user entered card number
        if (null != userEnteredCardDetails) {
            //get maxlength of card number and CVV for identified card type
            int cardMaxlength = 0, cvvMaxLength = 0, cardMinLength = 0;
            boolean cardMinAndMaxSame = false;
            if (null != userEnteredCardDetails.getCardMaxCVVLength()) {
                try {
                    cardMaxlength = Integer.parseInt(userEnteredCardDetails.getCardMaxNumberLength());
                    cardMinLength = Integer.parseInt(userEnteredCardDetails.getCardMinNumberLength());
                    if (cardMaxlength == cardMaxlength) {
                        cardMinAndMaxSame = true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (null != userEnteredCardDetails.getCardMaxCVVLength()) {
                try {
                    cvvMaxLength = Integer.parseInt(userEnteredCardDetails.getCardMaxCVVLength());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            //Check if card holder's name is entered
            if (null == cardHolderName || cardHolderName.trim().equals("")
                    || cardHolderName.trim().length() == 0) {
                setError(edtCardHolderName, nameOnCardErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NAME);
                edtCardHolderName.requestFocus();
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                return false;
            }
            //Check if card number is entered
            else if (null == cardCreditNumber || cardCreditNumber.equals(" ")
                    || cardCreditNumber.length() == 0) {
                setError(edtCardNumber, numberOnCardErrorText,
                        WebserviceConstants.ENTER_CARD_NUMBER);
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardNumber.requestFocus();
                return false;
            } else if (cardMinAndMaxSame && cardCreditNumber.length() != cardMaxlength) {
                setError(edtCardNumber, numberOnCardErrorText, "The card number must be " + cardMaxlength + " digits");
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardNumber.requestFocus();
                return false;
            } else if (!cardMinAndMaxSame && cardCreditNumber.length() > cardMaxlength) {
                setError(edtCardNumber, numberOnCardErrorText, "Card number must be " + cardMaxlength + " digits or lesser.");
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardNumber.requestFocus();
                return false;
            } else if (!cardMinAndMaxSame && cardCreditNumber.length() < cardMinLength) {
                setError(edtCardNumber, numberOnCardErrorText, "Card number must be " + cardMinLength + " digits or greater.");
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardNumber.requestFocus();
                return false;
            }
            //Check if expiry is required and expiry month is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && cardExpiryMonth.equalsIgnoreCase("Select Expiry Month")) {
                expiryMonthErrorText.setText(WebserviceConstants.SELECT_EXPIRY_MONTH);
                expiryMonthErrorText.setVisibility(View.VISIBLE);
                edtSecurityCode.setText("");
                edtCardSecurityCode.setText("");
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                return false;
            }
            //Check if expiry is required and expiry year is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && cardExpiryYear.equalsIgnoreCase("Select Expiry Year")) {
                expiryYearErrorText.setText(WebserviceConstants.SELECT_EXPIRY_YEAR);
                expiryYearErrorText.setVisibility(View.VISIBLE);
                edtSecurityCode.setText("");
                edtCardSecurityCode.setText("");
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                return false;
            } else if (userEnteredCardDetails.getCardUsesCVV() && (null == securityCode || securityCode.equals(" ")
                    || securityCode.length() == 0)) {
                setError(edtCardSecurityCode, creditSecurityErrorText,
                        WebserviceConstants.ENTER_SECURITY_CODE);
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardSecurityCode.requestFocus();
                return false;
            } else if (userEnteredCardDetails.getCardUsesCVV() && securityCode.length() != cvvMaxLength) {
                setError(edtCardSecurityCode, creditSecurityErrorText,
                        WebserviceConstants.ENTER_SECURITY_CODE);
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                edtCardSecurityCode.requestFocus();
                return false;
            }

        }
        //card type is not identified for the card number
        else {
            //Check if card holder's name is entered
            if (null == cardHolderName || cardHolderName.trim().equals("")
                    || cardHolderName.trim().length() == 0) {
                setError(edtCardHolderName, nameOnCardErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NAME);
                edtCardHolderName.requestFocus();
                makedLayoutVisible(mCredit_card_details_layout, mCreditCardLayout);
                return false;
            } else {
                setError(edtCardNumber, numberOnCardErrorText,
                        WebserviceConstants.INVALID_CREDIT_CARD);
                edtCardNumber.requestFocus();
                return false;
            }

        }
        return true;
    }

    private void invokePaymentService() {
        pd.setMessage("Loading");
        pd.show();
        InvokerParams<ReviewOrderBean> invokerParams = new InvokerParams<ReviewOrderBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.PAYMENT_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populatePaymentParameters());
        invokerParams.setUltaBeanClazz(ReviewOrderBean.class);
        PaymentHandler userCreationHandler = new PaymentHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            pd.setMessage("Loading");
            pd.show();
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddNewShippingAddressActivity><invokeForgotPassword><UltaException>>"
                    + ultaException);

        }

    }

    private Map<String, String> populatePaymentParameters() {

        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");
        if (redeemEntry) {
            String mRedeemValue = mChangeRedeemTv.getText().toString().trim();
            if (!mRedeemValue.equals("0 points($0)")) {
                urlParams.put("loyaltyAmount", redeemPointvalue);
                mPaymentType = "Redeem Points";
            }

        }
        if (null != UltaDataCache.getDataCacheInstance().getShippingAddress()) {
            formdata = UltaDataCache.getDataCacheInstance()
                    .getShippingAddress();
            if (formdata.get("shippingAsBilling").equals("true")) {

                urlParams.put("billingFirstName", formdata.get("first"));
                urlParams.put("billingLastName", formdata.get("last"));
                urlParams.put("billingAddress1", formdata.get("addressline1"));
                urlParams.put("billingAddress2", formdata.get("addressline2"));
                urlParams.put("billingCity", formdata.get("city"));
                urlParams.put("billingCountry", "US");
                urlParams.put("billingZip", formdata.get("zipcode"));
                urlParams.put("billingPhone", billingPhone);
                urlParams.put("billingState", formdata.get("state"));
                urlParams.put("paymentMethod", "creditCard");
                if (creditCardEntry) {
                    urlParams.put("mapValues.creditCardType", cardType);
                    urlParams.put("mapValues.creditCardNumber",
                            cardCreditNumber);
                    urlParams.put("mapValues.nameOnCard", cardHolderName);
                    if (cardExpiryMonth.equalsIgnoreCase("Select Expiry Month")) {
                        urlParams.put("mapValues.expirationMonth", "");
                    } else {
                        urlParams.put("mapValues.expirationMonth", cardExpiryMonth);
                    }
                    if (cardExpiryYear.equalsIgnoreCase("Select Expiry Year")) {
                        urlParams.put("mapValues.expirationYear", "");

                    } else {
                        urlParams.put("mapValues.expirationYear", cardExpiryYear);

                    }

                    urlParams
                            .put("creditCardVerificationNumber2", securityCode);

                    String omnitureCardType = omnitureCardType(cardType);
                    if (null != mPaymentType
                            && !mPaymentType.equalsIgnoreCase("")) {
                        mPaymentType = mPaymentType + "," + omnitureCardType;
                    } else {
                        mPaymentType = omnitureCardType;
                    }

                }
                if (giftCardEntry) {
                    urlParams.put("mapValues.creditCardType", cardType);
                    urlParams.put("giftCardNumber", giftCardNumber);
                    urlParams.put("giftCardPin", giftCardPin);
                    if (null != mPaymentType
                            && !mPaymentType.equalsIgnoreCase("")) {
                        mPaymentType = mPaymentType + "," + "Gift Card";
                    } else {
                        mPaymentType = "Gift Card";
                    }
                }

            } else {
                if (null != UltaDataCache.getDataCacheInstance()
                        .getBillingAddress()) {
                    HashMap<String, String> billingdata = new HashMap<String, String>();
                    billingdata = UltaDataCache.getDataCacheInstance()
                            .getBillingAddress();
                    urlParams.put("billingFirstName",
                            billingdata.get("FirstName"));
                    urlParams.put("billingLastName",
                            billingdata.get("LastName"));
                    urlParams.put("billingAddress1",
                            billingdata.get("Address1"));
                    urlParams.put("billingAddress2",
                            billingdata.get("Address2"));
                    urlParams.put("billingCity", billingdata.get("City"));
                    urlParams.put("billingCountry", "US");
                    urlParams.put("billingZip", billingdata.get("ZipCode"));
                    urlParams.put("billingPhone", billingdata.get("Phone"));
                    if (billingdata.get("State").length() >= 2) {
                        urlParams.put("billingState", billingdata.get("State")
                                .substring(0, 2));
                    }
                    urlParams.put("paymentMethod", "creditCard");
                    if (creditCardEntry) {
                        urlParams.put("mapValues.creditCardType", cardType);
                        urlParams.put("mapValues.creditCardNumber",
                                cardCreditNumber);
                        urlParams.put("mapValues.nameOnCard", cardHolderName);
                        urlParams.put("mapValues.expirationMonth",
                                cardExpiryMonth);
                        urlParams.put("mapValues.expirationYear",
                                cardExpiryYear);
                        urlParams.put("creditCardVerificationNumber2",
                                securityCode);
                        String omnitureCardType = omnitureCardType(cardType);

                        if (null != mPaymentType
                                && !mPaymentType.equalsIgnoreCase("")) {
                            mPaymentType = mPaymentType + ","
                                    + omnitureCardType;
                        } else {
                            mPaymentType = omnitureCardType;
                        }

                    }
                    if (giftCardEntry) {
                        urlParams.put("mapValues.creditCardType", cardType);
                        urlParams.put("giftCardNumber", giftCardNumber);
                        urlParams.put("giftCardPin", giftCardPin);
                        if (null != mPaymentType
                                && !mPaymentType.equalsIgnoreCase("")) {
                            mPaymentType = mPaymentType + "," + "Gift Card";
                        } else {
                            mPaymentType = "Gift Card";
                        }
                    }

                }
            }

        } else {

        }

        return urlParams;
    }

    public String omnitureCardType(String cardType) {

        if (cardType.equalsIgnoreCase("VISA")) {
            return "Visa";
        } else if (cardType.equalsIgnoreCase("DISCOVER")) {
            return "Discover";
        } else if (cardType.equalsIgnoreCase("AMERICANEXPRESS")) {
            return "American Express";
        } else if (cardType.equalsIgnoreCase("MASTERCARD")) {
            return "Master Card";
        }

        return cardType;
    }

    /**
     * The Class PaymentHandler.
     */
    public class PaymentHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @SuppressWarnings("deprecation")
        public void handleMessage(Message msg) {
            pd.setMessage("Loading");
            pd.show();
            if (null != pd && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    mInvokePaymentService = true;
                    askRelogin(PaymentMethodActivity.this);
                    // verifyCrediCardAction.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // verifyCrediCardAction.leaveAction();
                } else {
                    try {

                        notifyUser(getErrorMessage(),
                                PaymentMethodActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                    loadingLayout.setVisibility(View.GONE);
                    formLayout.setVisibility(View.VISIBLE);

                }
            } else {

                Logger.Log("<PaymentHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                ReviewOrderBean ultaBean = (ReviewOrderBean) getResponseBean();
                List<String> errors = ultaBean.getErrorInfos();
                String displayMessage = null;

                if (null != ultaBean && null != ultaBean.getComponent()) {
                    CheckoutCartBean paymentDetails = ultaBean.getComponent()
                            .getPaymentDetails();
                    if (null != paymentDetails
                            && null != paymentDetails
                            .getGiftCardPaymentGroups()
                            && !paymentDetails.getGiftCardPaymentGroups()
                            .isEmpty()) {
                        List<CheckoutGiftCardPaymentGroupBean> giftCardPaymentGroups = paymentDetails
                                .getGiftCardPaymentGroups();
                        for (int j = 0; j < giftCardPaymentGroups.size(); j++) {
                            if (null != giftCardPaymentGroups.get(j)
                                    && null != giftCardPaymentGroups.get(j)
                                    .getInsufficientGCBalanceMessage()) {
                                displayMessage = giftCardPaymentGroups.get(j)
                                        .getInsufficientGCBalanceMessage();
                                break;
                            }
                        }
                    }
                }
                if (null != displayMessage) {
                    if (creditCardEntry) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("addCard", "false");
                        UltaDataCache.getDataCacheInstance().setPaymentMethod(
                                map);
                        if (redeemEntry) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRedeemLevelPoints(null);
                        }
                        Intent goToShippingMethod = new Intent(
                                PaymentMethodActivity.this,
                                ReviewOrderActivity.class);
                        goToShippingMethod.putExtra("order", ultaBean);
                        startActivity(goToShippingMethod);
                    } else {
                        try {
                            mPaymentType = "";
                            isGiftCardInsufficient = true;


                            final Dialog alertDialog = showAlertDialog(
                                    PaymentMethodActivity.this,
                                    "Alert",
                                    "Ordered amount exceeds GiftCard balance. Please select a Credit Card to complete the payment.",
                                    "Ok", "");
                            alertDialog.show();

                            mDisagreeButton.setVisibility(View.GONE);
                            mAgreeButton
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            alertDialog.dismiss();
                                        }
                                    });

                        } catch (WindowManager.BadTokenException e) {
                        } catch (Exception e) {
                        }
                    }

                } else if (null != errors && !(errors.isEmpty())) {
                    try {
                        mPaymentType = "";
                        Logger.Log("++++" + errors.get(0));
                        if (errors.get(0).contains("credit card")
                                || errors.get(0).contains("Security Code")) {


                            final Dialog alertDialog = showAlertDialog(
                                    PaymentMethodActivity.this, "Alert",
                                    errors.get(0), "Ok", "");
                            alertDialog.show();

                            mDisagreeButton.setVisibility(View.GONE);
                            mAgreeButton
                                    .setOnClickListener(new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            alertDialog.dismiss();
                                        }
                                    });

                        } else {
                            notifyUser(errors.get(0),
                                    PaymentMethodActivity.this);
                        }
                        edtSecurityCode.setText("");
                        edtCardSecurityCode.setText("");
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                } else {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("addCard", "false");
                    UltaDataCache.getDataCacheInstance().setPaymentMethod(map);
                    if (redeemEntry) {
                        UltaDataCache.getDataCacheInstance()
                                .setRedeemLevelPoints(null);
                    }
                    Intent goToShippingMethod = new Intent(
                            PaymentMethodActivity.this,
                            ReviewOrderActivity.class);
                    goToShippingMethod.putExtra("order", ultaBean);
                    startActivity(goToShippingMethod);
                }
                trackEvarsUsingActionName(PaymentMethodActivity.this,
                        WebserviceConstants.CHECKOUT_STEP_5_EVENT_ACTION,
                        WebserviceConstants.PAYMENT_TYPE_KEY, mPaymentType);

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
                            Double totalAdjustment = 0.0;
                            List<CheckoutOrderAdjustmentBean> orderAdjustments = checkoutShippmentMethodBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();

                            mSubTotalValueTextView.setText("$"
                                    + checkoutShippmentMethodBean
                                    .getComponent().getCart()
                                    .getOrderDetails()
                                    .getRawSubtotal());
//                            }
                            //tiered price: Additional discount

                            if (null != checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount() &&
                                    !checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
                                mTvAdditionalDiscountValue.setText("-$"
                                        + String.format("%.2f",
                                        Double.valueOf(checkoutShippmentMethodBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount())));
                            }
                            if (null != UltaDataCache.getDataCacheInstance()
                                    .getShippingType()) {
                                mShippingTypeTextView.setText(UltaDataCache
                                        .getDataCacheInstance()
                                        .getShippingType());
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
                                    + String.format("%.2f",
                                    checkoutShippmentMethodBean
                                            .getComponent().getCart()
                                            .getOrderDetails()
                                            .getShipping()));
                            if (Double.valueOf(checkoutShippmentMethodBean
                                    .getComponent().getCart()
                                    .getOrderDetails()
                                    .getShipping()).equals(0.0)) {
                                mShippingTypeValueTextView.setText("FREE");
                            }
                            double total = checkoutShippmentMethodBean
                                    .getComponent().getCart().getOrderDetails()
                                    .getTotal();

                            mTotalValueTextView.setText("$"
                                    + String.format("%.2f", total));
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
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setFooterValues(AddToCartBean addToCartBean) {
        isGiftEnabled = true;
        try {
            if (null != addToCartBean) {
                if (null != addToCartBean.getComponent()) {
                    if (null != addToCartBean.getComponent().getCart()) {
                        if (null != addToCartBean.getComponent().getCart()
                                .getOrderDetails()) {
                            mTaxValueTextView.setText("$"
                                    + String.format("%.2f", addToCartBean
                                    .getComponent().getCart()
                                    .getOrderDetails().getTax()));
                            if (null != UltaDataCache.getDataCacheInstance()
                                    .getShippingType()) {
                                mShippingTypeTextView.setText(UltaDataCache
                                        .getDataCacheInstance()
                                        .getShippingType());
                            }

                            Double totalAdjustment = 0.0;
                            List<OrderAdjustmentBean> orderAdjustments = addToCartBean
                                    .getComponent().getCart()
                                    .getOrderAdjustments();


                            mSubTotalValueTextView
                                    .setText("$"
                                            + String.format(
                                            "%.2f",
                                            (addToCartBean
                                                    .getComponent()
                                                    .getCart()
                                                    .getOrderDetails()
                                                    .getRawSubtotal())));


                            if (null != addToCartBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount() &&
                                    !addToCartBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount().isEmpty()) {
                                mTvAdditionalDiscountValue.setText("-$"
                                        + String.format("%.2f",
                                        Double.valueOf(addToCartBean.getComponent().getCart().getOrderDetails().getTieredDiscountAmount())));
                            }

                            mShippingTypeValueTextView.setText("$"
                                    + String.format("%.2f", addToCartBean
                                    .getComponent().getCart()
                                    .getOrderDetails().getShipping()));
                            if (Double.valueOf(addToCartBean
                                    .getComponent().getCart()
                                    .getOrderDetails().getShipping()).equals(0.0)) {
                                mShippingTypeValueTextView.setText("FREE");
                            }
                            if (null != redeemPointvalue) {
                                mRedeemPointsFooterLayout
                                        .setVisibility(View.VISIBLE);
                                mReedemablePointsTextView.setText("Redeem "
                                        + mTotalRedeemPoints + " points");
                                mReedemablePointsTextViewValue.setText("-$"
                                        + String.format("%.2f",
                                        redeemPointvalue));
                            }

                            double redeemValue = 0.00;
                            if (null != redeemPointvalue) {
                                redeemValue = Double
                                        .parseDouble(redeemPointvalue);
                            }


                            double total = addToCartBean.getComponent()
                                    .getCart().getOrderDetails().getTotalNew();

                            mTotalValueTextView.setText("$"
                                    + String.format("%.2f", total));
                        }
                        //Coupon Value
                        if (null != addToCartBean.getComponent().getCart().getCouponDiscount() &&
                                null != addToCartBean.getComponent().getCart().getCouponDiscount().getOrderDiscount() &&
                                null != addToCartBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment()) {
                            if (Double.valueOf(addToCartBean.getComponent().getCart().getCouponDiscount().getOrderDiscount()).equals(0.0)) {
                                mCouponDiscountLayout.setVisibility(View.GONE);
                            } else {
                                mTvCouponDiscountValue.setText("-$"
                                        + String.format(
                                        "%.2f",
                                        Double.valueOf(addToCartBean.getComponent().getCart().getCouponDiscount().getTotalAdjustment())));
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void creatingPageName() {
        String categoryname = "";
        if (UltaDataCache.getDataCacheInstance().isAnonymousCheckout()) {
            categoryname = WebserviceConstants.PAYMENT_GUEST_PAGE;
        } else if (Utility.retrieveBooleanFromSharedPreference(
                UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER,
                getApplicationContext())) {
            categoryname = WebserviceConstants.PAYMENT_LOYALITY_PAGE;
        } else {
            categoryname = WebserviceConstants.PAYMENT_NON_LOYALITY_PAGE;
        }

        trackAppState(this, categoryname);
    }

    public void setFilterForsecurityCode(int id) {
        if (null != creditCardsList && creditCardsList.size() != 0) {
            id = id - 100;
            edtSecurityCode.setText("");
            String cardtype = creditCardsList.get(id).getCreditCardType();
            if (cardtype.equalsIgnoreCase("AmericanExpress")) {
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(4);
                edtSecurityCode.setFilters(FilterArray);
            } else {
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(3);
                edtSecurityCode.setFilters(FilterArray);
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public void createPaymentTypeString(String paymentTypeToBeAdded) {
        if (null != UltaDataCache.getDataCacheInstance().getPaymentType()) {
            String paymentType = UltaDataCache.getDataCacheInstance()
                    .getPaymentType();
            UltaDataCache.getDataCacheInstance().setPaymentType(
                    paymentType + "+" + paymentTypeToBeAdded);
        } else {
            UltaDataCache.getDataCacheInstance().setPaymentType(
                    paymentTypeToBeAdded);
        }
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            navigateToBasketOnSessionTimeout(PaymentMethodActivity.this);
        } else {
            if (null != pd && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.hashCode() == edtSecurityCode.getText().hashCode()) {
            edtSecurityCode.setBackgroundDrawable(originalDrawable);
            savedCardSecurityErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtCardHolderName.getText().hashCode()) {
            edtCardHolderName.setBackgroundDrawable(originalDrawable);
            nameOnCardErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtCardNumber.getText().hashCode()) {
            edtCardNumber.setBackgroundDrawable(originalDrawable);
            numberOnCardErrorText.setVisibility(View.GONE);
            checkCreditCardTypeExpirationAndCVV(s.toString().trim());
        } else if (s.hashCode() == edtCardSecurityCode.getText().hashCode()) {
            edtCardSecurityCode.setBackgroundDrawable(originalDrawable);
            creditSecurityErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtGiftCardNumber.getText().hashCode()) {
            edtGiftCardNumber.setBackgroundDrawable(originalDrawable);
            giftcardNumberErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtGiftCardPin.getText().hashCode()) {
            edtGiftCardPin.setBackgroundDrawable(originalDrawable);
            giftCardPinErrorText.setVisibility(View.GONE);
        }
    }

    public void setError(EditText editText, TextView errorTV, String message) {
        edtSecurityCode.setText("");
        edtCardSecurityCode.setText("");
        errorTV.setText("" + message);
        errorTV.setVisibility(View.VISIBLE);
    }

    public void makedLayoutVisible(LinearLayout layoutToBeVisible,
                                   LinearLayout layoutToClick) {
        if (layoutToBeVisible.getVisibility() != View.VISIBLE) {
            layoutToClick.performClick();
        }
    }

    public void showExpiryDialog() {
        notifyUser("Credit Card Expired", PaymentMethodActivity.this);
    }

    public boolean isCardExpired(String expiryYear, int expiryMonth) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        if (Integer.parseInt(expiryYear) < year) {
            return true;
        } else if (expiryYear.equals("" + year) && expiryMonth <= month) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check credit card type
     * Check if expiration is needed
     * Check if cvv is needed
     *
     * @param cardNumber
     */
    private void checkCreditCardTypeExpirationAndCVV(String cardNumber) {
        cardType = "";
        identifiedUserEnteredCardDetails = null;
        boolean cardTypeIdentified = false;
        if (null != identifyCardType(cardNumber)) {
            mAcceptedCardTypesLayout.setVisibility(View.GONE);
            CreditCardInfoBean identifiedCard = identifyCardType(cardNumber);
            identifiedUserEnteredCardDetails = identifiedCard;
            cardTypeIdentified = true;
            cardType = identifiedCard.getCardType();
            isCVVRequired = identifiedCard.getCardUsesCVV();
            isExpirationDateRequired = identifiedCard.getCardUsesExpirationDate();

            Log.d("cardType", "" + cardType);
            Log.d("isCVVRequired", "" + isCVVRequired);
            Log.d("isExpiration", "" + isExpirationDateRequired);

            if (cardType.equalsIgnoreCase("americanExpress")) {
                edtCardSecurityCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            } else {
                edtCardSecurityCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            }

            if (isCVVRequired) {
                msecurityCodeLayout.setVisibility(View.VISIBLE);
            } else {
                msecurityCodeLayout.setVisibility(View.GONE);
            }
            if (isExpirationDateRequired) {
                edtCardNumber.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                edtCardNumber.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView arg0, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideKeyboard();
                            edtCardNumber.clearFocus();
                            spinnerExpiryMonth.requestFocus();
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    spinnerExpiryMonth.performClick();
                                }
                            }, 200);

                        }
                        return true;
                    }
                });
                mExpiryLayout.setVisibility(View.VISIBLE);
            } else {
                edtCardNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
                edtCardNumber.setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView arg0, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideKeyboard();
                        }
                        return true;
                    }
                });
                mExpiryLayout.setVisibility(View.GONE);
            }
            LoadImageFromWebOperations(identifiedCard.getCardImage());
        }
        if (!cardTypeIdentified) {
            mAcceptedCardTypesLayout.setVisibility(View.VISIBLE);
            edtCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.creditcard_default), null);
            msecurityCodeLayout.setVisibility(View.VISIBLE);
            mExpiryLayout.setVisibility(View.VISIBLE);
            edtCardNumber.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        }
        if (cardNumber.isEmpty() || cardNumber.length() == 0) {
            mAcceptedCardTypesLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Download image from url using Aquery and set to the card type
     *
     * @param url
     */
    public void LoadImageFromWebOperations(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            AQuery androidQuery = new AQuery(PaymentMethodActivity.this);
            androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
                    edtCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                }
            });
        } catch (Exception e) {
            Logger.Log("<UltaException>>"
                    + e);
        }
    }

    public void hideOrShowSecurityForSavedCards(String cardNumber) {
        if (null != identifyCardType(cardNumber)) {
            CreditCardInfoBean identifiedCard = identifyCardType(cardNumber);
            if (identifiedCard.getCardUsesCVV()) {
                edtSecurityCode.setVisibility(View.VISIBLE);
            } else {
                edtSecurityCode.setVisibility(View.GONE);
            }
        }
    }

}
