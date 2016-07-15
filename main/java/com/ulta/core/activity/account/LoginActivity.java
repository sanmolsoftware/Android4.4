/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.checkout.AddShippingAddressLogginUserActivity;
import com.ulta.core.activity.myprofile.FavoritesActivity;
import com.ulta.core.activity.product.FreeSamplesActivity;
import com.ulta.core.activity.product.HomeActivity;
import com.ulta.core.activity.rewards.RewardsActivity;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.account.ProfileBean;
import com.ulta.core.conf.UltaConstants;
import com.ulta.core.conf.WebserviceConstants;
import com.ulta.core.conf.types.HttpMethod;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * The Class LoginActivity.
 */
public class LoginActivity extends UltaBaseActivity implements OnClickListener,
        OnSessionTimeOut, TextWatcher {

    /**
     * The req code checkout register.
     */
    private static int REQ_CODE_FAVORITES_REGISTER = 10;
    private static final int REQ_CODE_LOGIN_REWARDS = 222;
    private static int REQ_CODE_REGISTER = 190;
    private static final int REQ_CODE_LOGIN_ULTAMATE_CARD_APPLY = 555;

    private static final int REQ_CODE_LOGIN = 0;

    private static final int REQ_CODE_UPLOAD = 111;

    /**
     * The username length error message.
     */
    private static String USERNAME_LENGTH_ERROR_MESSAGE = "Please enter your User name to Login";

    /** The email validation message. */
    // private static String EMAIL_VALIDATION_MESSAGE = "Invalid Email Entered";

    /**
     * The password length error message.
     */
    private static String PASSWORD_LENGTH_ERROR_MESSAGE = "Please enter your password to Login";

    /** The login error title. */
    /* private static String LOGIN_ERROR_TITLE = "Login Failed"; */

    /** The login error message. */
    /*
	 * private static String LOGIN_ERROR_MESSAGE =
	 * "Please check your password or username";
	 */

    /**
     * The btn register.
     */
    private Button btnLogin, btnRegister, btnGuest;

    /**
     * The edit username.
     */
    private EditText editPasswordLogin, editUsername;

    /**
     * The txt forgot username.
     */
    private TextView txtForgotUsername, txtOr;

    /** String to hold user entered mail id */
	/* private String guestEmail; */
    /**
     * The alert dialog.
     */
    AlertDialog alertDialog;

    private ProgressDialog pd;

    // private ProgressDialog rewardsProgressDialog;

    /**
     * The username.
     */
    private String username;

    /**
     * The password login.
     */
    private String passwordLogin;

    /**
     * The origin.
     */
    String origin, check;

    /**
     * The origin.
     */
    int fromCehckout;
    boolean isFromPayPal;
    // private UemAction loginAction;

    private Switch staySignedInSwitch;

    private boolean isStaySignedIn = true;

    private SharedPreferences staySignedInSharedPreferences;

    private Editor staySignedInEditor;

    private String loginPassword;

    private String loginUsername;

    private byte[] loginPasswordBytes;

    private byte[] loginUserNameBytes;

    /**
     * The profile bean.
     */
    private ProfileBean profileBean;

    private String beautyClubMember;

    private TextView mSignIntextView;

    private TextView mNewToUltaTextView;

    private SharedPreferences mRegistrationIdSharedPreferences;

    private SharedPreferences pushNotificationSharedPreferences;
    private TextView mUsernameErrorText, mPasswordErrorText;
    Drawable originalDrawable;
    private Typeface typeface;

    /*
     * (non-Javadoc)
     *
     * @see com.ulta.core.activity.UltaBaseActivity#onCreate(android.os.Bundle)
     */
    @SuppressLint("NewApi")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        setActivity(LoginActivity.this);
        trackAppState(this, WebserviceConstants.LOGIN);
        setTitle("Sign In");

        btnGuest = (Button) findViewById(R.id.btnGuestContinue);
        txtOr = (TextView) findViewById(R.id.txtOr);
        mUsernameErrorText = (TextView) findViewById(R.id.usernameErrorText);
        mPasswordErrorText = (TextView) findViewById(R.id.passwordErrorText);
        typeface = setHelveticaRegulartTypeFace();

        if (null != getIntent().getExtras()) {
            origin = getIntent().getExtras().getString("origin");
            Logger.Log("<<<<" + origin);

            if (null != origin) {
                if (origin.equals("basket")
                        || UltaDataCache.getDataCacheInstance()
                        .isExpressCheckout()) {
                    btnGuest.setVisibility(View.VISIBLE);
                    txtOr.setVisibility(View.VISIBLE);
                }
            }
            Logger.Log("<<<<" + getIntent().getExtras().getString("check"));
            check = getIntent().getExtras().getString("check");
            Logger.Log("<<<<" + check);
            fromCehckout = getIntent().getExtras().getInt("fromCehckout", 0);
            isFromPayPal = getIntent().getExtras().getBoolean("fromPayPal",
                    false);
        }

        if (fromCehckout == 2) {
            disableMenu();
            trackAppAction(LoginActivity.this,
                    WebserviceConstants.CHECKOUT_STEP_2_EVENT_ACTION);

            trackAppAction(LoginActivity.this,
                    WebserviceConstants.CHECKOUT_STEP_2_VISIT_EVENT_ACTION);
        }
        editUsername = (EditText) findViewById(R.id.editUsername);
        editUsername.requestFocus();
        editUsername.addTextChangedListener(this);
        originalDrawable = editUsername.getBackground();

        editPasswordLogin = (EditText) findViewById(R.id.editPasswordLogin);

        editPasswordLogin.addTextChangedListener(this);

        txtForgotUsername = (TextView) findViewById(R.id.txtForgotUsername);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnLogin.setTypeface(setHelveticaRegulartTypeFace());
        txtForgotUsername.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnRegister.setTypeface(setHelveticaRegulartTypeFace());
        mSignIntextView = (TextView) findViewById(R.id.signInText);
        mNewToUltaTextView = (TextView) findViewById(R.id.newtoultaText);

        mNewToUltaTextView.setTypeface(typeface);
        mSignIntextView.setTypeface(typeface);

        mUsernameErrorText.setTypeface(typeface);
        mPasswordErrorText.setTypeface(typeface);

        btnGuest.setOnClickListener(this);
        Utility.generateSecretKey();

        staySignedInSwitch = (Switch) findViewById(R.id.stay_signed_in_switch);
        staySignedInSwitch.setVisibility(View.VISIBLE);
        staySignedInSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            UltaDataCache.getDataCacheInstance()
                                    .setStaySignedIn(true);
                            isStaySignedIn = true;
                            Utility.encrypPasswordt(editPasswordLogin.getText()
                                    .toString());

                            Utility.encryptUserName(editUsername.getText()
                                    .toString());

                        } else {
                            UltaDataCache.getDataCacheInstance()
                                    .setStaySignedIn(false);
                            isStaySignedIn = false;
                        }
                    }
                });

        // SpannableString spannableStringForgotUsername = new SpannableString(
        // txtForgotUsername.getText().toString());
        // spannableStringForgotUsername.setSpan(new UnderlineSpan(), 0,
        // spannableStringForgotUsername.length(), 0);
        // txtForgotUsername.setText(spannableStringForgotUsername);
        editPasswordLogin
                .setOnEditorActionListener(new OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {

                        validationLogin();
                        return false;
                    }
                });
        pd = new ProgressDialog(LoginActivity.this);
        setProgressDialogLoadingColor(pd);
        pd.setMessage(LOADING_PROGRESS_TEXT);
        pd.setCancelable(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQ_CODE_FAVORITES_REGISTER == requestCode) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        } else if (REQ_CODE_REGISTER == requestCode) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Validation login.
     */
    public void validationLogin() {

        username = editUsername.getText().toString();
        passwordLogin = editPasswordLogin.getText().toString();
        loginPassword = editPasswordLogin.getText().toString();
        loginUsername = editUsername.getText().toString();

        alertDialog = new AlertDialog.Builder(this).create();

        if (username.length() == 0) {

            try {
                // notifyUser(USERNAME_LENGTH_ERROR_MESSAGE,
                // LoginActivity.this);
                editUsername.requestFocus();
                editUsername
                        .setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
                mUsernameErrorText.setText("" + USERNAME_LENGTH_ERROR_MESSAGE);
                mUsernameErrorText.setVisibility(View.VISIBLE);
            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else if (passwordLogin.length() < 5) {
            try {
                // notifyUser(PASSWORD_LENGTH_ERROR_MESSAGE,
                // LoginActivity.this);

                editPasswordLogin
                        .setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
                mPasswordErrorText.setText("" + PASSWORD_LENGTH_ERROR_MESSAGE);
                mPasswordErrorText.setVisibility(View.VISIBLE);

            } catch (WindowManager.BadTokenException e) {
            } catch (Exception e) {
            }

        } else {
            // UltaDataCache.getDataCacheInstance().setLoggedIn(true);
            pd.show();
            // loginAction = UemAction
            // .enterAction(WebserviceConstants.ACTION_LOGIN_INVOCATION);
            // loginAction.reportEvent("Login Started");
            mRegistrationIdSharedPreferences = getSharedPreferences(
                    WebserviceConstants.CHANNEL_ID_PREF, 0);
            pushNotificationSharedPreferences = getSharedPreferences(
                    WebserviceConstants.PUSH_NOTIFICATION_SHAREDPREF,
                    MODE_PRIVATE);
            boolean isActive = pushNotificationSharedPreferences.getBoolean(
                    WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true);
            String registrationId = mRegistrationIdSharedPreferences.getString(
                    WebserviceConstants.PUSH_CHANNEL_ID, "");
            invokeLogin(username, passwordLogin, registrationId, isActive);

        }
    }

    /**
     * Invoke login.
     *
     * @param username      the username
     * @param passwordLogin the password login
     */
    private void invokeLogin(String username, String passwordLogin,
                             String registrationId, boolean isActive) {
        InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.LOGIN_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateLoginParameters(username,
                passwordLogin, registrationId, isActive));
        invokerParams.setUltaBeanClazz(LoginBean.class);
        // invokerParams.setUserSessionClearingRequired(true);
        LoginHandler loginHandler = new LoginHandler();
        invokerParams.setUltaHandler(loginHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @param username      the username
     * @param passwordLogin the password login
     * @return Map<String, String>
     */
    private Map<String, String> populateLoginParameters(String username,
                                                        String passwordLogin, String registrationId, boolean isActive) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("value.login", username);
        urlParams.put("value.password", passwordLogin);
        if (isFromPayPal) {
            urlParams.put("fromPaypal", "true");
        }
        urlParams.put("token", registrationId);
        if (isActive) {
            urlParams.put("isActive", "true");
        } else {
            urlParams.put("isActive", "false");
        }
        return urlParams;

    }

    /**
     * The Class LoginHandler.
     */
    public class LoginHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<LoginHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (!(null != origin && origin.equalsIgnoreCase("isfromMyRewards"))) {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }

            if (null != getErrorMessage()) {

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                if (getErrorMessage().startsWith("401")) {
                    // loginAction.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // loginAction.leaveAction();
                    askRelogin(LoginActivity.this);
                } else {
                    try {
                        // loginAction.reportError(getErrorMessage(),
                        // WebserviceConstants.DYN_ERRCODE_LOGIN_ACTIVITY);
                        // loginAction.leaveAction();
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                LoginActivity.this);
                        setError(LoginActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {

                trackAppAction(LoginActivity.this,
                        WebserviceConstants.LOGIN_ACTION);


                Logger.Log("<Login><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                LoginBean ultaBean = (LoginBean) getResponseBean();
                List<String> result = ultaBean.getErrorInfos();
                resetCache();
                Logger.Log("<LoginHandler><handleMessage><getResponseBean>>"
                        + result);
                if (null == result
                        || result.get(0).equalsIgnoreCase("")
                        || result
                        .get(0)
                        .startsWith(
                                "Thank you for activating your rewards account")
                        || result
                        .get(0)
                        .startsWith(
                                "Some of the information we have on file for you needs updating")) {

                    UltaDataCache.getDataCacheInstance().setLoggedIn(true);
                    UltaDataCache.getDataCacheInstance().setLoginName(username);

                    //GMOB-3500 Conversant tag
                    ConversantUtility.loginTag(username);
                    clearAOCookie();
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
                        } else {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardMember(false);
                            Utility.saveToSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, false, getApplicationContext());
                            Utility.saveToSharedPreference(
                                    UltaConstants.REWARD_MEMBER,
                                    UltaConstants.BEAUTY_CLUB_NUMBER, "",
                                    getApplicationContext());
                        }
                        if (null != ultaBean.getComponent().get_balancePoints()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardsBalancePoints(
                                            ultaBean.getComponent()
                                                    .get_balancePoints());
                        }
                        if (null != ultaBean.getComponent()
                                .get_beautyClubNumber()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setRewardsBeautyClubNumber(
                                            ultaBean.getComponent()
                                                    .get_beautyClubNumber());
                        }
                        if (null != ultaBean.getComponent().get_firstName()) {
                            UltaDataCache.getDataCacheInstance()
                                    .setUserFirstName(
                                            ultaBean.getComponent()
                                                    .get_firstName());
                        }

                        if (null != ultaBean.getComponent()
                                .getShoppingCartCount()
                                && !ultaBean.getComponent()
                                .getShoppingCartCount().isEmpty()) {
                            try {
                                int bagCount = Integer.parseInt(ultaBean
                                        .getComponent().getShoppingCartCount());
                                if (bagCount > 0) {
                                    setItemCountInBasket(bagCount);
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }

                        if (null != ultaBean.getComponent()
                                .getFavoritesItemCount()
                                && !ultaBean.getComponent()
                                .getFavoritesItemCount().isEmpty()) {
                            try {
                                int favCount = Integer
                                        .parseInt(ultaBean.getComponent()
                                                .getFavoritesItemCount());
                                if (favCount > 0) {
                                    setFavoritesCountInNavigationDrawer(favCount);
                                    if (isPersisted) {
                                        createMenuData();
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    /**
                     * Auto Login
                     */
                    if (isStaySignedIn) {

                        Utility.encrypPasswordt(editPasswordLogin.getText()
                                .toString());

                        Utility.encryptUserName(editUsername.getText()
                                .toString());

                        loginPasswordBytes = UltaDataCache
                                .getDataCacheInstance()
                                .getEncodedPasswordBytes();

                        loginUserNameBytes = UltaDataCache
                                .getDataCacheInstance()
                                .getEncodedUserNameBytes();

                        String secretKey = Base64.encodeToString(UltaDataCache
                                .getDataCacheInstance().getSecretKey()
                                .getEncoded(), Base64.DEFAULT);

                        loginPassword = Base64.encodeToString(
                                loginPasswordBytes, Base64.DEFAULT);
                        loginUsername = Base64.encodeToString(
                                loginUserNameBytes, Base64.DEFAULT);
                        staySignedInSharedPreferences = getSharedPreferences(
                                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                                MODE_PRIVATE);
                        staySignedInEditor = staySignedInSharedPreferences
                                .edit();
                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                loginPassword);
                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                loginUsername);
                        if (null != ultaBean.getComponent().get_firstName()) {

                            staySignedInEditor
                                    .putString(
                                            WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                                            ultaBean.getComponent()
                                                    .get_firstName());
                        }
                        staySignedInEditor.putBoolean(
                                WebserviceConstants.IS_STAY_SIGNED_IN, true);

                        staySignedInEditor.putBoolean(
                                WebserviceConstants.IS_LOGGED_IN, true);

                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY,
                                secretKey);

                        staySignedInEditor.commit();
                    } else {
                        staySignedInSharedPreferences = getSharedPreferences(
                                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                                MODE_PRIVATE);
                        staySignedInEditor = staySignedInSharedPreferences
                                .edit();
                        staySignedInEditor.putBoolean(
                                WebserviceConstants.IS_STAY_SIGNED_IN, false);

                        staySignedInEditor.putBoolean(
                                WebserviceConstants.IS_LOGGED_IN, true);

                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_USERNAME,
                                " ");
                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_PASSWORD,
                                " ");
                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY,
                                " ");
                        staySignedInEditor.putString(
                                WebserviceConstants.STAY_SIGNED_IN_FIRST_NAME,
                                ultaBean.getComponent().get_firstName());
                        staySignedInEditor.commit();
                    }

                    // int
                    // isfromHome=getIntent().getExtras().getInt("isfromHome",0);
                    int isfromUltaBase = 0;
					/* int isfromProductDetails = 0; */
                    if (null != getIntent().getExtras()) {
                        isfromUltaBase = getIntent().getExtras().getInt(
                                "isfromUltaBase", 0);
						/*
						 * isfromProductDetails =
						 * getIntent().getExtras().getInt(
						 * "isfromProductDetails", 0);
						 */
                    }

                    Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME,
                            UltaConstants.REMEMBER_CLICKED,
                            getApplicationContext());

                    Utility.saveToSharedPreference(
                            UltaConstants.LOGGED_MAIL_ID, username,
                            getApplicationContext());

                    if (UltaDataCache.getDataCacheInstance().isOrderSubmitted()) {
                        UltaDataCache.getDataCacheInstance().setOrderSubmitted(
                                false);
                    }

                    if (null != origin
                            && origin.equalsIgnoreCase("pdpOlapicUpload")) {

                        setResult(RESULT_OK);
                        finish();

                    }

                    if (null != origin
                            && origin.equalsIgnoreCase("isfromHomeFavorites")) {

                        Intent myrewardsIntent = new Intent(LoginActivity.this,
                                FavoritesActivity.class);
                        myrewardsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK |

                                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(myrewardsIntent);

                    }
                    if (null != origin
                            && origin
                            .equalsIgnoreCase("fromProductFavotitesTap")) {
                        setResult(RESULT_OK);
                        finish();
                    }
                    if (null != origin
                            && origin.equalsIgnoreCase("isfromHomeAccounts")) {

                        Intent myAccountsIntent = new Intent(
                                LoginActivity.this, MyAccountActivity.class);
                        myAccountsIntent
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK |

                                        Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(myAccountsIntent);
                    } else if (null != origin
                            && origin.equalsIgnoreCase("isfromMyRewards")) {

                        setResult(RESULT_OK);
                        finish();

                    } else if (null != origin
                            && origin.equalsIgnoreCase("isfromApplyUltamateCard")) {
                        setResult(RESULT_OK);
                        finish();
                    } else if (null != origin
                            && origin.equalsIgnoreCase("homeScreen")) {

                        Intent homeIntent = new Intent(LoginActivity.this,
                                HomeActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_NEW_TASK |

                                Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(homeIntent);

                    } else if (isfromUltaBase == 3) {

                        Intent myAccountsIntent = new Intent(
                                LoginActivity.this, MyAccountActivity.class);
                        myAccountsIntent
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                        | Intent.FLAG_ACTIVITY_NEW_TASK |

                                        Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        startActivity(myAccountsIntent);
                    } else if (null != origin
                            && origin.equalsIgnoreCase("productdetails")) {
                        finish();
                    } else if (null != origin
                            && origin.equalsIgnoreCase("fromSideMenu")) {
                        Intent intentForFavoritesActivity = new Intent(
                                LoginActivity.this, FavoritesActivity.class);
                        startActivity(intentForFavoritesActivity);
                        finish();
                    } else if (null != origin && !" ".equals(origin)
                            && "basket".equalsIgnoreCase("origin")
                            || (fromCehckout == 1)) {
                        Logger.Log(">>>>>>>>> to freesample");
                        // setResult(RESULT_OK);
                        if (UltaDataCache.getDataCacheInstance()
                                .isOnlyEgiftCard()) {
							/*
							 * Intent intentForLoginActivity = new Intent(
							 * LoginActivity.this,
							 * ShippingAdressActivity.class);
							 * startActivity(intentForLoginActivity); finish();
							 */

                            Intent intentForLoginActivity = new Intent(
                                    LoginActivity.this,
                                    AddShippingAddressLogginUserActivity.class);
                            startActivity(intentForLoginActivity);
                            finish();
                        } else {
                            Intent intentForLoginActivity = new Intent(
                                    LoginActivity.this,
                                    FreeSamplesActivity.class);
                            startActivity(intentForLoginActivity);
                            finish();
                        }
                    } else if (null != origin && !" ".equals(origin)
                            && "basket".equalsIgnoreCase("origin")
                            || (fromCehckout == 2)) {
                        trackAppState(LoginActivity.this,
                                WebserviceConstants.CHECKOUT_LOGIN);
                        if (isFromPayPal) {
                            invokePayPalPaymentDetails();
                        }
                        finish();

                    }
                    // loginAction.reportEvent("Login success");
                    // loginAction.leaveAction();
                    if (!(null != origin && origin
                            .equalsIgnoreCase("isfromMyRewards"))) {
                        finish();
                    }

                }
                // login failure
                else {

                    try {
                        pd.dismiss();
                        // loginAction.reportError(result.get(0), 105);
                        // loginAction.leaveAction();


                        if (!result.get(0).startsWith("You are already logged")) {
                            notifyUser(
                                    Utility.formatDisplayError(result.get(0)),
                                    LoginActivity.this);
                        }
                        setError(LoginActivity.this, result.get(0));
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }

            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtForgotUsername:
                trackAppState(this,
                        WebserviceConstants.ACCOUNT_FORGOT_PASSWORD_STEP1);
                Intent txtForgotUsernameIntent = new Intent(LoginActivity.this,
                        ForgotLoginActivity.class);
                txtForgotUsernameIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK |

                        Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                if (fromCehckout == 1) {
                    txtForgotUsernameIntent.putExtra("fromCehckout", 1);
                }
                startActivity(txtForgotUsernameIntent);
                break;

            case R.id.btnLogin:
                validationLogin();
                break;

            case R.id.btnRegister:
                Intent registerIntent = new Intent(LoginActivity.this,
                        RegisterDetailsActivity.class);
                registerIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                Logger.Log("<<<<" + origin);
                registerIntent.putExtra("check", check);
                if (null != origin && origin.equalsIgnoreCase("basket")
                        && fromCehckout != 2) {
                    registerIntent.putExtra("origin", origin);

                    startActivity(registerIntent);
                } else if (null != origin && origin.equalsIgnoreCase("basket")
                        && fromCehckout == 2) {
                    registerIntent.putExtra("origin", origin);
                    registerIntent.putExtra("fromCehckout", 2);
                    registerIntent.putExtra("fromPayPal", isFromPayPal);
                    startActivityForResult(registerIntent, REQ_CODE_REGISTER);
                } else if (null != origin
                        && origin.equalsIgnoreCase("isforEgcActivity")) {

                    String id = getIntent().getExtras().getString("id");
                    String skuId = getIntent().getExtras().getString("skuId");

                    registerIntent.putExtra("id", id);
                    Logger.Log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>id>>>>>>>>>>>>>>>>>>>>>"
                            + id);

                    registerIntent.putExtra("skuId", skuId);
                    registerIntent.putExtra("origin", origin);

                    startActivity(registerIntent);

                } else if (null != origin
                        && origin.equalsIgnoreCase("productdetails")) {
                    registerIntent.putExtra("origin", origin);
                    startActivityForResult(registerIntent,
                            REQ_CODE_FAVORITES_REGISTER);
                } else if (fromCehckout == 1) {
                    registerIntent.putExtra("fromCehckout", 1);
                    startActivity(registerIntent);
                } else if (null != origin && origin.equalsIgnoreCase("homeScreen")) {
                    registerIntent.putExtra("origin", "homeScreen");
                    startActivity(registerIntent);

                } else if (null != origin
                        && origin.equalsIgnoreCase("isfromHomeFavorites")) {
                    registerIntent.putExtra("fromHomeforFavorites", 1);
                    startActivity(registerIntent);

                } else if (null != origin
                        && origin.equalsIgnoreCase("isfromHomeAccounts")) {
                    registerIntent.putExtra("origin", "isfromHomeAccounts");
                    startActivity(registerIntent);
                    finish();
                } else if (null != origin && origin.equalsIgnoreCase("relogin")) {
                    registerIntent.putExtra("origin", origin);
                    // registerIntent.putExtra("check", check);
                    Logger.Log("<<<<<" + check);
                    startActivityForResult(registerIntent, REQ_CODE_REGISTER);
                } else if (null != origin
                        && origin.equalsIgnoreCase("isfromMyRewards")) {
                    registerIntent.putExtra("origin", "isfromMyRewards");
                    startActivityForResult(registerIntent, REQ_CODE_LOGIN_REWARDS);
                    setResult(RESULT_OK);
                    finish();
                } else if (null != origin
                        && origin.equalsIgnoreCase("isfromApplyUltamateCard")) {
                    registerIntent.putExtra("origin", "isfromApplyUltamateCard");
                    startActivityForResult(registerIntent, REQ_CODE_LOGIN_ULTAMATE_CARD_APPLY);
                    setResult(RESULT_OK);
                    finish();
                } else if (null != origin
                        && origin.equalsIgnoreCase("fromProductFavotitesTap")) {
                    registerIntent.putExtra("origin", "fromProductFavotitesTap");
                    startActivity(registerIntent);
                    startActivityForResult(registerIntent, REQ_CODE_LOGIN);
                    setResult(RESULT_OK);
                    finish();

                } else if (null != origin
                        && origin.equalsIgnoreCase("pdpOlapicUpload")) {
                    registerIntent.putExtra("origin", "pdpOlapicUpload");
                    startActivity(registerIntent);
                    startActivityForResult(registerIntent, REQ_CODE_UPLOAD);
                    setResult(RESULT_OK);
                    finish();

                } else if (null != getIntent().getExtras()) {
                    if (getIntent().getExtras().getBoolean("myAccountfromUltaBase")) {
                        registerIntent.putExtra("myAccountfromUltaBase", true);
                        startActivity(registerIntent);
                        finish();
                    }

                } else {
                    startActivity(registerIntent);
                }

                break;
            // 3.2 release
            // Modified for 3.5 release
            case R.id.btnGuestContinue:
                Intent guestLogin = new Intent(LoginActivity.this,
                        GuestLoginActivity.class);
                guestLogin.putExtra("fromPayPal", isFromPayPal);
                startActivity(guestLogin);
                break;

            default:
                break;
        }
    }

    // My Profile Webservice Part

    /**
     * Invoke my profile details.
     */
    private void invokeMyProfileDetails() {
        // rewardsProgressDialog = new ProgressDialog(this);
        // rewardsProgressDialog.setMessage("Loading..");
        // rewardsProgressDialog.show();
        InvokerParams<ProfileBean> invokerParams = new InvokerParams<ProfileBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.MY_PROFILE_DETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams
                .setUrlParameters(populateRetrieveMyProfileDetailsHandlerParameters());
        invokerParams.setUltaBeanClazz(ProfileBean.class);
        RetrieveMyProfileDetailsHandler retrieveMyProfileDetailsHandler = new RetrieveMyProfileDetailsHandler();
        invokerParams.setUltaHandler(retrieveMyProfileDetailsHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<MyAccountActivity><invokeMyProfileDetails()><UltaException>>"
                    + ultaException);
            // rewardsProgressDialog.dismiss();
        }

    }

    /**
     * The Class RetrieveMyProfileDetailsHandler.
     */
    public class RetrieveMyProfileDetailsHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            // askRelogin("");
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                finish();
            }
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    // rewardsProgressDialog.dismiss();
                    askRelogin(LoginActivity.this);
                    if (UltaDataCache.getDataCacheInstance().isLoggedIn()) {
                        refreshPage();
                    }

                } else {
                    try {
                        // rewardsProgressDialog.dismiss();
                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                LoginActivity.this);
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));

                Log.d("beautyClubMember", "beautyClubMember");
                profileBean = (ProfileBean) getResponseBean();
                // rewardsProgressDialog.dismiss();
                if (null != profileBean) {
                    if (null != profileBean.getBeautyClubNumber()) {

                        Logger.Log("<MyProfileActivity>" + "BeanPopulated");
                        beautyClubMember = profileBean.getBeautyClubNumber();
                        String planDesc = "";
                        if (null != profileBean.getBeautyClubPlanType()) {
                            if (null != profileBean.getBeautyClubPlanType()
                                    .getPlanDesc()) {
                                planDesc = profileBean.getBeautyClubPlanType()
                                        .getPlanDesc();
                            }
                        }
                        Intent intentRewards = new Intent(LoginActivity.this,
                                RewardsActivity.class);
                        intentRewards.putExtra("MemberId", beautyClubMember);
                        intentRewards.putExtra("PlanDescription", planDesc);
                        startActivity(intentRewards);

                    } else {
                        Toast.makeText(LoginActivity.this, "RewardsActivity", Toast.LENGTH_SHORT).show();

                        Intent joinRewards = new Intent(LoginActivity.this,
                                JoinRewardsActivity.class);

                        joinRewards.putExtra("origin", "isfromMyRewards");
                        joinRewards.putExtra("check", "");
                        joinRewards.putExtra("memberId", beautyClubMember);
                        joinRewards
                                .putExtra("userName", profileBean.getEmail());
                        joinRewards.putExtra("firstName",
                                profileBean.getFirstName());
                        joinRewards.putExtra("lastName",
                                profileBean.getLastName());
                        joinRewards.putExtra("address1", profileBean
                                .getHomeAddress().getAddress1());
                        joinRewards.putExtra("address2", profileBean
                                .getHomeAddress().getAddress2());
                        joinRewards.putExtra("city", profileBean
                                .getHomeAddress().getCity());
                        joinRewards.putExtra("strZip", profileBean
                                .getHomeAddress().getPostalCode());
                        joinRewards.putExtra("phone", profileBean
                                .getHomeAddress().getPhoneNumber());
                        joinRewards.putExtra("dob",
                                profileBean.getDateOfBirth());
                        joinRewards.putExtra("state", profileBean
                                .getHomeAddress().getState());
                        startActivity(joinRewards);
                    }
                }
            }
        }
    }

    /**
     * Populate retrieve my profile details handler parameters.
     *
     * @return the map
     */
    private Map<String, String> populateRetrieveMyProfileDetailsHandlerParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-return-form-handler-exceptions", "TRUE");
        urlParams.put("atg-rest-depth", "2");
        return urlParams;
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            invokeMyProfileDetails();
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
        if (s.hashCode() == editUsername.getText().hashCode()) {
            editUsername.setBackgroundDrawable(originalDrawable);
            mUsernameErrorText.setVisibility(View.GONE);
        } else if (s.hashCode() == editPasswordLogin.getText().hashCode()) {
            editPasswordLogin.setBackgroundDrawable(originalDrawable);
            mPasswordErrorText.setVisibility(View.GONE);
        }

    }
}
