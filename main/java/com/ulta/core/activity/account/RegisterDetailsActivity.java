/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.checkout.AddShippingAddressLogginUserActivity;
import com.ulta.core.activity.product.FreeSamplesActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.account.StateListBean;
import com.ulta.core.bean.checkout.CheckoutShippmentMethodBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
import com.ulta.core.conf.types.HttpProtocol;
import com.ulta.core.net.WebserviceUtility;
import com.ulta.core.net.executor.ExecutionDelegator;
import com.ulta.core.net.handler.UltaHandler;
import com.ulta.core.net.invoker.InvokerParams;
import com.ulta.core.sessiontimeout.interfaces.OnSessionTimeOut;
import com.ulta.core.util.ConversantUtility;
import com.ulta.core.util.UltaException;
import com.ulta.core.util.Utility;
import com.ulta.core.util.caching.UltaDataCache;
import com.ulta.core.util.log.Logger;
import com.ulta.core.widgets.flyin.OnDoneClickedListener;
import com.urbanairship.UAirship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;
import static com.ulta.core.util.Utility.validateEmail;

/**
 * The Class RegisterDetailsActivity.
 */
public class RegisterDetailsActivity extends UltaBaseActivity implements
        OnDoneClickedListener, OnSessionTimeOut, TextWatcher {
    private String[] anArrayOfStrings;
    private boolean isSpinnerSelected = false;
    private boolean member = false;
    private boolean checked = true;
    private boolean sign = true;
    private String optIn = "TRUE";
    private boolean isLoyaltyAccountOpted = true;
    private SharedPreferences staySignedInSharedPreferences;

    private Editor staySignedInEditor;
    /**
     * The edit lastname.
     */
    private EditText editEmail, editConfirmEmail, editPassword,
            editReenterpassword, editFirstname, editLastname;

    /**
     * The edit id.
     */
    private EditText editAddress1, editAddress2, editCity, editZipcode, editId,
            editDateOfBirth, editphoneNumber;

    /**
     * The sp state.
     */
    private Spinner spState;

    /**
     * The btn continue.
     */
    private Button btnContinue;

    /** The radio member. */
//    private RadioButton radioSign, radioMember, radioThanks;

    /**
     * The lastname.
     */
    private String email, confirmEmail, password, reenterpassword, firstname,
            lastname;

    /**
     * The state.
     */
    private String address1, address2, city, zipcode, state, dateofbirth,
            phone1;

    /**
     * The result.
     */
    private List<String> result;

    // private static String EMAIL_LENGTH_VALIDATION_TITLE =
    // "User Name is Mandatory";
    /**
     * The email validation message.
     */
    private static String EMAIL_VALIDATION_MESSAGE = "Please enter valid Email";

    private static String CONFIRM_EMAIL_VALIDATION_MESSAGE = "Supplied email ids do not match";

    // private static String PASSWORD_LENGTH_VALIDATION_TITLE =
    // "Password is Mandatory";
    /**
     * The password validation message.
     */
    private static String PASSWORD_VALIDATION_MESSAGE = "Please enter valid password";

    // private static String CONFIRM_PASSWORD_LENGTH_VALIDATION_TITLE =
    // "Confirm Password is Mandatory";
    /**
     * The confirm password validation message.
     */
    private static String CONFIRM_PASSWORD_VALIDATION_MESSAGE = "The supplied passwords do not match";

    // private static String FIRST_NAME_VALIDATION_TITLE =
    // "First name is Mandatory";
    /**
     * The first name validation message.
     */
    private static String FIRST_NAME_VALIDATION_MESSAGE = "Please enter valid first name";

    /**
     * The state validation message.
     */
    private static String STATE_VALIDATION_MESSAGE = "Please select a state";

    // private static String LAST_NAME_VALIDATION_TITLE =
    // "Last name is Mandatory";
    /**
     * The last name validation message.
     */
    private static String LAST_NAME_VALIDATION_MESSAGE = "Please enter valid last name";

    private static String DATE_OF_BIRTH_VALIDATION_MESSAGE = "Please enter valid date of birth";
    // private static String STREE_ADDRESS_VALIDATION_TITLE =
    // "Street address is Mandatory";
    /**
     * The street address validation message.
     */
    private static String STREET_ADDRESS_VALIDATION_MESSAGE = "Please enter valid street address";

    // private static String CITY_VALIDATION_TITLE = "City is Mandatory";
    /**
     * The city validation message.
     */
    private static String CITY_VALIDATION_MESSAGE = "Please enter a city";

    // private static String ZIP_CODE_VALIDATION_TITLE =
    // "Zip code is Mandatory";
    /**
     * The zip code validation message.
     */
    private static String ZIP_CODE_VALIDATION_MESSAGE = "Please enter a valid zip code";

    private static String PHONE_NUMBER_VALIDATION_MESSAGE = "Please enter valid phone number";

    private static String BEAUTY_CLUB_NUMBER_VALIDATION_MESSAGE = "Please enter Beauty Club Membership Id";

    /**
     * The origin.
     */
    private String origin, check;

    FrameLayout loadingDialog;

    private ProgressDialog pd;

    private int fromCehckout;
    TextView firstNameErrorText, lastNameErrorText, emailErrorText,
            confirm_emailErrorText, passwordErrorText, repasswordErrorText,
            phoneErrorText, address1ErrorText, cityErrorText, stateErrorText,
            zipErrorText, dateofbirthErrorText, idErrorText;
    Drawable originalDrawable;
    private static CheckoutShippmentMethodBean checkoutShippmentMethodBean;
    private String anonymousUserState = null;
    private boolean isFromPayPal;
    private LinearLayout membershipId;
    private Switch check_reward_switch, yes_sign_switch, already_a_member_switch;
    private byte[] loginPasswordBytes;
    private byte[] loginUserNameBytes;
    private String loginPassword;

    private String loginUsername;

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
     */
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerdetails);
        trackAppState(this, WebserviceConstants.REGISTER);
        setTitle("New Account");
        if (null != getIntent().getExtras()) {
            origin = getIntent().getExtras().getString("origin");
            fromCehckout = getIntent().getExtras().getInt("fromCehckout", 0);
            isFromPayPal = getIntent().getExtras().getBoolean("fromPayPal",
                    false);
            Logger.Log(">>>>>>>>fromCehckout in register: " + fromCehckout);
            check = getIntent().getExtras().getString("check");
            checkoutShippmentMethodBean = (CheckoutShippmentMethodBean) getIntent()
                    .getExtras().get("revieworder");
        }
        if (fromCehckout == 2) {
            disableMenu();
            trackAppAction(RegisterDetailsActivity.this,
                    WebserviceConstants.CHECKOUT_STEP_2_EVENT_ACTION);

            trackAppAction(RegisterDetailsActivity.this,
                    WebserviceConstants.CHECKOUT_STEP_2_VISIT_EVENT_ACTION);
        }
        editEmail = (EditText) findViewById(R.id.editEmail);
        editConfirmEmail = (EditText) findViewById(R.id.editConfirmEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editReenterpassword = (EditText) findViewById(R.id.editReenterpassword);
        editFirstname = (EditText) findViewById(R.id.editFirstname);
        editFirstname.requestFocus();
        editLastname = (EditText) findViewById(R.id.editLastname);
        editAddress1 = (EditText) findViewById(R.id.editAddress1);
        editAddress2 = (EditText) findViewById(R.id.editAddress2);
        editCity = (EditText) findViewById(R.id.editCity);
        editZipcode = (EditText) findViewById(R.id.editZip);
        editId = (EditText) findViewById(R.id.editId);
        membershipId = (LinearLayout) findViewById(R.id.membershipId);
        membershipId.setVisibility(View.GONE);
        check_reward_switch = (Switch) findViewById(R.id.check_reward_switch);
        yes_sign_switch = (Switch) findViewById(R.id.yes_sign_switch);
        already_a_member_switch = (Switch) findViewById(R.id.already_a_member_switch);
        editDateOfBirth = (EditText) findViewById(R.id.editDateOfBirth);
        editphoneNumber = (EditText) findViewById(R.id.phoneNumber);
//        radioMember = (RadioButton) findViewById(R.id.radioMember);
//        radioSign = (RadioButton) findViewById(R.id.radioSign);
//        radioThanks = (RadioButton) findViewById(R.id.radioThanks);
        loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
        spState = (Spinner) findViewById(R.id.spState);
        originalDrawable = editZipcode.getBackground();
        /**
         * Error messages text view
         */

        firstNameErrorText = (TextView) findViewById(R.id.firstNameErrorText);
        lastNameErrorText = (TextView) findViewById(R.id.lastNameErrorText);
        emailErrorText = (TextView) findViewById(R.id.emailErrorText);
        confirm_emailErrorText = (TextView) findViewById(R.id.confirm_emailErrorText);
        passwordErrorText = (TextView) findViewById(R.id.passwordErrorText);
        repasswordErrorText = (TextView) findViewById(R.id.repasswordErrorText);
        phoneErrorText = (TextView) findViewById(R.id.phoneErrorText);
        dateofbirthErrorText = (TextView) findViewById(R.id.dateofbirthErrorText);
        address1ErrorText = (TextView) findViewById(R.id.address1ErrorText);
        cityErrorText = (TextView) findViewById(R.id.cityErrorText);
        stateErrorText = (TextView) findViewById(R.id.stateErrorText);
        zipErrorText = (TextView) findViewById(R.id.zipErrorText);
        idErrorText = (TextView) findViewById(R.id.idErrorText);
        editDateOfBirth.addTextChangedListener(this);
        editFirstname.addTextChangedListener(this);
        editLastname.addTextChangedListener(this);
        editAddress1.addTextChangedListener(this);
        editCity.addTextChangedListener(this);
        editZipcode.addTextChangedListener(this);
        editphoneNumber.addTextChangedListener(this);
        editEmail.addTextChangedListener(this);
        editConfirmEmail.addTextChangedListener(this);
        editPassword.addTextChangedListener(this);
        editReenterpassword.addTextChangedListener(this);
        editId.addTextChangedListener(this);
        // 3.2 release populating values into fields if guest user wants to
        // register.
        if (checkoutShippmentMethodBean != null
                && checkoutShippmentMethodBean.getComponent() != null) {
            if (null != checkoutShippmentMethodBean.getComponent()
                    .getAnonymousEmailId()
                    && !checkoutShippmentMethodBean.getComponent()
                    .getAnonymousEmailId().isEmpty()) {
                editEmail.setText(checkoutShippmentMethodBean.getComponent()
                        .getAnonymousEmailId());
            }
            /*
             * if (checkoutShippmentMethodBean.getComponent().getCart() != null
			 * && checkoutShippmentMethodBean.getComponent().getCart()
			 * .getCreditCardPaymentGroups() != null &&
			 * !checkoutShippmentMethodBean.getComponent().getCart()
			 * .getCreditCardPaymentGroups().isEmpty()) {
			 * editFirstname.setText(checkoutShippmentMethodBean
			 * .getComponent().getCart().getCreditCardPaymentGroups()
			 * .get(0).getFirstName());
			 * editLastname.setText(checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getLastName());
			 * editAddress1.setText(checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getAddress1());
			 * editAddress2.setText(checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getAddress2());
			 * editCity.setText(checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getCity());
			 * String phone = checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getPhoneNumber();
			 * if (phone != null && phone.contains("-")) { phone =
			 * phone.replaceAll("-", ""); //
			 * Toast.makeText(JoinRewardsActivity.this, phone, // 2000).show();
			 * editphoneNumber.setText(phone); }
			 * 
			 * editZipcode.setText(checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getPostalCode());
			 * anonymousUserState = checkoutShippmentMethodBean.getComponent()
			 * .getCart().getCreditCardPaymentGroups().get(0) .getState(); }
			 * else
			 */
            if (checkoutShippmentMethodBean.getComponent().getCart() != null
                    && checkoutShippmentMethodBean.getComponent().getCart()
                    .getHardGoodShippingGroups() != null
                    && !checkoutShippmentMethodBean.getComponent().getCart()
                    .getHardGoodShippingGroups().isEmpty()) {
                editFirstname.setText(checkoutShippmentMethodBean
                        .getComponent().getCart().getHardGoodShippingGroups()
                        .get(0).getFirstName());
                editLastname.setText(checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getLastName());
                editAddress1.setText(checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getAddress1());
                editAddress2.setText(checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getAddress2());
                editCity.setText(checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0).getCity());
                String phone = checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getPhoneNumber();
                if (phone != null && phone.contains("-")) {
                    phone = phone.replaceAll("-", "");
                    // Toast.makeText(JoinRewardsActivity.this, phone,
                    // 2000).show();
                    editphoneNumber.setText(phone);
                }
                editZipcode.setText(checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getPostalCode());
                anonymousUserState = checkoutShippmentMethodBean.getComponent()
                        .getCart().getHardGoodShippingGroups().get(0)
                        .getState();
            }
        }
        btnContinue = (Button) findViewById(R.id.btnContinue);
        pd = new ProgressDialog(RegisterDetailsActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);

        pd.show();
        // loadingDialog.setVisibility(View.VISIBLE);
        invokeStateList();

        btnContinue.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                validationRegisterDetails();
            }
        });
        check_reward_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    optIn = "TRUE";
                } else {
                    optIn = "FALSE";
                }
            }
        });

        yes_sign_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    already_a_member_switch.setChecked(false);
                    sign = true;
                    isLoyaltyAccountOpted = true;
                } else {
                    sign = false;
                    isLoyaltyAccountOpted = false;
                }
            }
        });
        already_a_member_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    yes_sign_switch.setChecked(false);
                    member = true;
                    membershipId.setVisibility(View.VISIBLE);
                } else {
                    member = false;
                    membershipId.setVisibility(View.GONE);
                }
            }
        });


        Drawable drawable = getResources().getDrawable(
                R.drawable.beauty_pref_check_box);
        drawable.setBounds(0, 0, 60, 60);

//        radioMember.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (radioMember.isChecked() == true) {
//                    member = true;
//                    editId.setEnabled(true);
//                    membershipId.setVisibility(View.VISIBLE);
//                } else {
//                    member = false;
//                    membershipId.setVisibility(View.GONE);
//                    editId.setText("");
//                    editId.setEnabled(false);
//                }
//            }
//        });

//        radioSign.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (radioSign.isChecked() == true) {
//                    sign = true;
//                    isLoyaltyAccountOpted = true;
//                } else {
//                    sign = false;
//                    isLoyaltyAccountOpted = false;
//                }
//            }
//        });
//        radioThanks.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                if (radioThanks.isChecked() == true) {
//                    checked = true;
//                    isLoyaltyAccountOpted = false;
//                } else {
//                    checked = false;
//                    isLoyaltyAccountOpted = false;
//                }
//            }
//        });

        drawable = getResources().getDrawable(
                R.drawable.beauty_pref_radio_button);
        drawable.setBounds(0, 0, 60, 60);
//        radioSign.setButtonDrawable(android.R.color.transparent);
//        radioSign.setCompoundDrawables(drawable, null, null, null);
//        radioSign.setCompoundDrawablePadding(15);
//        radioSign.setPadding(35, 25, 3, 10);
        drawable = getResources().getDrawable(
                R.drawable.beauty_pref_radio_button);
        drawable.setBounds(0, 0, 60, 60);
//        radioMember.setButtonDrawable(android.R.color.transparent);
//        radioMember.setCompoundDrawables(drawable, null, null, null);
//        radioMember.setCompoundDrawablePadding(15);
//        radioMember.setPadding(35, 25, 3, 10);
        drawable = getResources().getDrawable(
                R.drawable.beauty_pref_radio_button);
        drawable.setBounds(0, 0, 60, 60);
//        radioThanks.setButtonDrawable(android.R.color.transparent);
//        radioThanks.setCompoundDrawables(drawable, null, null, null);
//        radioThanks.setCompoundDrawablePadding(15);
//        radioThanks.setPadding(35, 25, 3, 10);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static boolean validateDate(String dateofbirth) {

        String dateAndMonth[] = dateofbirth.split("/");

        try {
            if (dateAndMonth.length == 2) {

                String month = dateAndMonth[0];
                String day = dateAndMonth[1];

                int monthValue = Integer.parseInt(month);
                int dayValue = Integer.parseInt(day);

                if (monthValue >= 1 && monthValue <= 12) {

                    if (month.length() < 2 || day.length() < 2) {
                        return false;
                    } else {

                        if (dayValue >= 1 && dayValue <= 31) {
                            if (day.equals("31")
                                    && (month.equals("4") || month.equals("6")
                                    || month.equals("9")
                                    || month.equals("11")
                                    || month.equals("04")
                                    || month.equals("06") || month
                                    .equals("09"))) {
                                return false;
                            } else if (dayValue > 29 && month.equals("02")) {
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * Validation register details.
     */
    public void validationRegisterDetails() {

        email = editEmail.getText().toString().trim();
        confirmEmail = editConfirmEmail.getText().toString().trim();
        password = editPassword.getText().toString().trim();
        reenterpassword = editReenterpassword.getText().toString().trim();
        firstname = editFirstname.getText().toString().trim();
        lastname = editLastname.getText().toString().trim();
        address1 = editAddress1.getText().toString().trim();
        address2 = editAddress2.getText().toString().trim();
        city = editCity.getText().toString().trim();
        zipcode = editZipcode.getText().toString().trim();
        try {
            state = spState.getSelectedItem().toString().trim();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        dateofbirth = editDateOfBirth.getText().toString().trim();
        String phone = editphoneNumber.getText().toString().trim();
        if (phone.length() != 0 && phone != null) {
            if (phone.length() == 10) {
                phone = Utility.formatPhoneNumber(phone);
            }
        }
        if (firstname.length() == 0) {
            try {
                editFirstname.requestFocus();
                setError(editFirstname, firstNameErrorText,
                        FIRST_NAME_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (!firstname.matches("[a-zA-Z ]*")) {
            try {
                editFirstname.requestFocus();
                setError(editFirstname, firstNameErrorText,
                        "Please enter a valid first name");
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (lastname.length() == 0) {
            try {
                editLastname.requestFocus();
                setError(editLastname, lastNameErrorText,
                        LAST_NAME_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (!lastname.matches("[a-zA-Z ]*")) {
            try {
                editLastname.requestFocus();
                setError(editLastname, lastNameErrorText,
                        "Please enter a valid last name");
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (email.length() == 0) {
            try {
                editEmail.requestFocus();
                setError(editEmail, emailErrorText, EMAIL_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (!validateEmail(email)) {

            try {
                editEmail.requestFocus();
                setError(editEmail, emailErrorText, EMAIL_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (confirmEmail.length() == 0 || !email.equals(confirmEmail)) {
            try {
                editConfirmEmail.setText("");
                editConfirmEmail.requestFocus();
                setError(editConfirmEmail, confirm_emailErrorText,
                        CONFIRM_EMAIL_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (password.length() < 5) {
            try {
                editPassword.requestFocus();
                setError(editPassword, passwordErrorText,
                        PASSWORD_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (reenterpassword.length() < 5) {
            try {
                editReenterpassword.requestFocus();
                setError(editReenterpassword, repasswordErrorText,
                        CONFIRM_PASSWORD_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (!password.equals(reenterpassword)) {
            try {
                editPassword.setText("");
                editReenterpassword.setText("");
                editPassword.requestFocus();
                setError(editReenterpassword, repasswordErrorText,
                        CONFIRM_PASSWORD_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (dateofbirth.length() != 0 && !validateDate(dateofbirth)) {
            try {
                editDateOfBirth.requestFocus();
                setError(editDateOfBirth, dateofbirthErrorText,
                        DATE_OF_BIRTH_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (phone.length() == 0 || phone == null || phone.length() < 12) {
            try {
                editphoneNumber.requestFocus();
                setError(editphoneNumber, phoneErrorText,
                        PHONE_NUMBER_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (address1.length() == 0) {
            try {
                editAddress1.requestFocus();
                setError(editAddress1, address1ErrorText,
                        STREET_ADDRESS_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (city.length() == 0) {
            try {
                editCity.requestFocus();
                setError(editCity, cityErrorText, CITY_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (!isSpinnerSelected) {
            try {
                stateErrorText.setText(STATE_VALIDATION_MESSAGE);
                stateErrorText.setVisibility(View.VISIBLE);
                spState.requestFocus();
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (state.equalsIgnoreCase("* Select state")) {
            stateErrorText.setText(STATE_VALIDATION_MESSAGE);
            stateErrorText.setVisibility(View.VISIBLE);
            spState.requestFocus();
        } else if (zipcode.length() < 5) {
            try {
                editZipcode.requestFocus();
                setError(editZipcode, zipErrorText, ZIP_CODE_VALIDATION_MESSAGE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (member == true && editId.getText().toString().length() == 0) {
            try {
                setError(editId, idErrorText, BEAUTY_CLUB_NUMBER_VALIDATION_MESSAGE);
                editId.requestFocus();
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }
        } else if (null != origin
                && origin
                .equalsIgnoreCase("isfromApplyUltamateCard") && !member && !sign) {
            notifyUser("Please sign up for Ultamate Rewards in order to apply for the Ultamate Rewards Credit Card.", RegisterDetailsActivity.this);
        } else {
            phone1 = phone;
            pd.show();
            invokeUserCreation();
        }
    }

    /**
     * Invoke user creation.
     */
    private void invokeUserCreation() {
        InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.USER_CREATION_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateUserCreationParameters());
        Logger.Log("<<" + check);
        if (("check").equalsIgnoreCase(check)) {
            invokerParams.setUserSessionClearingRequired(true);
        }
        invokerParams.setUltaBeanClazz(LoginBean.class);
        UserCreationHandler userCreationHandler = new UserCreationHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<RegisterationActivity><invokeUserCreation><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateUserCreationParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "2");
        urlParams.put("value.login", email);
        urlParams.put("value.password", password);
        urlParams.put("value.confirmpassword", reenterpassword);
        urlParams.put("value.firstName", firstname);
        urlParams.put("value.lastName", lastname);
        urlParams.put("value.homeAddress.address1", address1);
        urlParams.put("value.homeAddress.address2", address2);
        urlParams.put("value.homeAddress.state", state);
        urlParams.put("value.homeAddress.city", city);
        urlParams.put("value.homeAddress.postalCode", zipcode);
        urlParams.put("value.dateOfBirth", dateofbirth);
        urlParams.put("zip", zipcode);
        /*
         * urlParams.put("day", day); urlParams.put("month", month);
		 */
        urlParams.put("phoneNo", phone1);
        urlParams.put("emailOpt", optIn);
        urlParams.put("confirmPassword", "TRUE");
        // if(checked){
//        if (checked) {
//            urlParams.put("value.member", "false");
//            urlParams.put("beautyClubMember", "false");
//        }
        if (member) {
            urlParams.put("value.member", "true");
            urlParams.put("beautyClubMember", "true");
            urlParams.put("beautyClubNumber", editId.getText().toString());

        } else if (sign) {
            urlParams.put("value.member", "true");
            urlParams.put("beautyClubMember", "join");

        } else {
            urlParams.put("value.member", "false");
            urlParams.put("beautyClubMember", "false");
        }
        return urlParams;
    }

    /**
     * The Class UserCreationHandler.
     */
    public class UserCreationHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<UserCreationHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(RegisterDetailsActivity.this);
                } else {
                    if (pd != null && pd.isShowing()) {
                        try {
                            pd.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                RegisterDetailsActivity.this);
                        setError(RegisterDetailsActivity.this,
                                getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                if (pd != null && pd.isShowing()) {
                    try {
                        pd.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                trackAppAction(RegisterDetailsActivity.this,
                        WebserviceConstants.REGISTRATION_ACTION);
                Logger.Log("<Registration><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                LoginBean ultaBean = (LoginBean) getResponseBean();
                final List<String> result = ultaBean.getErrorInfos();
                // final String []res=new
                // String[ultaBean.getErrorInfos().size()];
                Logger.Log("<UserCreationHandlerr><handleMessage><getResponseBean>>"
                        + result);
                if (null == result
                        || result
                        .get(0)
                        .toString()
                        .startsWith(
                                "Please enter valid beauty club number")
                        || result
                        .get(0)
                        .toString()
                        .startsWith(
                                "Some of the information we have on file ")
                        || result.get(0).toString()
                        .startsWith("Thank you for activating your ")
                        || result.get(0).toString().startsWith("Whoops")
                        || result.get(0).equalsIgnoreCase("")
                        || result.get(0).toString().startsWith("System Error")
                        || result
                        .get(0)
                        .toString()
                        .startsWith(
                                "Thank you for activating your rewards account at ULTA.com. Please click here to update your profile")
                        || result
                        .get(0)
                        .toString()
                        .startsWith(
                                "The rewards membership number you are trying ")
                        || result.get(0).toString()
                        .startsWith("Invalid member number")) {
                    if (null != result) {
                        if (null != result.get(0)) {
                            setError(RegisterDetailsActivity.this, result
                                    .get(0).toString());
                        }
                    }
                    UltaDataCache.getDataCacheInstance().setLoggedIn(true);
                    String errorMessage = "";
                    if (null != result) {
                        if (result
                                .get(0)
                                .toString()
                                .startsWith(
                                        "Please enter valid beauty club number")) {
                            errorMessage = "Please enter valid beauty club number";
                        } else if (result.get(0).toString()
                                .startsWith("Invalid member number")) {
                            errorMessage = result.get(0).toString();
                        } else if (result
                                .get(0)
                                .toString()
                                .startsWith(
                                        "Whoops. Your Ulta.com profile information ")) {
                            errorMessage = "Your Ulta.com profile information does not match the Rewards Membership records we have for you, therefore we could not complete the activation of your membership.If you need further assistance please call Guest Services at 1-866-983-8582.";
                        } else if (result
                                .get(0)
                                .toString()
                                .startsWith("The rewards membership number you are")) {
                            errorMessage = "The rewards membership number you are trying to use is already associated with another online account. If you need further assistance please call Guest Services at 1-866-983-8582.";
                        } else if (result
                                .get(0)
                                .toString()
                                .startsWith("System Error adding Rewards member account, please try again later.")) {
                            errorMessage = "System Error adding Rewards member account, please try again later.";
                        } else if (result
                                .get(0)
                                .toString()
                                .startsWith("System Error, please try again later")) {
                            errorMessage = "System Error adding Rewards member account, please try again later.";
                        } else if (result.get(0).toString()
                                .startsWith("System Error while adding BC member Id To your profile, please try again later")) {
                            errorMessage = "System Error while adding BC member Id To your profile, please try again later.";
                        } else {
                            errorMessage = result.get(0).toString();
                        }

                    }

                    if (isLoyaltyAccountOpted) {
                        trackAppAction(
                                RegisterDetailsActivity.this,
                                WebserviceConstants.LOYALTY_ACCOUNT_CREATED_EVENT_ACTION);
                    }
                    //Make the user as stay signed in.
                    Utility.encrypPasswordt(password.trim());

                    Utility.encryptUserName(email.trim());

                    loginPasswordBytes = UltaDataCache
                            .getDataCacheInstance()
                            .getEncodedPasswordBytes();

                    loginUserNameBytes = UltaDataCache
                            .getDataCacheInstance()
                            .getEncodedUserNameBytes();

                    String secretKey = Base64.encodeToString(
                            UltaDataCache.getDataCacheInstance()
                                    .getSecretKey().getEncoded(),
                            Base64.DEFAULT);

                    loginPassword = Base64.encodeToString(
                            loginPasswordBytes, Base64.DEFAULT);
                    loginUsername = Base64.encodeToString(
                            loginUserNameBytes, Base64.DEFAULT);
                    staySignedInSharedPreferences = getSharedPreferences(
                            WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                            MODE_PRIVATE);
                    staySignedInEditor = staySignedInSharedPreferences
                            .edit();
                    staySignedInEditor
                            .putString(
                                    WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                    loginPassword);
                    staySignedInEditor
                            .putString(
                                    WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                    loginUsername);
                    if (null != ultaBean.getComponent().get_firstName()) {

                        staySignedInEditor
                                .putString(
                                        WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                                        ultaBean.getComponent()
                                                .get_firstName());
                    }
                    staySignedInEditor
                            .putBoolean(
                                    WebserviceConstants.IS_STAY_SIGNED_IN,
                                    true);
                    staySignedInEditor.putBoolean(
                            WebserviceConstants.IS_LOGGED_IN, true);
                    staySignedInEditor
                            .putString(
                                    WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY,
                                    secretKey);

                    staySignedInEditor.commit();
                    Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME,
                            UltaConstants.REMEMBER_CLICKED,
                            getApplicationContext());
                    Utility.saveToSharedPreference(
                            UltaConstants.LOGGED_MAIL_ID, email,
                            getApplicationContext());

                    ConversantUtility.loginTag(email);
                    String message = WebserviceConstants.CREATE_ACCOUNT_SUCCESS;
                    if (sign) {
                        message = WebserviceConstants.CREATE_ACCOUNT_JOIN_REWARDS_SUCCESS;
                    } else if (member) {
                        message = WebserviceConstants.CREATE_ACCOUNT_ACTIVATE_REWARDS_SUCCESS;
                    }
                    if (!errorMessage.isEmpty()) {
                        message = WebserviceConstants.CREATE_ACCOUNT_SUCCESS;
                        message = message + errorMessage;
                    }
                    if (null != ultaBean.getComponent()) {
                        //email optin value
                        Utility.saveToSharedPreference(UltaConstants.EMAIL_OPT_IN, ultaBean.getComponent().getEmailOptIn(),
                                getApplicationContext());
                        //Ultamate Card Details
                        if (null != ultaBean.getComponent().getCreditCardType()) {
                            Utility.saveToSharedPreference(
                                    UltaConstants.REWARD_MEMBER,
                                    UltaConstants.ULTAMATE_CARD_TYPE, "" + ultaBean.getComponent().getCreditCardType(),
                                    getApplicationContext());
                        }

                        if (null != ultaBean.getComponent()
                                .get_beautyClubNumber()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardMember(true);
                            Utility.saveToSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, true, getApplicationContext());
                            Utility.saveToSharedPreference(
                                    UltaConstants.REWARD_MEMBER,
                                    UltaConstants.BEAUTY_CLUB_NUMBER, ultaBean.getComponent().get_beautyClubNumber(),
                                    getApplicationContext());
                            if (null != ultaBean.getComponent()
                                    .get_balancePoints()) {
                                UltaDataCache.getDataCacheInstance()
                                        .setRewardsBalancePoints(
                                                ultaBean.getComponent()
                                                        .get_balancePoints());
                            }
                            if (null != ultaBean.getComponent()
                                    .get_beautyClubNumber()) {
                                UltaDataCache
                                        .getDataCacheInstance()
                                        .setRewardsBeautyClubNumber(
                                                ultaBean.getComponent()
                                                        .get_beautyClubNumber());
                            }
                        } else

                        {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardMember(false);
                            Utility.saveToSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, false, getApplicationContext());
                            Utility.saveToSharedPreference(
                                    UltaConstants.REWARD_MEMBER,
                                    UltaConstants.BEAUTY_CLUB_NUMBER, "",
                                    getApplicationContext());
                        }
                    }
                    final Dialog alertDialog = showAlertDialog(
                            RegisterDetailsActivity.this,
                            message, "", "OK", "");
                    alertDialog.show();
                    messageTV.setVisibility(View.GONE);
                    mDisagreeButton.setVisibility(View.GONE);

                    mAgreeButton.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            if (null != origin && !" ".equals(origin)
                                    && origin.equalsIgnoreCase("basket")
                                    && fromCehckout == 2) {
                                if (isFromPayPal) {
                                    invokePayPalPaymentDetails();
                                }
                                setResult(RESULT_OK);
                                finish();

                            } else if (null != getIntent().getExtras()
                                    && (null != getIntent().getExtras()
                                    .getString("origin"))
                                    && getIntent().getExtras()
                                    .getString("origin")
                                    .equalsIgnoreCase("homeScreen")) {
                                // if (null != getIntent().getExtras()
                                // .getString("origin")) {
                                // if (getIntent()
                                // .getExtras()
                                // .getString("origin")
                                // .equalsIgnoreCase(
                                // "homeScreen")) {
                                Intent homeIntent = new Intent(
                                        RegisterDetailsActivity.this,
                                        HomeActivity.class);
                                homeIntent
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                | Intent.FLAG_ACTIVITY_NEW_TASK
                                                |

                                                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                startActivity(homeIntent);
                                // }
                                // }
                            } else if (null != origin && !" ".equals(origin)
                                    && origin.equalsIgnoreCase("basket")
                                    && fromCehckout != 2) {
                                /*
                                 * setResult(RESULT_OK); finish();
								 */
                                if (UltaDataCache.getDataCacheInstance()
                                        .isOnlyEgiftCard()) {
                                    /*
                                     * Intent intentForActivity = new Intent(
									 * RegisterDetailsActivity.this,
									 * ShippingAdressActivity.class);
									 * startActivity(intentForActivity);
									 * finish();
									 */

                                    Intent intentForActivity = new Intent(
                                            RegisterDetailsActivity.this,
                                            AddShippingAddressLogginUserActivity.class);
                                    startActivity(intentForActivity);
                                    finish();
                                } else {
                                    Intent intentForActivity = new Intent(
                                            RegisterDetailsActivity.this,
                                            FreeSamplesActivity.class);
                                    startActivity(intentForActivity);
                                }
                            } else if (null != origin
                                    && origin
                                    .equalsIgnoreCase("isfromMyRewards")) {
                                setResult(RESULT_OK);
                                finish();
                            } else if (null != origin
                                    && origin
                                    .equalsIgnoreCase("isfromApplyUltamateCard")) {
                                setResult(RESULT_OK);
                                finish();
                            } else if (null != origin
                                    && origin
                                    .equalsIgnoreCase("fromProductFavotitesTap")) {
                                setResult(RESULT_OK);
                                finish();

                            } else if (null != origin
                                    && origin
                                    .equalsIgnoreCase("pdpOlapicUpload")) {
                                setResult(RESULT_OK);
                                finish();

                            } else if (null != origin
                                    && !" ".equals(origin)
                                    && origin
                                    .equalsIgnoreCase("productdetails")) {
                                setResult(RESULT_OK);
                                finish();
                            } else if (null != origin && !" ".equals(origin)
                                    && origin.equalsIgnoreCase("relogin")) {
                                setResult(RESULT_OK);
                                finish();
                            } else if (null != origin
                                    && origin
                                    .equalsIgnoreCase("isfromHomeAccounts")) {
                                Intent myrewardsIntent = new Intent(
                                        RegisterDetailsActivity.this,
                                        MyAccountActivity.class);
                                if (null != result
                                        && result
                                        .get(0)
                                        .toString()
                                        .startsWith(
                                                "Invalid member number")) {
                                    myrewardsIntent.putStringArrayListExtra(
                                            "result",
                                            (ArrayList<String>) result);

                                } else if (null != result
                                        && result
                                        .get(0)
                                        .toString()
                                        .startsWith(
                                                "The rewards membership number")) {
                                    myrewardsIntent.putExtra("result1",
                                            "TheRewards");
                                } else if (null != result) {
                                    if (result
                                            .get(0)
                                            .toString()
                                            .startsWith(
                                                    "Thank you for activating your ")
                                            || result.get(0).startsWith(
                                            "Some of the information ")) {
                                        myrewardsIntent.putExtra("result2",
                                                "thankyou");
                                    }
                                }
                                /*
                                 * else if (null != result) { if (result .get(0)
								 * .startsWith(
								 * "System Error adding Rewards member account, please try again later"
								 * )) { myrewardsIntent.putExtra( "result3",
								 * "System"); } }
								 */
                                startActivity(myrewardsIntent);
                                finish();
                            } else {

                                Intent myrewardsIntent = new Intent(
                                        RegisterDetailsActivity.this,
                                        MyAccountActivity.class);
                                startActivity(myrewardsIntent);
                                finish();
                            }

                        }
                    });

					/*
                     * alertDialog = new AlertDialog.Builder(
					 * RegisterDetailsActivity.this).create();
					 * 
					 * alertDialog.setTitle("Registration Successful");
					 * alertDialog .setMessage(
					 * "Congratulations your account has been successfully created."
					 * + "\n" + errorMessage);
					 * UltaDataCache.getDataCacheInstance().setLoggedIn(true);
					 * alertDialog.setButton("Ok", new
					 * DialogInterface.OnClickListener() { public void
					 * onClick(DialogInterface arg0, int arg1) {} });
					 * alertDialog.show();
					 */
                    UltaDataCache.getDataCacheInstance().setOrderSubmitted(
                            false);
                } else {

                    try {
                        notifyUser(result.get(0), RegisterDetailsActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }

                Log.d("sign", "" + sign);

                if (sign) {
                    // Add a set of tags to a Tag Group and apply the change.
                    UAirship.shared().getPushManager().editTagGroups()
                            .addTag("reward", "rewards_member")
                            .apply();
                }
            }
        }
    }

    /**
     * Invoke state list.
     */
    private void invokeStateList() {
        result = UltaDataCache.getDataCacheInstance().getStateList();
        if (result == null) {
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
        } else {
            try {
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            anArrayOfStrings = new String[result.size()];
            result.toArray(anArrayOfStrings);

            MySpinnerAdapter<CharSequence> spinnerArrayAdapter = new MySpinnerAdapter<CharSequence>(
                    RegisterDetailsActivity.this, anArrayOfStrings);
            spState.setAdapter(spinnerArrayAdapter);
            // 3.2 release
            // Pre-populating anonymous user's state in the registration page
            for (int i = 0; i < anArrayOfStrings.length; i++) {
                if (null != anonymousUserState && !anonymousUserState.isEmpty()
                        && anArrayOfStrings[i].startsWith(anonymousUserState)) {
                    spState.setSelection(i);
                    isSpinnerSelected = true;
                    break;
                }
            }
            spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView,
                                           View selectedItemView, int position, long id) {
                    stateErrorText.setVisibility(View.GONE);
                    if (null != parentView) {
                        if (null != parentView.getChildAt(0)) {
                            ((TextView) parentView.getChildAt(0))
                                    .setTextColor(getResources().getColor(
                                            R.color.black));
                            ((TextView) parentView.getChildAt(0))
                                    .setTextSize(12);
                            ((TextView) parentView.getChildAt(0)).setPadding(5,
                                    0, 0, 0);
                        }
                        stateErrorText.setVisibility(View.GONE);
                    }
                    if (position != 0) {
                        stateErrorText.setVisibility(View.GONE);
                        isSpinnerSelected = true;
                    }
                    // opcoNamePosition = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
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
        // urlParams.put("atg-rest-return-form-handler-properties", "true");
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

            // loadingDialog.setVisibility(View.GONE);
            try {
                pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != getErrorMessage()) {

                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            RegisterDetailsActivity.this);
                    setError(RegisterDetailsActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<StatList><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                StateListBean ultaBean = (StateListBean) getResponseBean();
                result = ultaBean.getStateList();
                UltaDataCache.getDataCacheInstance().setStateList(result);
                Logger.Log("<StatListHandlerr><handleMessage><getResponseBean>>"
                        + result);

                anArrayOfStrings = new String[result.size()];
                result.toArray(anArrayOfStrings);
                List<String> resultNew = new ArrayList<String>();
                resultNew.add("* Select state");
                resultNew.addAll(result);
                setUpStateSpinner(spState, resultNew);

                // MySpinnerAdapter<CharSequence> spinnerArrayAdapter = new
                // MySpinnerAdapter<CharSequence>(
                // RegisterDetailsActivity.this, anArrayOfStrings);
                // spState.setAdapter(spinnerArrayAdapter);
                // 3.2 release
                // Pre-populating anonymous user's state in the registration
                // page
                for (int i = 0; i < anArrayOfStrings.length; i++) {
                    if (null != anonymousUserState
                            && !anonymousUserState.isEmpty()
                            && anArrayOfStrings[i]
                            .startsWith(anonymousUserState)) {
                        spState.setSelection(i);
                        isSpinnerSelected = true;
                        break;
                    }
                }
                // spState.setOnItemSelectedListener(new
                // AdapterView.OnItemSelectedListener() {
                // @Override
                // public void onItemSelected(AdapterView<?> parentView,
                // View selectedItemView, int position, long id) {
                // ((TextView) parentView.getChildAt(0))
                // .setTextColor(getResources().getColor(
                // R.color.black));
                // ((TextView) parentView.getChildAt(0)).setTextSize(12);
                // ((TextView) parentView.getChildAt(0)).setPadding(5, 0,
                // 0, 0);
                // if (position != 0) {
                // isSpinnerSelected = true;
                // }
                // // opcoNamePosition = position;
                // }
                //
                // @Override
                // public void onNothingSelected(AdapterView<?> arg0) {
                // }
                // });
            }
        }
    }

	/*
	 */

    /**
     * The Class MySpinnerAdapter.
     */
    private class MySpinnerAdapter<T> extends ArrayAdapter<T> {
        private Context m_cContext;

        /**
         * Instantiates a new my spinner adapter.
         */
        public MySpinnerAdapter(Context ctx, T[] objects) {
            super(ctx, android.R.layout.simple_spinner_item, objects);
            this.m_cContext = ctx;
        }

        boolean firsttime = true;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 3.2 second condition of if loop added to prepopulat the anonymous
            // user's state
            if (firsttime && !isSpinnerSelected) {
                firsttime = false;
                // just return some empty view
                TextView tv = new TextView(m_cContext);
                tv.setText("* Select state");
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setPadding(5, 0, 0, 0);
                return tv;
            }
            // let the array adapter takecare this time
            return super.getView(position, convertView, parent);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.widgets.flyin.OnDoneClickedListener#onDoneClicked()
     */
    @Override
    public void onDoneClicked() {

        validationRegisterDetails();
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {
        if (isSuccess) {
            invokeUserCreation();
        } else {
            if (pd != null && pd.isShowing()) {
                try {
                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    @SuppressWarnings("deprecation")
    @Override
    public void afterTextChanged(Editable s) {
		/*
		 * getWindow().setSoftInputMode(
		 * WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		 */
        if (s.hashCode() == editFirstname.getText().hashCode()) {
            editFirstname.setBackgroundDrawable(originalDrawable);
            firstNameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editLastname.getText().hashCode()) {
            editLastname.setBackgroundDrawable(originalDrawable);
            lastNameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editEmail.getText().hashCode()) {
            editEmail.setBackgroundDrawable(originalDrawable);
            emailErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editConfirmEmail.getText().hashCode()) {
            editConfirmEmail.setBackgroundDrawable(originalDrawable);
            confirm_emailErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editPassword.getText().hashCode()) {
            editPassword.setBackgroundDrawable(originalDrawable);
            passwordErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editReenterpassword.getText().hashCode()) {
            editReenterpassword.setBackgroundDrawable(originalDrawable);
            repasswordErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editphoneNumber.getText().hashCode()) {
            editphoneNumber.setBackgroundDrawable(originalDrawable);
            phoneErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editAddress1.getText().hashCode()) {
            editAddress1.setBackgroundDrawable(originalDrawable);
            address1ErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editCity.getText().hashCode()) {
            editCity.setBackgroundDrawable(originalDrawable);
            cityErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editZipcode.getText().hashCode()) {
            editZipcode.setBackgroundDrawable(originalDrawable);
            zipErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editDateOfBirth.getText().hashCode()) {
            editDateOfBirth.setBackgroundDrawable(originalDrawable);
            dateofbirthErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editId.getText().hashCode()) {
            idErrorText.setVisibility(View.GONE);
        }

        changeAllEditTextBackground();

    }

    public void setError(EditText editText, TextView errorTV, String message) {
        errorTV.setText("" + message);
        errorTV.setVisibility(View.VISIBLE);
        changeAllEditTextBackground();
        editText.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
    }

    public void changeAllEditTextBackground() {
        changeEditTextBackground(editFirstname);
        changeEditTextBackground(editLastname);
        changeEditTextBackground(editEmail);
        changeEditTextBackground(editConfirmEmail);
        changeEditTextBackground(editPassword);
        changeEditTextBackground(editReenterpassword);
        changeEditTextBackground(editDateOfBirth);
        changeEditTextBackground(editphoneNumber);
        changeEditTextBackground(editAddress1);
        changeEditTextBackground(editAddress2);
        changeEditTextBackground(editCity);
        changeEditTextBackground(editZipcode);
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
                        stateErrorText.setVisibility(View.GONE);
                        int position = stateSpinner.getSelectedItemPosition();
                        if (position != 0) {
                            isSpinnerSelected = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }

                });
    }
}