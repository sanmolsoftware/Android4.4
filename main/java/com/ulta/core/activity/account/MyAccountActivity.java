/**
 * Copyright(c) ULTA, Inc. All Rights reserved.
 */

package com.ulta.core.activity.account;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ulta.R;
import com.ulta.core.activity.UltaBaseActivity;
import com.ulta.core.activity.myprofile.FavoritesActivity;
import com.ulta.core.activity.rewards.ClubUltaActivity;
import com.ulta.core.activity.rewards.GiftCardsTabActivity;
import com.ulta.core.activity.rewards.MobileOffersActivity;
import com.ulta.core.activity.rewards.NonSignedInRewardsActivity;
import com.ulta.core.activity.rewards.RewardsActivity;
import com.ulta.core.bean.StatusOnlyResponseBean;
import com.ulta.core.bean.account.LoginBean;
import com.ulta.core.bean.account.ProfileBean;
import com.ulta.core.bean.checkout.AddressBean;
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

import java.util.HashMap;
import java.util.Map;

import static com.ulta.core.conf.UltaConstants.LOADING_PROGRESS_TEXT;

/**
 * The Class MyAccountActivity.
 */
public class MyAccountActivity extends UltaBaseActivity implements
        OnClickListener, OnSessionTimeOut {

    public static final int REQUEST_CODE_JOIN = 007;

    /**
     * The lyt change password.
     */
    LinearLayout llAddressLine2, main_layout, lytGiftCard, lytRewards,
            lytMyOrderHistory, lytMybeautyList, lytPreferredShippingAddress,
             lytPaymentMethod, lytChangePassword,
            lytMember, lytCoupons, lytPurchase, lytBeautyPreference;
    FrameLayout loadingDialog;

    /**
     * The tv zip code.
     */
    TextView tvCountryName;
    TextView tvFirstname;
    TextView tvLastname;
    TextView tvPhoneNumber;
    TextView tvUserName;
    TextView tvAddline1;
    TextView tvAddline2;
    TextView tvCityName;
    TextView tvStateName;
    TextView tvZipCode;
    TextView tvUserNameText;
    TextView tvMember;
    TextView tvMembershipAccount;

    /**
     * The btn logout.
     */
    Button btnLogout;

    /** The default shipping address. */
    /* private AddressBean defaultShippingAddress; */

    /**
     * The home address.
     */
    private AddressBean homeAddress;

    /**
     * The zip.
     */
    private String firstName, lastName, dateOfBirth, userName,
            beautyClubNumber, balancePoints, phNumber, addLine1, addLine2,
            city, state, country, zip;

    private String planDesc, planType, planId;

    /**
     * The profile bean.
     */
    private ProfileBean profileBean;

    private Switch mPush_notification_switch, emai_subscription_switch;

    private ProgressDialog mPushNotificationDialog;

	/* private boolean isStaySignedIn = false; */

    private SharedPreferences staySignedInSharedPreferences,
            pushNotificationSharedPreferences;

    private Editor staySignedInEditor, pushNotificationEditor;

    private SharedPreferences mRegistrationIdSharedPreferences;

	/* private String loginPassword; */

    // private LinearLayout staySignedInLayout;

    private View mGiftCardBalDividerView, above_mobile_divider;

    private SharedPreferences mMobileOfferSharedPreferences;
    private Button signOutBtn;
    private boolean isEmailOpted = true;

    @Override
    public void onLogout() {
        // super.onLogout();
        finish();
    }

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // isOnCreateCalled=true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);

        // staySignedInLayout = (LinearLayout) findViewById(R.id.lytSignout);
        staySignedInSharedPreferences = getSharedPreferences(
                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);
        pushNotificationSharedPreferences = getSharedPreferences(
                WebserviceConstants.PUSH_NOTIFICATION_SHAREDPREF, MODE_PRIVATE);
        pushNotificationEditor = pushNotificationSharedPreferences.edit();
        mMobileOfferSharedPreferences = getSharedPreferences(
                WebserviceConstants.MOBILE_OFFER_SHAREDPREF, MODE_PRIVATE);
        /* pushNotificationEditor = pushNotificationSharedPreferences.edit(); */
        intViews();
        /*
         * if (!staySignedInSharedPreferences.getBoolean(
		 * WebserviceConstants.IS_STAY_SIGNED_IN, false)) {
		 * staySignedInLayout.setVisibility(View.GONE);
		 * mStaySignedInDividerView.setVisibility(View.GONE); } else {
		 * UltaDataCache.getDataCacheInstance().setStaySignedIn(true); }
		 *
		 */

        trackAppState(this, WebserviceConstants.MY_ACCOUNT);
        setTitle("My Account");
        setActivity(MyAccountActivity.this);

        if (null != getIntent().getExtras()
                && null != getIntent().getExtras().getStringArrayList("result")) {
            for (String a : getIntent().getExtras()
                    .getStringArrayList("result")) {
                // notifyUser(a, MyAccountActivity.this);
                /*
                 * final AlertDialog alertDialog = new AlertDialog.Builder(
				 * MyAccountActivity.this).create();
				 * alertDialog.setCancelable(false);
				 * alertDialog.setTitle("Invalid Member ID");
				 * alertDialog.setMessage(a); alertDialog.setButton("OK", new
				 * DialogInterface.OnClickListener() {
				 *
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { alertDialog.dismiss(); } }); alertDialog.show();
				 */

                final Dialog alertDialog = showAlertDialog(
                        MyAccountActivity.this, "Invalid Member ID", a, "Ok",
                        "");
                alertDialog.show();
                alertDialog.setCancelable(false);
                mDisagreeButton.setVisibility(View.GONE);
                mAgreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
            }

        } else if (null != getIntent().getExtras()
                && null != getIntent().getExtras().getString("result1")) {
            if (getIntent().getExtras().getString("result1")
                    .equalsIgnoreCase("TheRewards")) {
                /*
                 * final AlertDialog alertDialog = new AlertDialog.Builder(
				 * MyAccountActivity.this).create();
				 * alertDialog.setTitle("Sorry"); alertDialog .setMessage(
				 * "The rewards membership number you are trying to use is already associated with another online account. If you need further assistance please call Guest Services at 1-866-983-8582"
				 * ); alertDialog.setButton("OK", new
				 * DialogInterface.OnClickListener() {
				 *
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { alertDialog.dismiss(); } }); alertDialog.show();
				 */

                final Dialog alertDialog = showAlertDialog(
                        MyAccountActivity.this,
                        "Sorry",
                        "The rewards membership number you are trying to use is already associated with another online account. If you need further assistance please call Guest Services at 1-866-983-8582",
                        "Ok", "");
                alertDialog.show();

                mDisagreeButton.setVisibility(View.GONE);
                mAgreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
            }
        } else if (null != getIntent().getExtras()
                && null != getIntent().getExtras().getString("result2")) {
            if (getIntent().getExtras().getString("result2")
                    .equalsIgnoreCase("thankyou")) {
                /*
                 * final AlertDialog alertDialog = new AlertDialog.Builder(
				 * MyAccountActivity.this).create();
				 * alertDialog.setTitle("Success"); alertDialog .setMessage(
				 * "Thank you for activating your rewards account at ULTA.com."
				 * ); alertDialog.setButton("OK", new
				 * DialogInterface.OnClickListener() {
				 *
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { alertDialog.dismiss(); } }); alertDialog.show();
				 */

                final Dialog alertDialog = showAlertDialog(
                        MyAccountActivity.this,
                        "Success",
                        "Thank you for activating your rewards account at ULTA.com.",
                        "Ok", "");
                alertDialog.show();

                mDisagreeButton.setVisibility(View.GONE);
                mAgreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
            }
        } else if (null != getIntent().getExtras()
                && null != getIntent().getExtras().getString("result3")) {
            if (getIntent().getExtras().getString("result3")
                    .equalsIgnoreCase("System")) {
                /*
				 * final AlertDialog alertDialog = new AlertDialog.Builder(
				 * MyAccountActivity.this).create();
				 * alertDialog.setTitle("Sorry"); alertDialog .setMessage(
				 * "System Error adding Rewards member account, please try again later."
				 * ); alertDialog.setButton("OK", new
				 * DialogInterface.OnClickListener() {
				 *
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { alertDialog.dismiss(); } }); alertDialog.show();
				 */

                final Dialog alertDialog = showAlertDialog(
                        MyAccountActivity.this,
                        "Sorry",
                        "System Error adding Rewards member account, please try again later.",
                        "Ok", "");
                alertDialog.show();

                mDisagreeButton.setVisibility(View.GONE);
                mAgreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });
            }
        }
        emai_subscription_switch = (Switch) findViewById(R.id.emai_subscription_switch);
        isEmailOpted = Utility.retrieveBooleanFromSharedPreference(
                UltaConstants.EMAIL_OPT_IN, MyAccountActivity.this);
        emai_subscription_switch.setChecked(isEmailOpted);
        emai_subscription_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    isEmailOpted = true;
                    invokeEmailOptin(isEmailOpted);

                } else {
                    isEmailOpted = false;
                    invokeEmailOptin(isEmailOpted);
                }
            }
        });
        mPush_notification_switch = (Switch) findViewById(R.id.push_notification_switch);
        mPush_notification_switch.setChecked(pushNotificationSharedPreferences
                .getBoolean(WebserviceConstants.IS_PUSH_NOTIFICATION_ON, true));
        mPush_notification_switch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {

                        if (isChecked) {
                            pushNotificationEditor
                                    .putBoolean(
                                            WebserviceConstants.IS_PUSH_NOTIFICATION_ON,
                                            true);
                            pushNotificationEditor.commit();
                            toggleGcmRegistration(true);

                        } else {
                            pushNotificationEditor
                                    .putBoolean(
                                            WebserviceConstants.IS_PUSH_NOTIFICATION_ON,
                                            false);
                            pushNotificationEditor.commit();
                            toggleGcmRegistration(false);

                        }
                    }
                });

        setOnclickListeners();
        loadingDialog.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        // registerForLogoutBroadcast();
        invokeMyProfileDetails();
        if (null == beautyClubNumber) {
            lytRewards.setVisibility(View.GONE);
            above_mobile_divider.setVisibility(View.GONE);
            mGiftCardBalDividerView.setVisibility(View.GONE);
        } else {
            lytRewards.setVisibility(View.VISIBLE);
            above_mobile_divider.setVisibility(View.VISIBLE);
            mGiftCardBalDividerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Sets the onclick listeners.
     */
    private void setOnclickListeners() {

        lytGiftCard.setOnClickListener(this);
        lytRewards.setOnClickListener(this);
        lytMyOrderHistory.setOnClickListener(this);
        lytMybeautyList.setOnClickListener(this);
        lytPaymentMethod.setOnClickListener(this);
        lytPreferredShippingAddress.setOnClickListener(this);
        lytChangePassword.setOnClickListener(this);
        lytCoupons.setOnClickListener(this);
        lytBeautyPreference.setOnClickListener(this);
        signOutBtn.setOnClickListener(this);
    }

    /**
     * Int views.
     */
    private void intViews() {
        lytBeautyPreference = (LinearLayout) findViewById(R.id.lytBeautyPreference);
        lytMember = (LinearLayout) findViewById(R.id.linearLayoutMember);
        lytGiftCard = (LinearLayout) findViewById(R.id.lytGiftCard);
        lytRewards = (LinearLayout) findViewById(R.id.lytRewards);
        lytMyOrderHistory = (LinearLayout) findViewById(R.id.lytMyOrderHistory);
        lytMybeautyList = (LinearLayout) findViewById(R.id.lytMybeautyList);
        lytPreferredShippingAddress = (LinearLayout) findViewById(R.id.lytPreferredShippingAddress);
        lytPaymentMethod = (LinearLayout) findViewById(R.id.lytPaymentMethod);
        lytChangePassword = (LinearLayout) findViewById(R.id.lytChangePassword);
        lytCoupons = (LinearLayout) findViewById(R.id.lytCoupons);
        tvFirstname = (TextView) findViewById(R.id.tvFirstName);
        tvLastname = (TextView) findViewById(R.id.tvLastName);
        tvPhoneNumber = (TextView) findViewById(R.id.tvPhone);
        tvUserName = (TextView) findViewById(R.id.tvEmail);
        tvAddline1 = (TextView) findViewById(R.id.tvaddLine1);
        tvAddline2 = (TextView) findViewById(R.id.tvaddLine2);
        tvCityName = (TextView) findViewById(R.id.tvcity);
        tvZipCode = (TextView) findViewById(R.id.tvZip);
        tvStateName = (TextView) findViewById(R.id.tvState);
        tvCountryName = (TextView) findViewById(R.id.tvCountry);
        tvMember = (TextView) findViewById(R.id.tvMembershipAccountNumber);
        tvMembershipAccount = (TextView) findViewById(R.id.tvMembershipAccount);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        llAddressLine2 = (LinearLayout) findViewById(R.id.llAddressLine2);
        loadingDialog = (FrameLayout) findViewById(R.id.loadingDialog);
        tvUserNameText = (TextView) findViewById(R.id.tvUserNameText);
        lytMybeautyList.setVisibility(View.VISIBLE);
       // tvFirstname.setTypeface(setHelveticaRegulartTypeFace());
        tvFirstname.setTypeface(null,Typeface.BOLD);
       // tvLastname.setTypeface(setHelveticaRegulartTypeFace());
        tvLastname.setTypeface(null,Typeface.BOLD);
        tvPhoneNumber.setTypeface(setHelveticaRegulartTypeFace());
        tvUserName.setTypeface(setHelveticaRegulartTypeFace());
        tvAddline1.setTypeface(setHelveticaRegulartTypeFace());
        tvAddline2.setTypeface(setHelveticaRegulartTypeFace());
        tvZipCode.setTypeface(setHelveticaRegulartTypeFace());
        tvStateName.setTypeface(setHelveticaRegulartTypeFace());
        tvCountryName.setTypeface(setHelveticaRegulartTypeFace());
        tvCityName.setTypeface(setHelveticaRegulartTypeFace());
        tvMember.setTypeface(setHelveticaRegulartTypeFace());
       // tvMembershipAccount.setTypeface(setHelveticaRegulartTypeFace());
        tvMembershipAccount.setTypeface(null,Typeface.BOLD);
       // tvUserNameText.setTypeface(setHelveticaRegulartTypeFace());
        tvUserNameText.setTypeface(null, Typeface.BOLD);
        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        mGiftCardBalDividerView = (View) findViewById(R.id.giftCardBaldividerView);

        above_mobile_divider = (View) findViewById(R.id.above_mobile_divider);

        lytGiftCard.setVisibility(View.GONE);


    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.lytGiftCard:
                Intent intentForGiftCard = new Intent(MyAccountActivity.this,
                        GiftCardsTabActivity.class);
                startActivity(intentForGiftCard);
                break;
            case R.id.lytBeautyPreference:
                creatingPageName(WebserviceConstants.ACCOUNT_BEAUTY_PREFRENCES);
                Intent intentForBeautyPref = new Intent(MyAccountActivity.this,
                        BeautyPreferenceActivity.class);
                startActivity(intentForBeautyPref);
                break;
            case R.id.lytRewards:
                Logger.Log("<<<<<<<<<<" + planType);
                trackAppState(MyAccountActivity.this,
                        WebserviceConstants.HOME_MY_REWARDS_LOGGED_IN);
                if (null != planId && planId.equalsIgnoreCase("1")) {
                    Intent intentForClubRewards = new Intent(
                            MyAccountActivity.this, ClubUltaActivity.class);
                    intentForClubRewards.putExtra("MemberId", beautyClubNumber);

                    startActivity(intentForClubRewards);

                } else {
                    if (null != beautyClubNumber) {
                        Intent intentForRewards = new Intent(MyAccountActivity.this,
                                RewardsActivity.class);
                        intentForRewards.putExtra("Ponits", balancePoints);
                        intentForRewards.putExtra("MemberId", beautyClubNumber);
                        intentForRewards.putExtra("PlanDescription", planDesc);

                        startActivity(intentForRewards);
                    }
                    else
                    {
                        Intent intentMyAccount = new Intent(MyAccountActivity.this, NonSignedInRewardsActivity.class);
                        intentMyAccount.putExtra("from",
                                "fromRewards");
                        startActivity(intentMyAccount);
                    }
                }
                break;
            case R.id.lytChangePassword:
                creatingPageName(WebserviceConstants.ACCOUNT_CHANGE_PASSWORD_STEP1);
                Intent intentForChangePassword = new Intent(MyAccountActivity.this,
                        ChangePasswordActivity.class);
                startActivity(intentForChangePassword);
                break;

            case R.id.lytMyOrderHistory:
                creatingPageName(WebserviceConstants.ACCOUNT_ORDER_HISTORY);
                Intent intentForOrderHistory = new Intent(MyAccountActivity.this,
                        MyOrderHistoryActivity.class);
                startActivity(intentForOrderHistory);
                break;

            case R.id.lytPreferredShippingAddress:
                creatingPageName(WebserviceConstants.ACCOUNT_SHIPPING);
                Intent intentForMyPreferredAddress = new Intent(
                        MyAccountActivity.this,
                        MyPrefferedShippingAddressActvity.class);
                startActivity(intentForMyPreferredAddress);
                break;

            case R.id.lytPaymentMethod:
                creatingPageName(WebserviceConstants.ACCOUNT_PAYMENT);
                Intent intentForPaymentMethod = new Intent(MyAccountActivity.this,
                        PaymentMethodListActvity.class);
                startActivity(intentForPaymentMethod);
                break;

            case R.id.lytMybeautyList:
                creatingPageName(WebserviceConstants.ACCOUNT_FAVORITES);
                Intent intentForBeautyList = new Intent(MyAccountActivity.this,
                        FavoritesActivity.class);
                startActivity(intentForBeautyList);
                break;

            case R.id.lytCoupons:
                creatingPageName(WebserviceConstants.ACCOUNT_MOBILE_OFFERS);
                if (null != mMobileOfferSharedPreferences.getString(
                        WebserviceConstants.MOBILE_OFFER_ARRAY, null)) {
                    String[] offerUrl = mMobileOfferSharedPreferences.getString(
                            WebserviceConstants.MOBILE_OFFER_ARRAY, null)
                            .split(",");
                    Intent intentMobileOffers = new Intent(MyAccountActivity.this,
                            MobileOffersActivity.class);
                    intentMobileOffers.putExtra("couponUrl", offerUrl[0]);
                    intentMobileOffers.putExtra("position", 0);
                    // intentMobileOffers.putExtra("couponBean",
                    // offerUrl);
                    intentMobileOffers.putExtra("couponBean", offerUrl);
                    startActivity(intentMobileOffers);
                }

                break;
            case R.id.signOutBtn:
                final Dialog alertDialog = showAlertDialog(MyAccountActivity.this,
                        "Sign Out", "Are you sure you want to sign out?",
                        "Sign Out", "Cancel");
                Button agreeButton = (Button) alertDialog
                        .findViewById(R.id.btnAgree);
                Button disagreeButton = (Button) alertDialog
                        .findViewById(R.id.btnDisagree);
                agreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        pd = new ProgressDialog(MyAccountActivity.this);
                        setProgressDialogLoadingColor(pd);
                        pd.setMessage(LOADING_PROGRESS_TEXT);
                        pd.setCancelable(false);
                        pd.show();
                        invokeLogout();
                    }
                });
                disagreeButton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
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

        InvokerParams<ProfileBean> invokerParams = new InvokerParams<ProfileBean>();
        invokerParams
                .setServiceToInvoke(WebserviceConstants.MY_PROFILE_DETAILS_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.GET);
        invokerParams.setCookieHandlingSkip(false);
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
         * @see android.os.Handler#handleMessage(Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));
            // askRelogin("");
            if (null != getErrorMessage()) {
                if (getErrorMessage().startsWith("401")) {
                    askRelogin(MyAccountActivity.this);

                } else {
                    loadingDialog.setVisibility(View.GONE);
                    try {

                        notifyUser(
                                Utility.formatDisplayError(getErrorMessage()),
                                MyAccountActivity.this);
                        setError(MyAccountActivity.this, getErrorMessage());
                    } catch (WindowManager.BadTokenException e) {
                    } catch (Exception e) {
                    }
                }
            } else {
                loadingDialog.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                Logger.Log("<RetrieveMyProfileDetailsHandler><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                profileBean = (ProfileBean) getResponseBean();
                if (null != profileBean) {
                    if (null != profileBean.getErrorInfos()) {
                        setError(MyAccountActivity.this, profileBean
                                .getErrorInfos().get(0));
                    }
                    Logger.Log("<MyProfileActivity>" + "BeanPopulated");
                    beautyClubNumber = profileBean.getBeautyClubNumber();
                    UltaDataCache.getDataCacheInstance().setLoyaltyClubId(
                            beautyClubNumber);
                    if (null != profileBean.getBalancePoints()) {
                        UltaDataCache.getDataCacheInstance()
                                .setRewardsBalancePoints(
                                        profileBean.getBalancePoints());
                    }
                    if (null != profileBean.getBeautyClubNumber()) {
                        UltaDataCache
                                .getDataCacheInstance()
                                .setRewardsBeautyClubNumber(
                                        profileBean.getBeautyClubNumber());
                    }
                    Utility.saveToSharedPreference(
                            UltaConstants.REWARD_MEMBER,
                            UltaConstants.BEAUTY_CLUB_NUMBER, beautyClubNumber,
                            getApplicationContext());
                    trackEvarsUsingPageName(MyAccountActivity.this,
                            WebserviceConstants.MY_ACCOUNT,
                            WebserviceConstants.LOYALTY_CODE_KEY,
                            beautyClubNumber);

                    balancePoints = profileBean.getBalancePoints();
					/* gender = profileBean.getGender(); */
                    dateOfBirth = profileBean.getDateOfBirth();
                    firstName = profileBean.getFirstName();
                    homeAddress = profileBean.getHomeAddress();
                    lastName = profileBean.getLastName();
                    userName = profileBean.getEmail();

                    if (null != profileBean.getBeautyClubPlanType()) {
                        planType = profileBean.getBeautyClubPlanType()
                                .getPlanType();
                        planDesc = profileBean.getBeautyClubPlanType()
                                .getPlanDesc();
                    }
                    planId = profileBean.getPlanId();
                    setViews();

                }
            }
        }
    }

    /**
     * Sets the views.
     */
    private void setViews() {

        if (null != beautyClubNumber) {
            lytMember.setVisibility(View.VISIBLE);
            lytRewards.setVisibility(View.VISIBLE);
            above_mobile_divider.setVisibility(View.VISIBLE);
            mGiftCardBalDividerView.setVisibility(View.VISIBLE);
            tvMember.setText(beautyClubNumber);
        } else {
            lytMember.setVisibility(View.GONE);
            lytRewards.setVisibility(View.VISIBLE);
            above_mobile_divider.setVisibility(View.VISIBLE);
            mGiftCardBalDividerView.setVisibility(View.VISIBLE);
        }

        if (firstName != null)
            tvFirstname.setText(firstName);

        if (lastName != null)
            tvLastname.setText(lastName);

        // Reported error on playstore : Fixed
        if (null != homeAddress) {
            if (null != homeAddress.getPhoneNumber()) {
                phNumber = homeAddress.getPhoneNumber();
            }

            if (phNumber != null && !phNumber.isEmpty())
                tvPhoneNumber.setText(format(phNumber));

            if (userName != null) {
                tvUserName.setText(userName);
                tvUserNameText.setVisibility(View.VISIBLE);
            } else {
                tvUserNameText.setVisibility(View.INVISIBLE);
            }

            addLine1 = homeAddress.getAddress1();
            if (addLine1 != null)
                tvAddline1.setText(addLine1 + "");

            addLine2 = homeAddress.getAddress2();
            if (null != addLine2 && addLine2.length() != 0) {
                llAddressLine2.setVisibility(LinearLayout.VISIBLE);
                tvAddline2.setText(addLine2 + "");
            } else {
                llAddressLine2.setVisibility(LinearLayout.GONE);
            }

            city = homeAddress.getCity();
            if (city != null)
                tvCityName.setText(city + ", ");

            state = homeAddress.getState();
            if (state != null)
                tvStateName.setText(state + ", ");

            country = homeAddress.getCountry();
            if (country != null)
                tvCountryName.setText(country + " ");

            zip = homeAddress.getPostalCode();
            if (zip != null)
                tvZipCode.setText(zip);
        }
        // if (balancePoints != null) {
        // String balance = "0";
        // if (balancePoints.contains(".")) {
        // balance = balancePoints
        // .substring(0, balancePoints.indexOf("."));
        // tvDispalyPoints.setText(balance);
        // } else {
        // tvDispalyPoints.setText(balancePoints);
        // }
        // } else
        // tvDispalyPoints.setText("0");
        //
    }

    private CharSequence format(String phNumber2) {
        String phNumber = phNumber2.substring(0, 3);
        phNumber2 = " " + phNumber + phNumber2.substring(3);
        return phNumber2;
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

    // end of MyProfileDetails webservice Part

    /**
     * Invoke logout.
     */
    protected void invokeLogout() {
        InvokerParams<StatusOnlyResponseBean> invokerParams = new InvokerParams<StatusOnlyResponseBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.LOGOUT_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(HttpProtocol.http);
        invokerParams.setUrlParameters(populateChangePasswordParameters());
        invokerParams.setUltaBeanClazz(StatusOnlyResponseBean.class);
        invokerParams.setUserSessionClearingRequired(true);
        LogoutHandler userCreationHandler = new LogoutHandler();
        invokerParams.setUltaHandler(userCreationHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<MyAccountActivity><invokeUserCreation><UltaException>>"
                    + ultaException);

        }
    }

    /**
     * Method to populate the URL parameter map.
     *
     * @return Map<String, String>
     */
    private Map<String, String> populateChangePasswordParameters() {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        // urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-depth", "1");

        return urlParams;
    }

    /**
     * The Class LogoutHandler.
     */
    public class LogoutHandler extends UltaHandler {

        /**
         * Handle message.
         *
         * @param msg the msg
         * @see android.os.Handler#handleMessage(Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<LogoutHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                try {
                    notifyUser(Utility.formatDisplayError(getErrorMessage()),
                            MyAccountActivity.this);
                    setError(MyAccountActivity.this, getErrorMessage());
                } catch (WindowManager.BadTokenException e) {
                } catch (Exception e) {
                }
            } else {
                Logger.Log("<Logout><handleMessage><getResponseBean>>"
                        + (getResponseBean()));
                StatusOnlyResponseBean ultaBean = (StatusOnlyResponseBean) getResponseBean();
                String responseStatus = ultaBean.getResponseStatus();
                Logger.Log("<LogoutHandler><handleMessage><getResponseBean>>"
                        + responseStatus);
                // remember me is changed to unclicked state
                Utility.saveToSharedPreference(UltaConstants.REMEMBER_ME,
                        UltaConstants.BLANK_STRING, getApplicationContext());
                resetCache();
                UltaDataCache.getDataCacheInstance().setStaySignedIn(true);
                UltaDataCache.getDataCacheInstance().setLoggedIn(false);
                UltaDataCache.getDataCacheInstance().setRewardMember(false);
                UltaDataCache.getDataCacheInstance().setRewardsBalancePoints(null);
                Utility.saveToSharedPreference(
                        UltaConstants.REWARD_MEMBER,
                        UltaConstants.ULTAMATE_CARD_TYPE, "",
                        getApplicationContext());
                Utility.saveToSharedPreference(UltaConstants.REWARD_MEMBER, UltaConstants.IS_REWARD_MEMBER, false, getApplicationContext());
                //email optin value
                Utility.saveToSharedPreference(UltaConstants.EMAIL_OPT_IN, true,
                        getApplicationContext());
                Utility.saveToSharedPreference(
                        UltaConstants.REWARD_MEMBER,
                        UltaConstants.BEAUTY_CLUB_NUMBER, "",
                        getApplicationContext());
                UltaDataCache.getDataCacheInstance().setRedeemLevelPoints(null);
                staySignedInSharedPreferences = getSharedPreferences(
                        WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF,
                        MODE_PRIVATE);
                setItemCountInBasket(0);
                setFavoritesCountInNavigationDrawer(0);
                staySignedInEditor = staySignedInSharedPreferences.edit();
                staySignedInEditor.putBoolean(
                        WebserviceConstants.IS_STAY_SIGNED_IN, false);
                staySignedInEditor.putBoolean(WebserviceConstants.IS_LOGGED_IN,
                        false);
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");
                staySignedInEditor.putString(
                        WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");
                staySignedInEditor.commit();
                finish();
            }

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        }
    }

    public void refreshPage() {
        loadingDialog.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        // registerForLogoutBroadcast();
        invokeMyProfileDetails();

    }

    private void creatingPageName(String categoryname) {

        trackAppState(this, categoryname);
    }

	/*
	 * void toggleGcmRegistration(boolean isEnabled) { final boolean
	 * isRegistered = !TextUtils.isEmpty(GCMRegistrar .getRegistrationId(this));
	 * if (isEnabled) { if (!isRegistered) { // Register to GCM if we haven't
	 * registered already GCMRegistrar.register(this,
	 * WebserviceConstants.SENDER_ID); // pushNotificationRegistration();
	 * invokePushNotificationService("", "true"); } } else { // Unregister only
	 * if we were registered already. if (isRegistered) {
	 * GCMRegistrar.unregister(this); // pushNotificationRegistration();
	 * invokePushNotificationService("", "false"); } } }
	 */

    void toggleGcmRegistration(boolean isActive) {
        pushNotificationRegistration(isActive);
    }

    public void pushNotificationRegistration(boolean isActive) {

        mRegistrationIdSharedPreferences = getSharedPreferences(
                WebserviceConstants.CHANNEL_ID_PREF, 0);

        staySignedInSharedPreferences = getSharedPreferences(
                WebserviceConstants.STAY_SIGNED_IN_SHAREDPREF, MODE_PRIVATE);

        boolean isStaySignedIn = staySignedInSharedPreferences.getBoolean(
                WebserviceConstants.IS_STAY_SIGNED_IN, false);

        String registrationId = mRegistrationIdSharedPreferences.getString(
                WebserviceConstants.PUSH_CHANNEL_ID, "");

        if (isStaySignedIn) {

            String userName = staySignedInSharedPreferences.getString(
                    WebserviceConstants.STAY_SIGNED_IN_USERNAME, " ");

            String loginPassword = staySignedInSharedPreferences.getString(
                    WebserviceConstants.STAY_SIGNED_IN_PASSWORD, " ");

            String secretKey = staySignedInSharedPreferences.getString(
                    WebserviceConstants.STAY_SIGNED_IN_SECRET_KEY, " ");

            if (!loginPassword.equalsIgnoreCase(" ")
                    && !userName.equalsIgnoreCase(" ")) {
                loginPassword = Utility.decryptString(loginPassword, secretKey);
                userName = Utility.decryptString(userName, secretKey);
            }

            if (!(userName.equalsIgnoreCase(" ") && loginPassword
                    .equalsIgnoreCase(" "))) {
                isPersisted = true;
                invokeLogin(userName, loginPassword, registrationId, isActive);
            }

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
        mPushNotificationDialog = new ProgressDialog(MyAccountActivity.this);
        mPushNotificationDialog.setTitle("Registering..");
        mPushNotificationDialog.setCancelable(false);
        mPushNotificationDialog.show();
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
            if (null != mPushNotificationDialog
                    && mPushNotificationDialog.isShowing()) {
                mPushNotificationDialog.dismiss();
            }

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
        urlParams.put("value.login", username);
        urlParams.put("value.password", passwordLogin);
        urlParams.put("token", registrationId);
        if (isActive) {
            urlParams.put("isActive", "true");
        } else {
            urlParams.put("isActive", "false");
        }

        // if(isFromPayPal)
        // {
        // urlParams.put("fromPaypal", "true");
        // }
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
         * @see android.os.Handler#handleMessage(Message)
         */
        public void handleMessage(Message msg) {
            Logger.Log("<LoginHandler><handleMessage><getErrorMessage>>"
                    + (getErrorMessage()));

            if (null != getErrorMessage()) {
                if (null != mPushNotificationDialog
                        && mPushNotificationDialog.isShowing()) {
                    mPushNotificationDialog.dismiss();
                }
                if (getErrorMessage().startsWith("401")) {
                    // mLoginAction.reportError(
                    // WebserviceConstants.AUTHENTICATION_REQUIRED, 401);
                    // mLoginAction.leaveAction();
                    askRelogin(MyAccountActivity.this);
                }
            } else {
                Utility.saveToSharedPreference(UltaConstants.EMAIL_OPT_IN, isEmailOpted,
                        getApplicationContext());
                if (null != mPushNotificationDialog
                        && mPushNotificationDialog.isShowing()) {
                    mPushNotificationDialog.dismiss();

                }

            }
        }
    }

    @Override
    public void onLoginDoneAfterUnauthorizedError(boolean isSuccess) {

        if (isSuccess) {
            invokeMyProfileDetails();
        }
    }

    private void invokeEmailOptin(boolean isEmailOpted) {
        mPushNotificationDialog = new ProgressDialog(MyAccountActivity.this);
        mPushNotificationDialog.setMessage("Loading..");
        setProgressDialogLoadingColor(mPushNotificationDialog);
        mPushNotificationDialog.setCancelable(false);
        mPushNotificationDialog.show();
        InvokerParams<LoginBean> invokerParams = new InvokerParams<LoginBean>();
        invokerParams.setServiceToInvoke(WebserviceConstants.EMAIL_OPT_IN_SERVICE);
        invokerParams.setHttpMethod(HttpMethod.POST);
        invokerParams.setHttpProtocol(WebserviceUtility.securityEnabler());
        invokerParams.setUrlParameters(populateEmailOptInParameters(isEmailOpted));
        invokerParams.setUltaBeanClazz(LoginBean.class);
        LoginHandler loginHandler = new LoginHandler();
        invokerParams.setUltaHandler(loginHandler);
        try {
            new ExecutionDelegator(invokerParams);
        } catch (UltaException ultaException) {
            Logger.Log("<LoginsActivity><invokeLogin><UltaException>>"
                    + ultaException);

        }
    }

    private Map<String, String> populateEmailOptInParameters(boolean isEmailOpted) {
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("atg-rest-output", "json");
        urlParams.put("atg-rest-depth", "1");
        urlParams.put("atg-rest-return-form-handler-exceptions", "true");
        urlParams.put("atg-rest-return-form-handler-properties", "true");
        urlParams.put("value.emailOptIn", "" + isEmailOpted);
        return urlParams;

    }

}
