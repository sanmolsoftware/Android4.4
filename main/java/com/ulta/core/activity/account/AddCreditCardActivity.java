/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.bean.account.CreditCardInfoBean;
import com.ulta.core.bean.account.PaymentMethodBean;
import com.ulta.core.bean.account.StateListBean;
import com.ulta.core.bean.checkout.PaymentDetailsBean;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

public class AddCreditCardActivity extends UltaBaseActivity implements
        OnDoneClickedListener, OnSessionTimeOut, TextWatcher {
    PaymentMethodBean paymentMethodBean;

    EditText edtProfilecreditCardNickname, edtProfilenameOnCard,
            edtProfilecreditCardNumber, edtProfilefirstName,
            edtProfilelastName, edtProfilephoneNumber, edtProfileaddress1,
            edtProfileaddress2, edtProfilecity, edtProfilezip,
            edtProfilenickname;

    Spinner spProfileExpirationMonth, spProfileExpirationYear;

    String strProfilecreditCardNickname,
            strProfilecreditCardType = "Select Card Type",
            strProfilenameOnCard, strProfilecreditCardNumber,
            strProfilefirstName, strProfilelastName, strProfilephoneNumber,
            strProfileaddress1, strProfileaddress2, strProfilecity,
            strProfilezip, strProfilenickname, strProfileExpirationYear,
            strProfilecountry, strProfileExpirationMonth, strProfileState,
            posofspinneritem, phone1;

    /* private String[] anArrayOfStrings; */
    private boolean isSpinnerSelected = false;
    private Spinner spState;
    private Button mDoneButton;
    String setasDefault = "false";
    CheckBox setDefault;

    private ArrayList<String> result1, result2, cardType;
    private List<String> result;
    private ProgressDialog pd;
    PaymentDetailsBean editCardDetails;
    private String defaultChecked;
    int CardLocation, expiryMonthLocation;

    LinearLayout mainLayout;
    FrameLayout loadingLayout;
    private StateListBean stateListBean;

    private String state, expirationYear;

    private int MY_SCAN_REQUEST_CODE = 100;

    private TextView nickNameErrorText, nameErrorText,
            cardNumberErrorText, expiryMonthErrorText, expiryYearErrorText,
            firstnameErrorText, profilenickNameErrorText, lastNameErrorText,
            phoneErrorText, address1ErrorText, cityErrorText, stateErrorText,
            zipErrorText;
    private Drawable originalDrawable;
    private boolean setStateIfEditCard = false;
    private CreditCardInfoBean identifiedUserEnteredCardDetails = null;
    private Boolean isExpirationDateRequired = true;
    private LinearLayout mExpiryLayout;
    boolean isEditCardPage = false;
    private LinearLayout mAcceptedCardTypesLayout,mCardImageLayout;
    private String serviceCall="addCard";

    /*
     * @Override protected void onResume() {
     * if(!isUltaCustomer(AddCreditCardActivity.this)){ finish(); }
     * super.onResume(); }
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_add_new_credit_card);
        setActivity(AddCreditCardActivity.this);
        setTitle("Add New Card");
        intViews();
        if (null != getIntent().getExtras()) {
            if (null != getIntent().getExtras().get("CardToEdit")) {
                editCardDetails = (PaymentDetailsBean) getIntent().getExtras()
                        .get("CardToEdit");
                if (null != editCardDetails) {
                    setStateIfEditCard = true;
                }

                defaultChecked = (String) getIntent().getExtras()
                        .get("default");
                if (defaultChecked != null
                        && defaultChecked.equals(editCardDetails.getId())) {
                    setDefault.setChecked(true);
                }
                prePopulateValues();
                setTitle("Edit Credit Card");
                edtProfilecreditCardNickname.setEnabled(false);
                edtProfilecreditCardNumber.setEnabled(false);
            }
        }
        loadingLayout.setVisibility(View.VISIBLE);
        pd = new ProgressDialog(AddCreditCardActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);
        invokeStateList();
        setAdaptersAndListeners();
        setDefault.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg0.isChecked()) {

                    setasDefault = "true";

                } else {
                    setasDefault = "false";
                }
            }
        });

    }

    private void prePopulateValues() {
        isEditCardPage = true;
        String phoneNumber = null;
        if (null != editCardDetails) {

            try {
                String[] phnum = editCardDetails.getPhoneNumber().split("-");
                phoneNumber = phnum[0];
                for (int i = 1; i < phnum.length; i++) {
                    phoneNumber = phoneNumber + phnum[i];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            edtProfilecreditCardNickname.setText(editCardDetails.getNickName());
            edtProfilenameOnCard.setText(editCardDetails.getNameOnCard());
            edtProfilecreditCardNumber.setText("************"
                    + editCardDetails.getCreditCardNumber().substring(12));
            edtProfilecreditCardNumber
                    .setOnTouchListener(new OnTouchListener() {

                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            if (null != edtProfilecreditCardNumber.getText()) {
                                if (edtProfilecreditCardNumber.getText()
                                        .toString().length() == 16)
                                    if (edtProfilecreditCardNumber.getText()
                                            .toString().contains("*")) {
                                        edtProfilecreditCardNumber
                                                .setText(editCardDetails
                                                        .getCreditCardNumber());
                                    }
                            }
                            return false;
                        }
                    });
            edtProfilefirstName.setText(editCardDetails.getFirstName());
            edtProfilelastName.setText(editCardDetails.getLastName());
            edtProfilephoneNumber.setText(phoneNumber);
            edtProfileaddress1.setText(editCardDetails.getAddress1());
            edtProfileaddress2.setText(editCardDetails.getAddress2());
            edtProfilecity.setText(editCardDetails.getCity());
            edtProfilezip.setText(editCardDetails.getPostalCode());
            edtProfilenickname.setText(editCardDetails.getNickName());
            edtProfilenickname.setVisibility(View.GONE);
            profilenickNameErrorText.setVisibility(View.GONE);
            try {
                expiryMonthLocation = null != editCardDetails.getExpirationMonth() ? Integer
                        .parseInt(editCardDetails.getExpirationMonth()) : -1;
                expirationYear = editCardDetails.getExpirationYear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            strProfilecreditCardType = editCardDetails.getCreditCardType();
            if (checkIfExpirationNeeded(strProfilecreditCardType)) {
                mExpiryLayout.setVisibility(View.VISIBLE);
            } else {
                mExpiryLayout.setVisibility(View.GONE);

            }
            if (null != identifiedUserEnteredCardDetails && null != identifiedUserEnteredCardDetails.getCardImage()) {
                LoadImageFromWebOperations(identifiedUserEnteredCardDetails.getCardImage());
            }
            Logger.Log("prepopulate EDIT PH NUM"
                    + editCardDetails.getPhoneNumber());
        }
    }

    private void intViews() {
        mDoneButton = (Button) findViewById(R.id.doneBtn);
        edtProfilecreditCardNickname = (EditText) findViewById(R.id.profile_card_nick_name);
        edtProfilenameOnCard = (EditText) findViewById(R.id.profile_card_name);
        edtProfilecreditCardNumber = (EditText) findViewById(R.id.profile_card_number);
        edtProfilefirstName = (EditText) findViewById(R.id.profile_f_name);
        edtProfilelastName = (EditText) findViewById(R.id.profile_l_name);
        edtProfilephoneNumber = (EditText) findViewById(R.id.profile_phone);
        edtProfileaddress1 = (EditText) findViewById(R.id.profile_al_1);
        edtProfileaddress2 = (EditText) findViewById(R.id.profile_a2_1);
        edtProfilecity = (EditText) findViewById(R.id.profile_city);
        edtProfilezip = (EditText) findViewById(R.id.profile_zip_code);
        edtProfilenickname = (EditText) findViewById(R.id.profile_n_name);
        spProfileExpirationMonth = (Spinner) findViewById(R.id.profile_card_exp_month);
        spProfileExpirationYear = (Spinner) findViewById(R.id.profile_card_exp_year);
        spState = (Spinner) findViewById(R.id.profile_stateSpinner);
        mainLayout = (LinearLayout) findViewById(R.id.new_credit_card_details_profile);
        loadingLayout = (FrameLayout) findViewById(R.id.profileAddCardloadingDialog);
        setDefault = (CheckBox) findViewById(R.id.setDefaultcard);
        Drawable drawable = getResources().getDrawable(
                R.drawable.beauty_pref_check_box);
        drawable.setBounds(0, 0, 60, 60);
        setDefault.setButtonDrawable(android.R.color.transparent);
        setDefault.setCompoundDrawables(drawable, null, null, null);
        setDefault.setCompoundDrawablePadding(15);
        setDefault.setPadding(15, 25, 3, 10);
        mDoneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Logger.Log(">>>>>>>>>>>>>>>>>>>>>>>>> In ondoneClicked");
                getAllValues();
                if (isValidationSuccess(identifiedUserEnteredCardDetails)) {
                    Logger.Log(">>>>>>>>> After validation success");
                    pd.show();
                    if (null != editCardDetails) {
                        invokeEditCreditCardDetails();
                    } else {
                        invokeAddnewCreditCardDetails();
                    }

                    Logger.Log(">>>>>>Inside Invoke Method");
                }
            }
        });

        nickNameErrorText = (TextView) findViewById(R.id.nickNameErrorText);
        nameErrorText = (TextView) findViewById(R.id.nameErrorText);
        cardNumberErrorText = (TextView) findViewById(R.id.cardNumberErrorText);
        expiryMonthErrorText = (TextView) findViewById(R.id.expiryMonthErrorText);
        expiryYearErrorText = (TextView) findViewById(R.id.expiryYearErrorText);
        firstnameErrorText = (TextView) findViewById(R.id.firstnameErrorText);
        profilenickNameErrorText = (TextView) findViewById(R.id.profilenickNameErrorText);
        lastNameErrorText = (TextView) findViewById(R.id.lastNameErrorText);
        phoneErrorText = (TextView) findViewById(R.id.phoneErrorText);
        address1ErrorText = (TextView) findViewById(R.id.address1ErrorText);
        cityErrorText = (TextView) findViewById(R.id.cityErrorText);
        stateErrorText = (TextView) findViewById(R.id.stateErrorText);
        zipErrorText = (TextView) findViewById(R.id.zipErrorText);
        originalDrawable = edtProfilecreditCardNickname.getBackground();
        edtProfilecreditCardNickname.addTextChangedListener(this);
        edtProfilenameOnCard.addTextChangedListener(this);
        edtProfilecreditCardNumber.addTextChangedListener(this);
        edtProfilefirstName.addTextChangedListener(this);
        edtProfilenickname.addTextChangedListener(this);
        edtProfilelastName.addTextChangedListener(this);
        edtProfilephoneNumber.addTextChangedListener(this);
        edtProfileaddress1.addTextChangedListener(this);
        edtProfilecity.addTextChangedListener(this);
        edtProfilezip.addTextChangedListener(this);
        mExpiryLayout = (LinearLayout) findViewById(R.id.ExpiryLayout);
        mAcceptedCardTypesLayout= (LinearLayout) findViewById(R.id.acceptedCardTypesLayout);
        mCardImageLayout= (LinearLayout) findViewById(R.id.cardImageLayout);
        addAcceptedCardImages();
    }

    private void addAcceptedCardImages()
    {
        mCardImageLayout.removeAllViews();
        CreditCardInfoBean singleCardInfo = null;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo && null!=singleCardInfo.getCardImage()) {
                        ImageView cardImage=new ImageView(AddCreditCardActivity.this);
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                                        FrameLayout.LayoutParams.WRAP_CONTENT);
                        cardImage.setLayoutParams(params);
                        cardImage .setPadding(0, 0, 20, 0);
                        new AQuery(cardImage).image(singleCardInfo.getCardImage(), true, false, 200,
                                R.drawable.creditcard_default, null, AQuery.FADE_IN);
                        mCardImageLayout.addView(cardImage);

                    }
                }
            }
        }
    }

    private void setAdaptersAndListeners() {
        cardType = new ArrayList<String>();
        cardType.add("VISA");
        cardType.add("MasterCard");
        cardType.add("DISCOVER");
        cardType.add("AmericanExpress");

        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("Name", "VISA");
        map.put("Icon", R.drawable.visa);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "MasterCard");
        map.put("Icon", R.drawable.mastercard);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "DISCOVER");
        map.put("Icon", R.drawable.discover);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("Name", "AmericanExpress");
        map.put("Icon", R.drawable.americanexpress);
        list.add(map);


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

        // MySpinnerAdapter1 spinnerSrrayAdapter = new MySpinnerAdapter1(
        // AddCreditCardActivity.this, R.layout.state_list,
        // R.id.textState, result1);
        //
        // spProfileExpirationMonth.setAdapter(spinnerSrrayAdapter);
        //
        setUpStateSpinner(spProfileExpirationMonth, result1);
        // 3.2 release
        if (expiryMonthLocation >= 0) {
            spProfileExpirationMonth.setSelection(expiryMonthLocation - 1);
        }
        spProfileExpirationMonth
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                        expiryMonthErrorText.setVisibility(View.GONE);
                        // if (Integer.valueOf(pos + 1) < 10) {
                        // strProfileExpirationMonth = "0"
                        // + Integer.valueOf(pos + 1).toString();
                        //
                        // } else
                        // strProfileExpirationMonth = Integer
                        // .valueOf(pos + 1).toString();
                        if (pos != 0) {
                            /* isSpinnerSelectedMonth = true; */
                            expiryMonthLocation = pos;
                            if (Integer.valueOf(pos) < 10) {
                                strProfileExpirationMonth = "0"
                                        + Integer.valueOf(pos).toString();
                            } else
                                strProfileExpirationMonth = Integer
                                        .valueOf(pos).toString();
                        } else {

                            expiryMonthLocation = pos;
                            strProfileExpirationMonth = "Select Expiry Month";
                        }

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        result2 = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        result2.add("Select Expiry Year");
        for (int i = year; i <= year + 12; i++) {
            result2.add("" + i);
        }

        // MySpinnerAdapter1 spinnerSrrayAdapter1 = new MySpinnerAdapter1(
        // AddCreditCardActivity.this, R.layout.state_list,
        // R.id.textState, result2);
        //
        // spProfileExpirationYear.setAdapter(spinnerSrrayAdapter1);

        setUpStateSpinner(spProfileExpirationYear, result2);
        for (int k = 0; k < result2.size(); k++) {
            if (null != expirationYear && result2.get(k).equals(expirationYear)) {
                spProfileExpirationYear.setSelection(k);
                break;
            }
        }
        spProfileExpirationYear
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int pos, long id) {
                        expiryYearErrorText.setVisibility(View.GONE);
                        strProfileExpirationYear = parent
                                .getItemAtPosition(pos).toString();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

    }

    // --------------------------

    @Override
    public void onDoneClicked() {
        Logger.Log(">>>>>>>>>>>>>>>>>>>>>>>>> In ondoneClicked");
        getAllValues();
        if (isValidationSuccess(identifiedUserEnteredCardDetails)) {
            Logger.Log(">>>>>>>>> After validation success");
            pd.show();
            if (null != editCardDetails) {
                invokeEditCreditCardDetails();
            } else {
                invokeAddnewCreditCardDetails();
            }

            Logger.Log(">>>>>>Inside Invoke Method");
        }
    }

	/*
     * service for adding credit card
	 */

    protected void invokeAddnewCreditCardDetails() {
        serviceCall="addCard";
        InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.ADD_NEW_CREDIT_CARD_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        // invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateAddCreditCardDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
        RetrieveAddCreditCardDetailsHandler retrieveAddCreditCardDetailsHandlerParameters = new RetrieveAddCreditCardDetailsHandler();
        invokerParams
                .setUltaHandler(retrieveAddCreditCardDetailsHandlerParameters);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddCreditCardactivity><invokeAddnewCreditCardDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate payment method details handler parameters.
     *
     * @return the map
     */
    private Map<String, String> populateAddCreditCardDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "TRUE");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("editValue.creditCardNickname",
                strProfilecreditCardNickname.toUpperCase());
        urlParams.put("editValue.creditCardType", strProfilecreditCardType);
        urlParams.put("editValue.nameOnCard", strProfilenameOnCard);
        urlParams.put("editValue.creditCardNumber", strProfilecreditCardNumber);
        urlParams.put("editValue.expirationMonth", strProfileExpirationMonth);
        urlParams.put("editValue.expirationYear", strProfileExpirationYear);
        urlParams.put("billAddrValue.nickname",
                strProfilenickname.toUpperCase());
        urlParams.put("billAddrValue.firstName", strProfilefirstName);
        urlParams.put("billAddrValue.lastName", strProfilelastName);
        urlParams.put("billAddrValue.address1", strProfileaddress1);
        urlParams.put("billAddrValue.address2", strProfileaddress2);
        urlParams.put("billAddrValue.city", strProfilecity);
        urlParams.put("billAddrValue.state", state);
        urlParams.put("billAddrValue.phoneNumber", phone1);
        urlParams.put("billAddrValue.postalCode", strProfilezip);
        urlParams.put("billAddrValue.country", "US");
        urlParams.put("zip", strProfilezip);
        urlParams.put("billAddrValue.shippingAddrNickname", "NEW");
        urlParams.put("state", state);
        urlParams.put("country", "US");
        urlParams.put("primaryCreditCardStatus", "pCCDefault");
        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class RetrieveAddCreditCardDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveAddCreditCardDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(AddCreditCardActivity.this);

                } else {
                    try {
                        notifyUser(Utility.formatDisplayError(getErrorMessage()),
                                AddCreditCardActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                Logger.Log("<RetrieveAddCreditCardDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                paymentMethodBean = (PaymentMethodBean) getResponseBean();
                if (null != paymentMethodBean
                        && null != paymentMethodBean.getErrorInfos()) {
                    try {
                        notifyUser(Utility.formatDisplayError(paymentMethodBean
                                        .getErrorInfos().get(0)),
                                AddCreditCardActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                } else if (null != paymentMethodBean
                        && null == paymentMethodBean.getErrorInfos()) {
                    if (setasDefault.equalsIgnoreCase("true")) {
                        pd.show();

                        invokeSetasDefaultCreditCardDetails();
                    } else {
                        finish();
                    }
                }
            }
        }
    }

	/*
     * service for adding credit card
	 */

    protected void invokeSetasDefaultCreditCardDetails() {
        serviceCall="setDefault";
        InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.SET_AS_DEFAULT_CREDIT_CARD_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateSetasDefaultCreditCardDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
        RetrieveSetasDefaultCreditCardDetailsHandler retrieveSetasDefaultCreditCardDetailsHandlerParameters = new RetrieveSetasDefaultCreditCardDetailsHandler();
        invokerParams
                .setUltaHandler(retrieveSetasDefaultCreditCardDetailsHandlerParameters);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<AddCreditCardactivity><invokeSetasDefaultCreditCardDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate payment method details handler parameters.
     *
     * @return the map
     */
    private Map<String, String> populateSetasDefaultCreditCardDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "0");
        urlParams
                .put("defaultCard", strProfilecreditCardNickname.toUpperCase());

        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class RetrieveSetasDefaultCreditCardDetailsHandler extends
            UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {

            Logger.Log("<RetrieveSetasDefaultSetasDefaultCreditCardDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    // finish();
                    askRelogin(AddCreditCardActivity.this);
                } else {
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                AddCreditCardActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                Logger.Log("<RetrieveSetasDefaultSetasDefaultCreditCardDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                paymentMethodBean = (PaymentMethodBean) getResponseBean();
                if (null != paymentMethodBean) {

                    Logger.Log("<AddCreitCardActivity>" + "BeanPopulated");

                    finish();

                }
            }
        }
    }

    /**
     * Gets the all values.
     *
     * @return the all values
     */
    private void getAllValues() {
        strProfilenameOnCard = edtProfilenameOnCard.getText().toString().trim();
        strProfilecreditCardNickname = edtProfilecreditCardNickname.getText()
                .toString().trim();

        strProfilenameOnCard = edtProfilenameOnCard.getText().toString().trim();
        if (edtProfilecreditCardNumber.getText().toString().contains("*")) {
            strProfilecreditCardNumber = editCardDetails.getCreditCardNumber();
        } else {
            strProfilecreditCardNumber = edtProfilecreditCardNumber.getText()
                    .toString().trim();
        }/*
         * strProfilecreditCardNumber = edtProfilecreditCardNumber.getText()
		 * .toString().trim();
		 */
        strProfilefirstName = edtProfilefirstName.getText().toString().trim();
        strProfilelastName = edtProfilelastName.getText().toString().trim();
        strProfilephoneNumber = edtProfilephoneNumber.getText().toString()
                .trim();

        strProfileaddress1 = edtProfileaddress1.getText().toString().trim();
        strProfileaddress2 = edtProfileaddress2.getText().toString().trim();
        strProfilecity = edtProfilecity.getText().toString().trim();
        strProfilezip = edtProfilezip.getText().toString().trim();
        strProfilenickname = edtProfilenickname.getText().toString().trim();
        state = spState.getSelectedItem().toString();
        if (!state.equalsIgnoreCase("* Select state")) {
            state = state.substring(0, 2);
        }

        Logger.Log(">>>>>>>>>>>>>>>>>>>>>" + strProfilenameOnCard + " \n "
                + strProfilecreditCardNickname + " \n " + strProfilenameOnCard
                + " \n " + strProfilecreditCardNumber + " \n "
                + strProfilefirstName + " \n " + strProfilelastName + " \n "
                + phone1 + " \n " + strProfileaddress1 + "--"
                + strProfileaddress2 + " \n " + strProfilecity + " \n "
                + strProfilezip + " \n " + strProfilenickname + " \n "
                + strProfilecountry + " \n " + strProfilecreditCardType
                + " \n " + strProfileExpirationMonth + " \n "
                + strProfileExpirationYear + " \n " + state);

    }

    private class MySpinnerAdapter1 extends ArrayAdapter {

        /**
         * Instantiates a new my spinner adapter.
         *
         * @param context            the context
         * @param resource           the resource
         * @param textViewResourceId the text view resource id
         * @param result             the result
         */
        @SuppressWarnings("unchecked")
        public MySpinnerAdapter1(Context context, int resource,
                                 int textViewResourceId, List<String> result) {

            super(context, resource, textViewResourceId, result);
        }
    }

    /**
     * Setup city spinner.
     */

    private void invokeStateList() {
        InvokerParams<StateListBean> invokerParams = new InvokerParams<StateListBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.STATE_LIST_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateStatListParameters());
        invokerParams.setUltaBeanClazz(StateListBean.class);
        StatListHandler userCreationHandler = new StatListHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<StatList><invokeStatList><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateStatListParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();

        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "2");

        return urlParams;
    }

    /**
     * The Class StatListHandler.
     */
    public class StatListHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<UserCreationHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            loadingLayout.setVisibility(View.GONE);
            if (null != getErrorMessage()) {
                notifyUser(Utility.formatDisplayError(getErrorMessage()),
                        AddCreditCardActivity.this);
            } else {
                Logger.Log("<StatList><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                loadingLayout.setVisibility(View.GONE);
                stateListBean = (StateListBean) getResponseBean();
                result = (ArrayList<String>) stateListBean.getStateList();
                List<String> resultNew = new ArrayList<String>();
                resultNew.add("* Select state");
                resultNew.addAll(result);
                setUpStateSpinner(spState, resultNew);
                UltaDataCache.getDataCacheInstance().setStateList(result);
            }
        }
    }

    /**
     * Checks if is validation success.
     *
     * @return true, if is validation success
     */
    private boolean isValidationSuccess(CreditCardInfoBean userEnteredCardDetails) {
        phone1 = Utility.formatPhoneNumber(strProfilephoneNumber);
        //If card type is identified for the user entered card number
        int cardMaxlength = 0,cardMinLength=0;
        boolean cardMinAndMaxSame=false;
        if (null != userEnteredCardDetails) {
            //get maxlength of card number for identified card type
            if (null != userEnteredCardDetails.getCardMaxCVVLength()) {
                try {
                    cardMaxlength = Integer.parseInt(userEnteredCardDetails.getCardMaxNumberLength());
                    cardMinLength=Integer.parseInt(userEnteredCardDetails.getCardMinNumberLength());
                    if(cardMaxlength==cardMinLength)
                    {
                        cardMinAndMaxSame=true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            //Check if card holder's nick name is entered
            if (null == strProfilecreditCardNickname
                    || strProfilecreditCardNickname.equals(" ")
                    || strProfilecreditCardNickname.length() == 0) {
                setError(edtProfilecreditCardNickname, nickNameErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NICK_NAME);
                edtProfilecreditCardNickname.requestFocus();
                return false;
            }
            //Check if card holder's name is entered
            else if (null == strProfilenameOnCard
                    || strProfilenameOnCard.equals(" ")
                    || strProfilenameOnCard.length() == 0) {
                setError(edtProfilenameOnCard, nameErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NAME);
                edtProfilenameOnCard.requestFocus();
                return false;
            }
            //Check if card number is entered
            else if (null == strProfilecreditCardNumber
                    || strProfilecreditCardNumber.equals(" ")
                    || strProfilecreditCardNumber.length() == 0) {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,
                        WebserviceConstants.ENTER_CARD_NUMBER);
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
            else if(!isEditCardPage &&cardMinAndMaxSame&&strProfilecreditCardNumber.length()!=cardMaxlength)
            {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,"The card number must be "+cardMaxlength+" digits");
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
            else if(!isEditCardPage &&!cardMinAndMaxSame &&strProfilecreditCardNumber.length() > cardMaxlength)
            {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,"Card number must be "+cardMaxlength+" digits or lesser.");
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
            else if (!isEditCardPage &&!cardMinAndMaxSame &&strProfilecreditCardNumber.length()<cardMinLength) {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,"Card number must be "+cardMinLength+" digits or greater.");
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
            //Check if expiry is required and expiry month is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && (null == strProfileExpirationMonth
                    || strProfileExpirationMonth.equals(" ")
                    || strProfileExpirationMonth.length() == 0)) {
                expiryMonthErrorText.setText("Select Expiration Month ");
                expiryMonthErrorText.setVisibility(View.VISIBLE);
                spProfileExpirationMonth.requestFocus();
                return false;
            }
            //Check if expiry is required and expiry month is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && (strProfileExpirationMonth
                    .equalsIgnoreCase("Select Expiry Month"))) {
                expiryMonthErrorText.setText("Select Expiration Month ");
                expiryMonthErrorText.setVisibility(View.VISIBLE);
                spProfileExpirationMonth.requestFocus();
                return false;
            }
            //Check if expiry is required and expiry year is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && (null == strProfileExpirationYear
                    || strProfileExpirationYear.equals(" ")
                    || strProfileExpirationYear.length() == 0)) {
                expiryYearErrorText.setText("Select Expiration Year  ");
                expiryYearErrorText.setVisibility(View.VISIBLE);
                spProfileExpirationYear.requestFocus();
                return false;
            }
            //Check if expiry is required and expiry year is selected
            else if (userEnteredCardDetails.getCardUsesExpirationDate() && (strProfileExpirationYear
                    .equalsIgnoreCase("Select Expiry Year"))) {
                expiryYearErrorText.setText("Select Expiration Year  ");
                expiryYearErrorText.setVisibility(View.VISIBLE);
                spProfileExpirationYear.requestFocus();
                return false;
            } else if (strProfilefirstName.equalsIgnoreCase("")
                    || strProfilefirstName == null) {
                setError(edtProfilefirstName, firstnameErrorText,
                        "Fill the First Name");
                edtProfilefirstName.requestFocus();
                return false;
            } else if (strProfilefirstName.startsWith(" ")) {
                setError(edtProfilefirstName, firstnameErrorText,
                        "First Name can not start with a space");
                edtProfilenickname.requestFocus();
                return false;
            } else if ((!setStateIfEditCard) && (strProfilenickname.equalsIgnoreCase("")
                    || strProfilenickname == null)) {
                setError(edtProfilenickname, profilenickNameErrorText,
                        "Fill the Nickname");
                edtProfilenickname.requestFocus();
                return false;
            } else if (strProfilenickname.startsWith(" ")) {
                setError(edtProfilenickname, profilenickNameErrorText,
                        "Nickname can not start with a space");
                edtProfilenickname.requestFocus();
                return false;
            } else if (strProfilelastName.equalsIgnoreCase("")
                    || strProfilelastName == null) {
                setError(edtProfilelastName, lastNameErrorText,
                        "Fill the Last Name");
                edtProfilelastName.requestFocus();
                return false;
            } else if (strProfilelastName.startsWith(" ")) {
                setError(edtProfilelastName, lastNameErrorText,
                        "Last Name can not start with a space");
                edtProfilelastName.requestFocus();
                return false;
            } else if (strProfilephoneNumber.equalsIgnoreCase("")
                    || strProfilephoneNumber == null
                    || strProfilephoneNumber.startsWith(" ")
                    || strProfilephoneNumber.length() < 10) {
                setError(edtProfilephoneNumber, phoneErrorText,
                        "Fill the 10 digit Phone Number ");
                edtProfilephoneNumber.requestFocus();
                return false;

            } else if (strProfileaddress1.equalsIgnoreCase("")
                    || strProfileaddress1 == null) {
                setError(edtProfileaddress1, address1ErrorText,
                        "Fill the Address Line 1 ");
                edtProfileaddress1.requestFocus();
                return false;
            } else if (strProfileaddress1.startsWith(" ")) {
                setError(edtProfileaddress1, address1ErrorText,
                        "Address Line1 can not start with a space");
                edtProfileaddress1.requestFocus();
                return false;
            } else if (strProfilecity.equalsIgnoreCase("") || strProfilecity == null) {
                setError(edtProfilecity, cityErrorText, "Fill the City");
                edtProfilecity.requestFocus();
                return false;
            } else if (strProfilecity.startsWith(" ")) {
                setError(edtProfilecity, cityErrorText,
                        "City can not start with a space");
                edtProfilecity.requestFocus();
                return false;
            } else if (state.equalsIgnoreCase("") || state == null) {
                stateErrorText.setText("Select the State ");
                stateErrorText.setVisibility(View.VISIBLE);
                spState.requestFocus();
                return false;
            } else if (state.equalsIgnoreCase("* Select state")) {
                stateErrorText.setText("Select the State ");
                stateErrorText.setVisibility(View.VISIBLE);
                spState.requestFocus();
                return false;
            } else if (strProfilezip.equalsIgnoreCase("") || strProfilezip == null
                    || strProfilezip.startsWith(" ")) {
                setError(edtProfilezip, zipErrorText, "Fill the Zipcode properly");
                edtProfilezip.requestFocus();
                return false;
            } else if (strProfilezip.length() < 5) {
                setError(edtProfilezip, zipErrorText,
                        "Zip Code cannot be less than 5 letters");
                edtProfilezip.requestFocus();
                return false;
            } else if (strProfilezip.length() > 5) {
                setError(edtProfilezip, zipErrorText,
                        "Zip Code cannot be more than 5 letters");
                edtProfilezip.requestFocus();
                return false;
            }

            //validate credit card exp date
            if (validateCardExp(spProfileExpirationMonth.getSelectedItemPosition(), spProfileExpirationYear.getSelectedItem().toString())) {
                expiryMonthErrorText.setText("Please verify your credit card expiration month and re-enter");
                expiryMonthErrorText.setVisibility(View.VISIBLE);
                spProfileExpirationMonth.requestFocus();
                return false;
            }
            //If expiration date is not required for card
            if (!userEnteredCardDetails.getCardUsesExpirationDate()) {
                strProfileExpirationMonth = "";
                strProfileExpirationYear = "";
                spProfileExpirationMonth.setSelection(0);
                spProfileExpirationYear.setSelection(0);
            }
        } else {
            //Check if card holder's nick name is entered
            if (null == strProfilecreditCardNickname
                    || strProfilecreditCardNickname.equals(" ")
                    || strProfilecreditCardNickname.length() == 0) {
                setError(edtProfilecreditCardNickname, nickNameErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NICK_NAME);
                edtProfilecreditCardNickname.requestFocus();
                return false;
            }
            //Check if card holder's name is entered
            else if (null == strProfilenameOnCard
                    || strProfilenameOnCard.equals(" ")
                    || strProfilenameOnCard.length() == 0) {
                setError(edtProfilenameOnCard, nameErrorText,
                        WebserviceConstants.ENTER_CARD_HOLDER_NAME);
                edtProfilenameOnCard.requestFocus();
                return false;
            }
            //Check if card number is entered
            else if (null == strProfilecreditCardNumber
                    || strProfilecreditCardNumber.equals(" ")
                    || strProfilecreditCardNumber.length() == 0) {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,
                        WebserviceConstants.ENTER_CARD_NUMBER);
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
            //for edit card we don't need card number validation as number field is non editable
            else if (!isEditCardPage) {
                setError(edtProfilecreditCardNumber, cardNumberErrorText,
                        WebserviceConstants.INVALID_CREDIT_CARD);
                edtProfilecreditCardNumber.requestFocus();
                return false;
            }
        }
        return true;
    }

    /*
      Validate Guest select credit card date is expired with device date
     */
    private boolean validateCardExp(int month, String year) {
        String expMonth = "";
        if (Integer.valueOf(month) < 10) {
            expMonth = "0"
                    + Integer.valueOf(month).toString();
        } else
            expMonth = Integer
                    .valueOf(month).toString();

        boolean expired = false;
        String input = expMonth + "/" + year;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yyyy");
        simpleDateFormat.setLenient(false); //to  strictly matches data obj pattern
        try {
            Date expiry = simpleDateFormat.parse(input);
            expired = expiry.before(adjustMonth(new Date(), 1)); // to avoid current month not as show expired
        } catch (ParseException ultaException) {
            Logger.Log("<EditCreditCardactivity><validateCardExp()><Parse UltaException>>"
                    + ultaException);
        } catch (Exception ultaException) {
            Logger.Log("<EditCreditCardactivity><validateCardExp()><UltaException>>"
                    + ultaException);
        }
        return expired;
    }

    public static Date adjustMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -i);
        return cal.getTime();
    }

    protected void invokeEditCreditCardDetails() {
        serviceCall="editCard";
        InvokerParams<PaymentMethodBean> invokerParams = new InvokerParams<PaymentMethodBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.EDIT_CREDIT_CARD_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        // invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateEditCreditCardDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(PaymentMethodBean.class);
        RetrieveEditCreditCardDetailsHandler retrieveEditCreditCardDetailsHandlerParameters = new RetrieveEditCreditCardDetailsHandler();
        invokerParams
                .setUltaHandler(retrieveEditCreditCardDetailsHandlerParameters);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<EditCreditCardactivity><invokeEditCreditCardDetails()><UltaException>>"
                    + ultaException);

        }

    }

    /**
     * Populate payment method details handler parameters.
     *
     * @return the map
     */
    private Map<String, String> populateEditCreditCardDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "0");
        urlParams.put("editValue.nickname", editCardDetails.getNickName());
        urlParams.put("editValue.creditCardType", strProfilecreditCardType);
        urlParams.put("editValue.nameOnCard", strProfilenameOnCard);
        urlParams.put("editValue.creditCardNumber", strProfilecreditCardNumber);
        urlParams.put("editValue.expirationMonth", strProfileExpirationMonth);
        urlParams.put("editValue.expirationYear", strProfileExpirationYear);
        /*urlParams.put("billAddrValue.nickname", strProfilenickname);*/
        urlParams.put("billAddrValue.firstName", strProfilefirstName);
        urlParams.put("billAddrValue.lastName", strProfilelastName);
        urlParams.put("billAddrValue.address1", strProfileaddress1);
        urlParams.put("billAddrValue.address2", strProfileaddress2);
        urlParams.put("billAddrValue.city", strProfilecity);
        urlParams.put("billAddrValue.state", state);
        urlParams.put("billAddrValue.phoneNumber", phone1);
        urlParams.put("billAddrValue.postalCode", strProfilezip);
        urlParams.put("billAddrValue.country", "US");
        urlParams.put("zip", strProfilezip);
        urlParams.put("editValue.newNickname",
                strProfilecreditCardNickname.toUpperCase());
        urlParams.put("primaryCreditCardStatus", "pCCDefault");
        return urlParams;
    }

    /**
     * The Class RetrievePaymentDetailsHandler.
     */
    public class RetrieveEditCreditCardDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveEditCreditCardDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    // finish();
                    askRelogin(AddCreditCardActivity.this);
                } else {
                    try {
                        notifyUser(Utility.formatDisplayError(getErrorMessage()),
                                AddCreditCardActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {

                Logger.Log("<RetrieveEditCreditCardDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                paymentMethodBean = (PaymentMethodBean) getResponseBean();
                if (null != paymentMethodBean) {

                    Logger.Log("<EditCardDetailsActivity>" + "BeanPopulated");
                    if (setasDefault.equalsIgnoreCase("true")) {
                        pd.show();
                        invokeSetasDefaultCreditCardDetails();
                    } else {
                        finish();
                    }

                }
            }
        }
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
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            if( serviceCall.equalsIgnoreCase("setDefault")) {
                invokeSetasDefaultCreditCardDetails();
            }
            else if(serviceCall.equalsIgnoreCase("addCard"))
            {
                invokeAddnewCreditCardDetails();
            }
            else if(serviceCall.equalsIgnoreCase("editCard"))
            {
                invokeEditCreditCardDetails();
            }
        } else {
            if (pd != null && pd.isShowing()) {
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
        if (s.hashCode() == edtProfilecreditCardNickname.getText().hashCode()) {
            edtProfilecreditCardNickname
                    .setBackgroundDrawable(originalDrawable);
            nickNameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilenameOnCard.getText().hashCode()) {
            edtProfilenameOnCard.setBackgroundDrawable(originalDrawable);
            nameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilecreditCardNumber.getText()
                .hashCode()) {
            edtProfilecreditCardNumber.setBackgroundDrawable(originalDrawable);
            cardNumberErrorText.setVisibility(View.GONE);
            checkCreditCardTypeExpirationAndCVV(s.toString().trim());
        } else if (s.hashCode() == edtProfilefirstName.getText().hashCode()) {
            edtProfilefirstName.setBackgroundDrawable(originalDrawable);
            firstnameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilenickname.getText().hashCode()) {
            edtProfilenickname.setBackgroundDrawable(originalDrawable);
            profilenickNameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilelastName.getText().hashCode()) {
            edtProfilelastName.setBackgroundDrawable(originalDrawable);
            lastNameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilephoneNumber.getText().hashCode()) {
            edtProfilephoneNumber.setBackgroundDrawable(originalDrawable);
            phoneErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfileaddress1.getText().hashCode()) {
            edtProfileaddress1.setBackgroundDrawable(originalDrawable);
            address1ErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilecity.getText().hashCode()) {
            edtProfilecity.setBackgroundDrawable(originalDrawable);
            cityErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == edtProfilezip.getText().hashCode()) {
            edtProfilezip.setBackgroundDrawable(originalDrawable);
            zipErrorText.setVisibility(View.GONE);
        }
        changeAllEditTextBackground();
    }

    public void changeAllEditTextBackground() {
        changeEditTextBackground(edtProfileaddress1);
        changeEditTextBackground(edtProfileaddress2);
        changeEditTextBackground(edtProfilecity);
        changeEditTextBackground(edtProfilecreditCardNickname);
        changeEditTextBackground(edtProfilecreditCardNumber);
        changeEditTextBackground(edtProfilefirstName);
        changeEditTextBackground(edtProfilelastName);
        changeEditTextBackground(edtProfilephoneNumber);
        changeEditTextBackground(edtProfilezip);
        changeEditTextBackground(edtProfilenameOnCard);
    }

    public void setError(EditText editText, TextView errorTV, String message) {
        errorTV.setText("" + message);
        errorTV.setVisibility(View.VISIBLE);
        changeAllEditTextBackground();
        editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
    }

    public void setUpStateSpinner(final Spinner stateSpinner,
                                  final List<String> anArrayOfStrings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, anArrayOfStrings);

        stateSpinner.setAdapter(adapter);
        stateSpinner
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {

                        int position = stateSpinner.getSelectedItemPosition();
                        if (position != 0) {
                            isSpinnerSelected = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {
                stateErrorText.setVisibility(View.GONE);
                ((TextView) parentView.getChildAt(0))
                        .setTextColor(getResources().getColor(R.color.black));
                ((TextView) parentView.getChildAt(0)).setTextSize(12);
                ((TextView) parentView.getChildAt(0)).setPadding(5, 0, 0, 0);
                if (position != 0) {
                    isSpinnerSelected = true;
                }
                // opcoNamePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        try {
            if (setStateIfEditCard) {
                for (int i = 0; i < anArrayOfStrings.size(); i++) {
                    if (anArrayOfStrings.get(i).startsWith(
                            editCardDetails.getState())) {
                        int positionOfStateSpinner = adapter
                                .getPosition(anArrayOfStrings.get(i));
                        spState.setSelection(positionOfStateSpinner);
                        break;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        strProfilecreditCardType = "";
        identifiedUserEnteredCardDetails = null;
        boolean cardTypeIdentified = false;
        if (null != identifyCardType(cardNumber)) {
            mAcceptedCardTypesLayout.setVisibility(View.GONE);
            CreditCardInfoBean identifiedCard = identifyCardType(cardNumber);
            identifiedUserEnteredCardDetails = identifiedCard;
            cardTypeIdentified = true;
            strProfilecreditCardType = identifiedCard.getCardType();
            isExpirationDateRequired = identifiedCard.getCardUsesExpirationDate();
            if (isExpirationDateRequired) {
                mExpiryLayout.setVisibility(View.VISIBLE);
            } else {
                mExpiryLayout.setVisibility(View.GONE);
            }
            LoadImageFromWebOperations(identifiedCard.getCardImage());
        }
        if (!cardTypeIdentified&&!isEditCardPage) {
            mAcceptedCardTypesLayout.setVisibility(View.VISIBLE);
            edtProfilecreditCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.creditcard_default), null);
            mExpiryLayout.setVisibility(View.VISIBLE);
        }
        if(cardNumber.isEmpty()||cardNumber.length()==0)
        {
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
            AQuery androidQuery = new AQuery(AddCreditCardActivity.this);
            androidQuery.ajax(url, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
                @Override
                public void callback(String url, Bitmap bitmap, AjaxStatus status) {
                    Drawable drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 62, false));
                    edtProfilecreditCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
                }
            });
        } catch (Exception e) {
            Logger.Log("<UltaException>>"
                    + e);
        }
    }

    /**
     * Check if the card requires expiration date//Edit card details pre populate function
     *
     * @param cardType
     */
    public boolean checkIfExpirationNeeded(String cardType) {
        CreditCardInfoBean singleCardInfo = null;
        boolean expirationNeeded = true;
        if (null != UltaDataCache.getDataCacheInstance().getCreditCardsInfo()) {
            List<CreditCardInfoBean> creditCardsInfo = UltaDataCache.getDataCacheInstance().getCreditCardsInfo();
            if (null != creditCardsInfo && !creditCardsInfo.isEmpty()) {
                for (int i = 0; i < creditCardsInfo.size(); i++) {
                    singleCardInfo = creditCardsInfo.get(i);
                    if (null != singleCardInfo && cardType.equalsIgnoreCase(singleCardInfo.getCardType())) {
                        identifiedUserEnteredCardDetails = singleCardInfo;
                        if (!singleCardInfo.getCardUsesExpirationDate()) {
                            expirationNeeded = false;
                        }
                    }
                }
            }
        }
        return expirationNeeded;
    }
}
